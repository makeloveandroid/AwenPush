package com.example.mypush.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.awen.push.core.App.PushApp;
import com.awen.push.core.AwenPushManager;
import com.awen.push.core.utils.SL;
import com.example.mypush.listener.PushListener;
import com.facebook.stetho.Stetho;

public class App extends PushApp {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);


    }

}
