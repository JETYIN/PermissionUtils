package com.work.service.permisson.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/6/1.
 */

public class GreenDaoUtils {
    //封装greendao调用方法

    private static GreenDaoUtils instance;
    /**
     * 映射被注解的实体类到greendao
     **/
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase sqLiteDatabase;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private String DB_NAME = "dylan";

    private GreenDaoUtils() {

    }

    public static GreenDaoUtils getInstance() {
        if (null == instance) {
            synchronized (GreenDaoUtils.class) {
                if (null == instance) {
                    instance = new GreenDaoUtils();
                }
            }
        }
        return instance;
    }


    private void initGreenDao(Context context) {
        mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        sqLiteDatabase = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(sqLiteDatabase);

        daoSession = daoMaster.newSession();
    }

    /**获取DaoSession,Student实体类映射到此类，通过getStudentDao获取对应实体**/
    public DaoSession getGreenDaoSession(Context context) {
        if (daoSession == null) {
            initGreenDao(context);
        }
        return daoSession;
    }

    /**获取SQLiteDatabase**/
    public SQLiteDatabase getGreenDaoSessionDB(Context context) {
        if (null == sqLiteDatabase) {
            initGreenDao(context);
        }
        return sqLiteDatabase;
    }
}
