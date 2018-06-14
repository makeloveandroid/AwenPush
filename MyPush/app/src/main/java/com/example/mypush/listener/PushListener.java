package com.example.mypush.listener;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.awen.push.core.entity.PushEntity;
import com.awen.push.core.listener.OnPushListener;
import com.example.mypush.app.App;
import com.example.mypush.util.NotificationUtils;

public class PushListener implements OnPushListener {
    @Override
    public void onPushStart() {
        Log.d("wyz", "推送服务器连接成功!");
        Toast.makeText(App.getApp(), "推送服务器连接成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void receivePush(PushEntity entity) {
        if (entity.type==PushEntity.NOTIFICATION){
            Log.d("wyz", "notification推送消息");
            NotificationUtils.makeStatusNotification(entity.type, new String(Base64.decode(entity.baseData, Base64.NO_WRAP)), App.getApp());
        }else if (entity.type==PushEntity.TOAST) {
            Log.d("wyz", "toast推送消息");
            Toast.makeText(App.getApp(), "推送Type:" + entity.type + " " + new String(Base64.decode(entity.baseData, Base64.NO_WRAP)), Toast.LENGTH_SHORT).show();
        }else if (entity.type==PushEntity.IMAGE) {
            Log.d("wyz", "图片推送");
        }
    }
}
