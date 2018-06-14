package com.awen.push.core.listener;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.awen.push.core.App.PushApp;
import com.awen.push.core.bus.RxBus;
import com.awen.push.core.dao.PushEntityDao;
import com.awen.push.core.entity.AcceptSuccessData;
import com.awen.push.core.entity.PulseData;
import com.awen.push.core.entity.PushEntity;
import com.awen.push.core.entity.TokenData;
import com.awen.push.core.impl.PulseManager;
import com.awen.push.core.sdk.ConnectionInfo;
import com.awen.push.core.sdk.OkSocket;
import com.awen.push.core.sdk.OkSocketOptions;
import com.awen.push.core.sdk.bean.IPulseSendable;
import com.awen.push.core.sdk.bean.ISendable;
import com.awen.push.core.sdk.bean.OriginalData;
import com.awen.push.core.sdk.connection.IConnectionManager;
import com.awen.push.core.sdk.connection.interfacies.ISocketActionListener;
import com.awen.push.core.utils.SL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.Charset;
import java.util.Arrays;

public class OnSocketListener implements ISocketActionListener {

    private final OnPushListener listener;

    public OnSocketListener(OnPushListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSocketIOThreadStart(Context context, String action) {

    }

    @Override
    public void onSocketIOThreadShutdown(Context context, String action, Exception e) {

    }

    @Override
    public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
        SL.e("收到数据");
        byte[] bodyBytes = data.getBodyBytes();
        byte[] headBytes = data.getHeadBytes();
        String dataJson = new String(bodyBytes);
        PushEntity pushEntity = new GsonBuilder().create().fromJson(dataJson, PushEntity.class);
        if (pushEntity.type == PushEntity.PULSE) {
            //代表的是心跳数据
            IConnectionManager connectionManager = OkSocket.open(info);
            if (connectionManager != null) {
                // 喂狗心跳置0
                SL.e("心跳喂狗完成");
                connectionManager.getPulseManager().feed();
            }
        } else {
            // 判断是否收到过这条推送消息
            PushEntity unique = PushApp.getDb().getPushEntityDao()
                    .queryBuilder()
                    .where(PushEntityDao.Properties.Id.eq(pushEntity.id))
                    .unique();
            if (unique == null) {
                if (listener != null) {
                    listener.receivePush(pushEntity);
                }
            }
            // 收到消息通知服务的 收推送消息完成
            RxBus.getInstance().post(new AcceptSuccessData(pushEntity.id));

            // 推送消息入库
            PushApp.getDb().getPushEntityDao().insertOrReplace(pushEntity);
        }
    }

    @Override
    public void onSocketWriteResponse(Context context, ConnectionInfo info, String action, ISendable data) {
        SL.e("写数据");
    }

    @Override
    public void onPulseSend(Context context, ConnectionInfo info, IPulseSendable data) {
        SL.e("心跳");

    }

    @Override
    public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
        SL.e("连接断开");
    }

    @Override
    public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
        SL.e("连接成功");
        if (listener != null) {
            listener.onPushStart();
        }
        IConnectionManager connectionManager = OkSocket.open(info);
        if (connectionManager != null) {
            //发送token设备数据
            connectionManager.send(new TokenData());


            OkSocketOptions option = connectionManager.getOption();
            boolean pulse = option.isPulse();
            if (pulse) {
                //启动心跳
                PulseData pulseData = new PulseData();
                PulseManager pulseManager = connectionManager.getPulseManager();
                pulseManager.setPulseSendable(pulseData);
                pulseManager.pulse();
            }
        }
    }

    @Override
    public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
        SL.e("连接结束");
    }
}
