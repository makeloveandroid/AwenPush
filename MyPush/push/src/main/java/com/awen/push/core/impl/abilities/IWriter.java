package com.awen.push.core.impl.abilities;

import com.awen.push.core.sdk.OkSocketOptions;
import com.awen.push.core.sdk.bean.ISendable;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IWriter {
    boolean write() throws RuntimeException;

    void setOption(OkSocketOptions option);

    void offer(ISendable sendable);

    void close();

}
