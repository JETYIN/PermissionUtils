package com.work.service.permisson;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.work.service.permisson.helper.PermissionAllow;
import com.work.service.permisson.helper.PermissionDeny;
import com.work.service.permisson.helper.PermissionStub;

public class MainActivity extends Activity {

    private int requestCode = 1000;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //执行
        init();
    }

    private void init() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionStub.getInstance(MainActivity.this).request(requestCode, permissionStr()).excute();
            }
        });
    }


    private String[] permissionStr() {
        return new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_SETTINGS};
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionStub.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * @param 传入请求参数
     **/
    @PermissionAllow(1000)
    public void allow() {

        Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT);
    }

    @PermissionDeny(1000)
    public void deny() {
        Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT);
    }
}
