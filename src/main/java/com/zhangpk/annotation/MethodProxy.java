package com.zhangpk.annotation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created By zhangpk On 2019/4/13
 **/

public class MethodProxy implements InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] parameters)
            throws Throwable {
        return DaoOpretorFactory.handle(method,parameters);
    }
    //是方法动态代理
}
