// IOnOperationListener.aidl
package com.bd.aidl_server;

import com.bd.aidl_server.Parameter;

interface IOnOperationListener {

    void onOperationCompleted(in Parameter result);
}
