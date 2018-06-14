package com.awen.push.core.impl.abilities;

import com.awen.push.core.sdk.ConnectionInfo;
import com.awen.push.core.sdk.connection.IConnectionManager;

/**
 * Created by xuhao on 2017/6/30.
 */

public interface IConnectionSwitchListener {
    void onSwitchConnectionInfo(IConnectionManager manager, ConnectionInfo oldInfo, ConnectionInfo newInfo);
}
