package com.awen.push.core.listener;

import com.awen.push.core.entity.PulseData;
import com.awen.push.core.entity.PushEntity;

public interface OnPushListener {
    /***
     * 开始接收push,推送
     */
     void onPushStart();

    /***
     * 收到推送消息
     */
    void receivePush(PushEntity entity);

}
