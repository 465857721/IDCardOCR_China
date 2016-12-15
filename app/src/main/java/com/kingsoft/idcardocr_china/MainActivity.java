package com.kingsoft.idcardocr_china;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kingsoft.idcardocr_china.idcardocr.CameraActivity;

public class MainActivity extends AppCompatActivity {
    private TextView tv_id;

    private int MY_SCAN_REQUEST_CODE = 100;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        tv_id = (TextView) findViewById(R.id.tv_id);
        findViewById(R.id.btn_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},
                            1);
                } else {
                    Intent scanIntent = new Intent(mContext, CameraActivity.class);
                    startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent scanIntent = new Intent(this, CameraActivity.class);
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
            } else {
                // Permission Denied
                Toast.makeText(this, "此功能须获摄像头权限", Toast.LENGTH_LONG).show();
//                Tools.toastInBottom("");
            }
        }
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
}
