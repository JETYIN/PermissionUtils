package com.work.service.permisson.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.graphics.drawable.animated.BuildConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/5/26.
 */

public class PermissionUtils {

    private String TAG = getClass().getSimpleName();


    public static void permissionGranted(Object object, int requestCode) {
//获取方法
        Method method[] = object.getClass().getDeclaredMethods();
        for (Method methodItem : method) {
            if (methodItem.isAnnotationPresent(PermissionAllow.class)) {
//获取反射对象
                PermissionAllow permissionAllow = methodItem.getAnnotation(PermissionAllow.class);
                //获取反射值
                int code = permissionAllow.value();

                if (requestCode == code) {
                    methodItem.setAccessible(true);
                    try {
                        methodItem.invoke(object, new Object[]{});
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }

    public static void permissionDeny(Object object, int requestCode) {
        Method method[] = object.getClass().getDeclaredMethods();

        for (Method mothodItem : method) {
            if (mothodItem.isAnnotationPresent(PermissionDeny.class)) {
                int value = mothodItem.getAnnotation(PermissionDeny.class).value();

                if (requestCode == value) {
                    mothodItem.setAccessible(true);
                    try {
                        mothodItem.invoke(object, new Object[]{});
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }

            }

        }

    }


    public static void startPermissionEditAct(Activity activity) {
        gotoMiuiPermission(activity);
    }


    /**
     * 跳转到miui的权限管理页面
     */
    private static void gotoMiuiPermission(Activity activity) {
        Intent i = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        i.setComponent(componentName);
        i.putExtra("extra_pkgname", activity.getPackageName());
        try {
            activity.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            gotoMeizuPermission(activity);
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private static void gotoMeizuPermission(Activity activity) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            gotoHuaweiPermission(activity);
        }
    }

    /**
     * 华为的权限管理页面
     */
    private static void gotoHuaweiPermission(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);

            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            activity.startActivity(getAppDetailSettingIntent(activity));
        }
    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        return localIntent;
    }

}
