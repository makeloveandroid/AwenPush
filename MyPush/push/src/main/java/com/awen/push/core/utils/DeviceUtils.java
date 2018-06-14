package com.awen.push.core.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.awen.push.core.entity.Constant;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.UUID;

public class DeviceUtils {

    /***
     * 获取设备的唯一识别码,果没有获取到如随机生成识别码并且保存到app当中
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getTokennId() {
        String UID = SPUtils.getInstance().getString(Constant.PUSH_TOKE, "");
        if (TextUtils.isEmpty(UID)) {
            String uid = UUID.randomUUID().toString();
            uid = EncryptUtils.encryptMD5ToString(uid);
            SPUtils.getInstance().put(Constant.PUSH_TOKE, uid);
            return uid;
        } else {
            return UID;
        }
    }
}
