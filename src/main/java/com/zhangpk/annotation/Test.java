package com.zhangpk.annotation;

import java.util.List;

/**
 * Created By zhangpk On 2019/4/13
 **/
public class Test {
    public static void main(String[] args) {
        UserMapper userMapper = MehtodProxyFactory.getBean(UserMapper.class);
//        userMapper.addUser("zhangpk", 18);
        List<User> users = userMapper.queryUser();
        System.out.println(users);
    }
}
