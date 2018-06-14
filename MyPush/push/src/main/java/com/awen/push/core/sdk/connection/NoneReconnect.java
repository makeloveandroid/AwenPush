package com.awen.push.core.sdk.connection;

import android.content.Context;

import com.awen.push.core.sdk.ConnectionInfo;

/**
 * 不进行重新连接的重连管理器
 * Created by Tony on 2017/10/24.
 */
public class NoneReconnect extends AbsReconnectionManager {
    @Override
    public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {

    }

    @Override
    public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {

    }

    @Override
    public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {

    }
}
