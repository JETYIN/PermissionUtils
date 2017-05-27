package com.work.service.permisson.helper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26.开发中可以将权限相关需求写在baseActivity中
 */

public class PermissionStub {

    /**
     * 权限检查类
     **/
    private static PermissionStub instance;
    private static String TAG = "PermissionStub";
    private static Object mObject;

    private static  int REQUEST_CODE;
    private static String[] permissonStr;


    private PermissionStub(Object object) {
        if (!checkObject(object)) {
            throw new IllegalArgumentException("类型错误");
        }
        this.mObject = object;

    }

    public static PermissionStub getInstance(Object object) {

        if (instance == null) {
            synchronized (PermissionStub.class) {
                if (instance == null) {
                    instance = new PermissionStub(object);
                }
            }
        }
        return instance;
    }

    /**
     * 留于外部调用的-传入参数列表
     **/
    public PermissionStub request(int code, String... params) {
        this.REQUEST_CODE = code;
        this.permissonStr = params;
        return this;

    }

    /**
     * 真正的请求权限方法,在Fragment中请求，需要调用Fragment的请求方法，否则会回调到Fragment中
     **/

    private void requestPermisson() {
        if (mObject instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) mObject, getDenyPermissonList(), REQUEST_CODE);
        }
        if (mObject instanceof Fragment) {
            ((Fragment) mObject).requestPermissions(getDenyPermissonList(), REQUEST_CODE);
        }
        if (mObject instanceof android.app.Fragment) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((android.app.Fragment) mObject).requestPermissions(getDenyPermissonList(), REQUEST_CODE);
            }


        }

    }

    public String[] getDenyPermissonList() {

        List<String> denyList = new ArrayList<>();
        for (String permisson : permissonStr) {

            if (!isGranted(permisson)) {
                denyList.add(permisson);
                Log.e(TAG, "****被拒绝权限:" +permisson );
            }
        }
        return denyList.toArray(new String[denyList.size()]);
    }

    /**
     * 执行调用申请-或是检查权限
     **/
    public void excute() {
        //检验是否是android N
        boolean IS_GRANTED = true;
        if (!checkSysVersion()) {
            Log.e(TAG, "****当前版本不是android N");
            recycle();
            return;
        }


        if (getDenyPermissonList().length > 0) {
            requestPermisson();
        } else {
            PermissionUtils.permissionGranted(mObject, REQUEST_CODE);
        }

    }

    //清出强引用
    public static void recycle() {
        mObject = null;
        permissonStr = null;
        REQUEST_CODE = -1;

    }

    private boolean isGranted(String permisson) {
        return ActivityCompat.checkSelfPermission(getActivity(), permisson) == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * 申请结果返回
     **/
    public static void onRequestPermissionsResult(Object object, int requestCode, String[] permissions, int[] grantResults) {
        showLog(permissions, grantResults);

        boolean isGranted = true;
        if (!checkObject(object)) {

            throw new IllegalArgumentException("类型错误，Object类型为Activity，Fragment");
        }

        //检验授权返回参数列表
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                return;
            }
        }

        if (isGranted) {
            //分发授权结果
            PermissionUtils.permissionGranted(mObject, REQUEST_CODE);

        } else {
            PermissionUtils.permissionDeny(mObject, REQUEST_CODE);
            //进入权限设置
            enterPermissionSetting();
        }

    }

    private static void showLog(String[] permissions, int[] grantResults) {
        for (String per : permissions) {
            Log.e(TAG, "****遍历返回permissions是:" + per);
        }
        for (int i = 0; i < grantResults.length; i++) {
            Log.e(TAG, "****grantResults:" + grantResults[i]);
        }
    }

    private static Activity getActivity() {
        if (mObject instanceof Activity) {
            return (Activity) mObject;
        }
        if (mObject instanceof Fragment) {
            return ((Fragment) mObject).getActivity();
        }
        if (mObject instanceof android.app.Fragment) {
            return ((android.app.Fragment) mObject).getActivity();
        }
        Log.e(TAG, "----object转换失败");
        return null;
    }

    /**
     * 检查当前api等级
     **/
    private boolean checkSysVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查类型-是否是对应的实例
     **/
    private static boolean checkObject(Object object) {
        if (object instanceof Activity || object instanceof android.app.Fragment || object instanceof android.app.Fragment) {
            return true;
        }
        return false;
    }

    /**
     * 进入权限设置
     **/

    public static void enterPermissionSetting() {

        new AlertDialog.Builder(getActivity()).setTitle("提示信息").setMessage("当前缺少程序运行必须权限，是否前往设置权限").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //进入设置页面
                setting();
            }
        });
    }

    private static void setting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        getActivity().startActivity(intent);


    }

}
