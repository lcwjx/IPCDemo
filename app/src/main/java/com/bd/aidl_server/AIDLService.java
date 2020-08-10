package com.bd.aidl_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;

public class AIDLService extends Service {

                      private List<User> users = new ArrayList<>();

    private static final String TAG = "AIDLService";
    /**
     * RemoteCallbackList 是系统专门提供的用于删除跨进程 listener 的类，
     * RemoteCallbackList 是一个泛型，支持管理任意的 AIDL 接口，从它的声明可以看出，因为所有的 AIDL 接口都继承自 IInteface 接口
     */
    private RemoteCallbackList<IOnOperationListener> callbackList;

    private IOperationManager.Stub stub = new IOperationManager.Stub() {
        @Override
        public int add(int p1, int p2) throws RemoteException {
            return p1 + p2;
        }

        @Override
        public Parameter subtraction(Parameter p1, Parameter p2) throws RemoteException {
            return new Parameter(p1.getParam() - p2.getParam());
        }

        @Override
        public List<User> getUsers() throws RemoteException {
            return users;
        }

        @Override
        public User addUserIn(User user) throws RemoteException {
            if (users == null) {
                users = new ArrayList<>();
            }
            if (user == null) {
                Log.e(TAG, "addUserIn params is null");
                user = new User();
            }
            //尝试修改user的参数，主要是为了观察其到客户端的反馈
            user.setName("测试 addUserIn");

            if (!users.contains(user)) {
                users.add(user);
            }
            return user;
        }

        @Override
        public User addUserOut(User user) throws RemoteException {
            if (users == null) {
                users = new ArrayList<>();
            }
            if (user == null) {
                Log.e(TAG, "addUserOut params is null");
                user = new User();
            }
            //尝试修改user的参数，主要是为了观察其到客户端的反馈
            user.setName("测试 addUserOut");

            if (!users.contains(user)) {
                users.add(user);
            }
            return user;
        }

        @Override
        public User addUserInout(User user) throws RemoteException {
            if (users == null) {
                users = new ArrayList<>();
            }
            if (user == null) {
                Log.e(TAG, "addUserInout params is null");
                user = new User();
            }
            //尝试修改user的参数，主要是为了观察其到客户端的反馈
            user.setName("测试 addUserInout");

            if (!users.contains(user)) {
                users.add(user);
            }
            return user;
        }

        @Override
        public void operation(Parameter p1, Parameter p2) throws RemoteException {
            try {
                Log.e(TAG, "operation 被调用，延时3秒，模拟耗时计算");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int param1 = p1.getParam();
            int param2 = p2.getParam();
            Parameter result = new Parameter(param1 * param2);
            //在操作 RemoteCallbackList 前，必须先调用其 beginBroadcast 方法
            //此外，beginBroadcast 必须和 finishBroadcast配套使用
            int count = callbackList.beginBroadcast();
            for (int i = 0; i < count; i++) {
                IOnOperationListener listener = callbackList.getBroadcastItem(i);
                if (listener != null) {
                    listener.onOperationCompleted(result);
                }
            }

            callbackList.finishBroadcast();
            Log.e(TAG, "计算结束");
        }

        @Override
        public void registerListener(final IOnOperationListener listener) throws RemoteException {
            listener.asBinder().linkToDeath(new DeathRecipient() {
                @Override
                public void binderDied() {
                    callbackList.unregister(listener);
                }
            }, 0);

            callbackList.register(listener);
            Log.e(TAG, "registerListener 注册回调成功");
        }

        @Override
        public void unregisterListener(IOnOperationListener listener) throws RemoteException {
            callbackList.unregister(listener);
            Log.e(TAG, "unregisterListener 解绑回调成功");
        }
    };

    public AIDLService() {
        callbackList = new RemoteCallbackList<>();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        stub.linkToDeath(new IBinder.DeathRecipient() {
            @Override
            public void binderDied() {

            }
        }, 0);
        return stub;
    }


}
