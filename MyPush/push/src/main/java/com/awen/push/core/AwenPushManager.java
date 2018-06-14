package com.awen.push.core;

import android.app.Application;
import android.util.Log;

import com.awen.push.core.bus.RxBus;
import com.awen.push.core.entity.AcceptSuccessData;
import com.awen.push.core.impl.PulseManager;
import com.awen.push.core.listener.OnPushListener;
import com.awen.push.core.listener.OnSocketListener;
import com.awen.push.core.sdk.ConnectionInfo;
import com.awen.push.core.sdk.OkSocket;
import com.awen.push.core.sdk.OkSocketOptions;
import com.awen.push.core.sdk.connection.IConnectionManager;
import com.awen.push.core.sdk.connection.PushReconnectManager;

import java.nio.ByteOrder;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public final class AwenPushManager {
    // 推送只能推送一次
    private boolean flag = false;


    private  IConnectionManager manager;

    private AwenPushManager(ConnectionInfo info, OkSocketOptions option, OnPushListener listener){
        //调用OkSocket,开启这次连接的通道,拿到通道Manager
        manager = OkSocket.open(info);
        manager.option(option);
        // 设置后台无限存货
        OkSocket.setBackgroundSurvivalTime(-1);

        // 设置回调
        OnSocketListener onSocketListener = new OnSocketListener(listener);

        manager.registerReceiver(onSocketListener);
    }

    /***
     * 开始推送
     * @return
     */
    public void startPush() {
        if (!flag) {
            flag = true;
            if (!manager.isConnect()){
                manager.connect();
            }
            //注册消息
            registerBus();
        }
    }

    private void registerBus() {
        // 注册发送数据事件
        RxBus.getInstance().toObservable(AcceptSuccessData.class)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<AcceptSuccessData>() {
            @Override
            public void accept(AcceptSuccessData eventMsg) throws Exception {
                if (eventMsg != null) {
                    if (manager.isConnect()){
                        Log.d("wyz", "收到消息通知服务:" + eventMsg.getStr());
                        manager.send(eventMsg);
                    }
                }
            }
        });
    }

    public static class Build {
        // 连接服务器地址
        private ConnectionInfo info;

        //连接配置
        private OkSocketOptions.Builder optionBuild = new OkSocketOptions.Builder();
        private OnPushListener listener;

        public Build settingSerice(String ip, int port) {
            info = new ConnectionInfo(ip, port);
            return this;
        }


        public Build setOnPushListener(OnPushListener listener) {
            this.listener = listener;
            return this;
        }

        public AwenPushManager build(Application app) {
            OkSocket.initialize(app,true);
            // 默认配置
            OkSocketOptions option = optionBuild
                    .setReadByteOrder(ByteOrder.LITTLE_ENDIAN)
                    // 写数据缓存大小
                    .setWritePackageBytes(1024 * 20)
                    // 读数据缓存大小
                    .setReadPackageBytes(1024 * 20)
                    // 允许心跳丢失次数
                    .setPulseFeedLoseTimes(5)
                    // 心跳间隔
                    .setPulseFrequency(5000)
                    // 开启心跳
                    .isPulse(true)
                    // 断开无限重连机制
                    .setReconnectionManager(new PushReconnectManager()) //设置服务器断开重新连接机制
                    .build();

            AwenPushManager awenPushManager = new AwenPushManager(info, option,listener);
            return awenPushManager;
        }
    }
}
