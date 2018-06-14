package com.awen.push.core.App;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.awen.push.core.AwenPushManager;
import com.awen.push.core.dao.DaoMaster;
import com.awen.push.core.dao.DaoSession;

public class PushApp extends Application {
    private static DaoSession pushDao;
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        initDao();
        application = this;
    }

    public static final Application getApp() {
        return application;
    }


    private void initDao() {
        DaoMaster.DevOpenHelper myDb = new DaoMaster.DevOpenHelper(this, "awen_push_db", null);
        SQLiteDatabase writableDatabase = myDb.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        pushDao = daoMaster.newSession();
    }

    public static final DaoSession getDb() {
        return pushDao;
    }
}
