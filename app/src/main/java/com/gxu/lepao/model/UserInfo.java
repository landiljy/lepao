package com.gxu.lepao.model;

import org.litepal.crud.DataSupport;

/**
 * Created by ljy on 2017-05-20.
 * 用户信息表
 */

public class UserInfo extends DataSupport{

    //记录编号,主键
    private int id;

    //电话号码
    private  String phone;

    //登录密码
    private String password;

    //昵称
    private String name;

    //性别
    private String sex;

    //年龄
    private int age;

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
