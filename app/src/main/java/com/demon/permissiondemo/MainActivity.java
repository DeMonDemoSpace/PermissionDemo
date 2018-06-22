package com.demon.permissiondemo;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.demon.permissiondemo.permission.PermissionHelper;
import com.demon.permissiondemo.permission.PermissionInterface;
import com.demon.permissiondemo.permission.PermissionUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements PermissionInterface {
    private static final String TAG = "MainActivity";
    //要申请的权限
    private String[] mPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionHelper = new PermissionHelper(this, this);
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionHelper.requestPermissions();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
                rxPermissions.requestEach(mPermissions).subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        Log.i(TAG, "accept: " + permission.toString());
                        if (permission.granted) {
                            //权限获取成功
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            //权限获取失败，但是没有永久拒绝
                        } else {
                            //权限获取失败，而且被永久拒绝
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getPermissionsRequestCode() {
        return 0;
    }

    @Override
    public String[] getPermissions() {
        return mPermissions;
    }

    @Override
    public void requestPermissionsSuccess() {
        //do something
    }

    @Override
    public void requestPermissionsFail() {
        mPermissions = PermissionUtil.getDeniedPermissions(this, mPermissions);
        PermissionUtil.PermissionDialog(this, PermissionUtil.permissionText(mPermissions) + "请在应用权限管理进行设置！");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] mPermissions, int[] grantResults) {
        if (permissionHelper.requestPermissionsResult(requestCode, mPermissions, grantResults)) {
            //权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, mPermissions, grantResults);
    }
}
