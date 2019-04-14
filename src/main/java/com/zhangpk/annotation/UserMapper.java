package com.zhangpk.annotation;

import java.util.List;

/**
 * Created By zhangpk On 2019/4/13
 **/
public interface UserMapper {

    @Insert("insert into user (name,age) values(?,?)")
    public void addUser(String name,int age);

    @Select("select * from user")
    public List<User> queryUser();
}

