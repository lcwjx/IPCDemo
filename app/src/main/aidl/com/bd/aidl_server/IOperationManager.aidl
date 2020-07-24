// IOperationManager.aidl
package com.bd.aidl_server;

import com.bd.aidl_server.Parameter;
import com.bd.aidl_server.User;
import com.bd.aidl_server.IOnOperationListener;

// Declare any non-default types here with import statements

interface IOperationManager {
    //基本数据类型
    int add(int p1,int p2);

    Parameter subtraction(in Parameter p1,in Parameter p2);

    List<User> getUsers();
    //测试 in out inout 三个定向tag
    User addUserIn(in User user);

    User addUserOut(out User user);

    User addUserInout(inout User user);


    void operation(in Parameter p1 , in Parameter p2);

    void registerListener(in IOnOperationListener listener);

    void unregisterListener(in IOnOperationListener listener);
}
