package com.bd.aidl_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.bd.aidl_server.IOnOperationListener;
import com.bd.aidl_server.IOperationManager;
import com.bd.aidl_server.Parameter;
import com.bd.aidl_server.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AIDLClient";

    private IOperationManager iOperationManager;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iOperationManager = IOperationManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iOperationManager = null;
        }
    };

    private IOnOperationListener.Stub operationListener = new IOnOperationListener.Stub() {
        @Override
        public void onOperationCompleted(Parameter result) throws RemoteException {
            Log.e(TAG, "通过监听回调计算出的结果：9*3=  " + result.getParam());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setClassName("com.bd.aidl_server", "com.bd.aidl_server.AIDLService");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        findViewById(R.id.btn_add).setOnClickListener(v -> {
            if (iOperationManager != null) {
                try {
                    Log.e(TAG, "5+4=  " + iOperationManager.add(5, 4));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_subtraction).setOnClickListener(v -> {
            if (iOperationManager != null) {
                try {
                    Parameter parameter = iOperationManager.subtraction(new Parameter(8), new Parameter(6));
                    Log.e(TAG, "8-6=  " + parameter.getParam());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_addUserIn).setOnClickListener(v -> {
            if (iOperationManager != null) {
                try {
                    User localUser = new User("张三", "1388888");
                    User user = iOperationManager.addUserIn(localUser);
                    Log.e(TAG, "addUserIn star");
                    Log.e(TAG, user.toString());
                    Log.e(TAG, "localUser=====" + localUser.toString());
                    Log.e(TAG, "addUserIn end");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.btn_addUserOut).setOnClickListener(v -> {
            if (iOperationManager != null) {
                try {
                    User localUser = new User("李四", "1366666");
                    User user = iOperationManager.addUserOut(localUser);
                    Log.e(TAG, "addUserOut star");
                    Log.e(TAG, user.toString());
                    Log.e(TAG, "localUser=====" + localUser.toString());
                    Log.e(TAG, "addUserOut end");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.btn_addUserInout).setOnClickListener(v -> {
            if (iOperationManager != null) {
                try {
                    User localUser = new User("王二麻", "1399999");
                    User user = iOperationManager.addUserInout(localUser);
                    Log.e(TAG, "addUserInout star");
                    Log.e(TAG, user.toString());
                    Log.e(TAG, "localUser=====" + localUser.toString());
                    Log.e(TAG, "addUserInout end");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_getUsers).setOnClickListener(v -> {
            if (iOperationManager != null) {
                try {
                    List<User> users = iOperationManager.getUsers();
                    Log.e(TAG, "getUsers star");
                    for (User user : users) {
                        Log.e(TAG, user.toString());
                    }
                    Log.e(TAG, "getUsers end");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_register).setOnClickListener(v -> {
            if (iOperationManager != null) {
                try {
                    iOperationManager.registerListener(operationListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_unregister).setOnClickListener(v -> {
            if (iOperationManager != null) {
                try {
                    iOperationManager.unregisterListener(operationListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_operation).setOnClickListener(v -> {
            if (iOperationManager != null) {
                try {
                    iOperationManager.operation(new Parameter(9), new Parameter(3));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null) {
            unbindService(connection);
        }
    }
}
