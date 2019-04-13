package com.zhangpk.annotation;

import java.lang.reflect.Proxy;

/**
 * Created By zhangpk On 2019/4/13
 **/
// 动态代理工厂
public class MehtodProxyFactory {

    public static <T> T getBean(Class<T> clazz){
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[] {clazz},
                new MethodProxy()
        );
    }
}
