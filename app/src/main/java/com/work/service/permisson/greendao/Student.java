package com.work.service.permisson.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2017/6/1.greendao声明实体,声明Student后会生成一个相应的StudentDao，通过映射实现
 */

@Entity
public class Student {
    //@Id设置自动增长列
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int age;
    private String address;

    @Generated(hash = 445709271)
    public Student(Long id, String name, int age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @Generated(hash = 1556870573)
    public Student() {
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
