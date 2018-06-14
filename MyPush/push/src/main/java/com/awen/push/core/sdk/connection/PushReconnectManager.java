package com.awen.push.core.sdk.connection;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.awen.push.core.impl.exceptions.PurifyException;
import com.awen.push.core.sdk.ConnectionInfo;
import com.awen.push.core.utils.SL;

import java.util.Iterator;

/**
 * 推送无限重连机制,间隔5秒连接一次
 */

public class PushReconnectManager extends AbsReconnectionManager {

    /**
     * 延时连接时间
     */
    private long mReconnectTimeDelay =  5 * 1000;


    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean isHolden = mConnectionManager.getOption().isConnectionHolden();

            if (!isHolden) {
                detach();
                return;
            }
            ConnectionInfo info = mConnectionManager.getConnectionInfo();
            SL.i("Reconnect the server " + info.getIp() + ":" + info.getPort() + " ...");
            if (!mConnectionManager.isConnect()) {
                mConnectionManager.connect();
            }
        }
    };

    @Override
    public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
        if (isNeedReconnect(e)) {//break with exception
            reconnectDelay();
        } else {
            reset();
        }
    }

    @Override
    public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
        reset();
    }

    @Override
    public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
        if (e != null) {
            reconnectDelay();
        }
    }

    /**
     * 是否需要重连
     *
     * @param e
     * @return
     */
    private boolean isNeedReconnect(Exception e) {
        synchronized (mIgnoreDisconnectExceptionList) {
            if (e != null && !(e instanceof PurifyException)) {//break with exception
                Iterator<Class<? extends Exception>> it = mIgnoreDisconnectExceptionList.iterator();
                while (it.hasNext()) {
                    Class<? extends Exception> classException = it.next();
                    if (classException.isAssignableFrom(e.getClass())) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    private void reset() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private void reconnectDelay() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(0, mReconnectTimeDelay);
        SL.i("Reconnect after " + mReconnectTimeDelay + " mills ...");
    }

    @Override
    public void detach() {
        reset();
        super.detach();
    }
}
