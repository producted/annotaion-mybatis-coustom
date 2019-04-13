package com.zhangpk.annotation;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * TODO 可以用工厂模式把代码抽出  在那之前暂时放在这里吧
 * Created By zhangpk On 2019/4/13
 **/
public class DaoOpretorFactory {

    //因为动态代理 我们的parameter就是方法内的参数 感叹代理的神奇
    public static Object handle(Method method,Object[] parameters) throws SQLException {
        String sql = null;
        //新增操作
        if (method.isAnnotationPresent(Insert.class)) {
            sql = method.getAnnotation(Insert.class).value();
            String simpleName = Insert.class.getSimpleName();
            String dealName = sql.split(" ")[0];
            if (dealName == null || !dealName.equalsIgnoreCase(simpleName)) {
                return null;
            }
            insert(sql,parameters);
        }
        return null;
    }

    private static void insert(String sql, Object[] parameters) throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; parameters != null && i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
        statement.execute();
        DbConnection.release(connection);
    }

}
