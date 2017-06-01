package com.work.service.permisson;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.work.service.permisson.greendao.GreenDaoUtils;
import com.work.service.permisson.greendao.Student;
import com.work.service.permisson.greendao.StudentDao;
import com.work.service.permisson.greendao.Subject;
import com.work.service.permisson.greendao.SubjectDao;
import com.work.service.permisson.helper.PermissionAllow;
import com.work.service.permisson.helper.PermissionDeny;
import com.work.service.permisson.helper.PermissionStub;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private int requestCode = 1000;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //执行
        init();
        insertByGroup();
        query();
        excuteGreenDao();
    }

    /**
     * greendao--3.0之前需要新建GreenDaoGenerator工程定义Dao对象，3.0以后无需此操作
     **/
    private void excuteGreenDao() {
        //获取生成的Student映射
        StudentDao studentdao = GreenDaoUtils.getInstance().getGreenDaoSession(this).getStudentDao();
        Student student = new Student(null, "dylan", 18, "sichuan");
        //插入实体类对应的数据及字段
        studentdao.insert(student);
    }

    /**
     * 查询
     **/

    private void query() {
        SubjectDao subjectDao = GreenDaoUtils.getInstance().getGreenDaoSession(this).getSubjectDao();
        /**查询所有subject列表信息**/
        List<Subject> list = subjectDao.loadAll();
        for (Subject sub : list) {
            Log.e("dylan", "查询结果是:" + sub.getSubjectName());
        }

    }


    /**
     * 构建数据源
     **/

    private List initData() {
        List<Subject> list = new ArrayList<>();
        list.add(new Subject(null, "math"));
        list.add(new Subject(null, "che"));
        list.add(new Subject(null, "eng"));
        list.add(new Subject(null, "phy"));
        return list;
    }

    /**
     * 批量插入
     **/
    private void insertByGroup() {
        SubjectDao subjectDao = GreenDaoUtils.getInstance().getGreenDaoSession(this).getSubjectDao();
        List<Subject> list = initData();
        for (Subject sub : list) {
            subjectDao.insert(sub);
            Log.e("dylan", "insert.....");
        }

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
     * @param
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
