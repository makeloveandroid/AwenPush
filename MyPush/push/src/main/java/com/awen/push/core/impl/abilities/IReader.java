package com.awen.push.core.impl.abilities;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.awen.push.core.sdk.OkSocketOptions;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IReader {

    @WorkerThread
    void read() throws RuntimeException;

    @MainThread
    void setOption(OkSocketOptions option);

    void close();
}
