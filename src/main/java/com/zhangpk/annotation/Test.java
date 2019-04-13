package com.zhangpk.annotation;

/**
 * Created By zhangpk On 2019/4/13
 **/
public class Test {
    public static void main(String[] args) {
        UserMapper userMapper = MehtodProxyFactory.getBean(UserMapper.class);
        userMapper.addUser("zhangpk", 18);
    }
}
