package com.awen.push.core.entity;

import com.awen.push.core.App.PushApp;
import com.awen.push.core.sdk.bean.IPulseSendable;
import com.awen.push.core.utils.DeviceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class TokenData implements IPulseSendable {

    private String str;

    public TokenData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", DeviceUtils.getTokennId());
            jsonObject.put("type", 2);
            str = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] parse() {
        byte[] body = str.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(body.length + 8);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(1001);// 这个type在服务端用的 现在没啥用处
        bb.putInt(body.length);
        bb.put(body);
        byte[] array = bb.array();
        return array;
    }
}
