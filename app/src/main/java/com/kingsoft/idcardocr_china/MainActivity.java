package com.kingsoft.idcardocr_china;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.kingsoft.idcardocr_china.idcardocr.CameraActivity;

public class MainActivity extends AppCompatActivity {
    private static final int GETPERMISSION_SUCCESS = 1;//获取权限成功
    private static final int GETPERMISSION_FAILER = 2;//获取权限失败
    private TextView tv_id;
    private int MY_SCAN_REQUEST_CODE = 100;
    private Context mContext;
    private MyHandler myHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        tv_id = (TextView) findViewById(R.id.tv_id);
        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getPermissions();
                requestAllPermission();
            }
        });
    }

    private void requestAllPermission() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(MainActivity.this,
                new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        myHandler.sendEmptyMessage(GETPERMISSION_SUCCESS);
                    }

                    @Override
                    public void onDenied(String permission) {
                        myHandler.sendEmptyMessage(GETPERMISSION_FAILER);
                    }
                });
    }

    //因为权限管理类无法监听系统，所以需要重写onRequestPermissionResult方法，更新权限管理类，并回调结果。这个是必须要有的。
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_SCAN_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String id = data.getStringExtra("id");
            Toast.makeText(this, id, Toast.LENGTH_LONG).show();
            if (id != null && id.length() == 18) {
                tv_id.setText(id);
            }
        }
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETPERMISSION_SUCCESS:
                    Intent scanIntent = new Intent(mContext, CameraActivity.class);
                    startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
                    break;
                case GETPERMISSION_FAILER:
                    Toast.makeText(mContext, "此功能须获摄像头权限1111111", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
