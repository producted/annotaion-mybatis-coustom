package com.zhangpk.annotation;

/**
 * Created By zhangpk On 2019/4/13
 **/
public interface UserMapper {

    @Insert("insert into user (name,age) values(?,?)")
    public void addUser(String name,int age);
}

