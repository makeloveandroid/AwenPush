package com.example.mypush;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.awen.push.core.AwenPushManager;
import com.awen.push.core.dao.PushEntityDao;
import com.awen.push.core.entity.PushEntity;
import com.awen.push.core.listener.OnPushListener;
import com.example.mypush.app.App;
import com.example.mypush.listener.PushListener;
import com.example.mypush.util.NotificationUtils;

import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity implements OnPushListener {

    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.iv_img);


    }

    public void startPush(View view) {
        AwenPushManager pushManager = new AwenPushManager.Build()
                // TODO: 2018/6/12 注意这里,只是为了效果方便,卸载activity里面,这样写会导致activity内存泄露,最好还是在服务中开启
                .setOnPushListener(this) // 设置推送回调
                .settingSerice("172.18.0.159", 8800)
                .build(getApplication());
        // 开启接收推送
        pushManager.startPush();
    }


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

            byte[] imageByte = Base64.decode(entity.baseData, Base64.NO_WRAP);
            Bitmap bitmap = Bytes2Bimap(imageByte);
            img.setImageBitmap(bitmap);
        }
    }

    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public void clearPush(View view) {
        PushEntityDao pushEntityDao = App.getDb().getPushEntityDao();
        pushEntityDao.deleteAll();
    }
}
