package com.awen.push.core.impl.abilities;

import com.awen.push.core.sdk.OkSocketOptions;
import com.awen.push.core.sdk.bean.ISendable;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IIOManager {
    void resolve();

    void setOkOptions(OkSocketOptions options);

    void send(ISendable sendable);

    void close();

    void close(Exception e);

}
