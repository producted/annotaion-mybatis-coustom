package com.zhangpk.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
            sql = generateSql(method.getAnnotation(Insert.class).value(),
                    Insert.class.getSimpleName());
            sql = method.getAnnotation(Insert.class).value();
            if (sql != null) {
                insert(sql,parameters);
            }
        }
        //查询
        if (method.isAnnotationPresent(Select.class)) {
            sql = generateSql(method.getAnnotation(Select.class).value(),
                    Select.class.getSimpleName());
            if (sql != null) {
                Class<?> clazz = method.getReturnType();
                //看是否是返回List
                if (List.class.isAssignableFrom(clazz)) {
                    //既然多条记录，那么就该知其泛型
                    //有关泛型通过反射取泛型的练习 见generic包下
                    Type type = method.getGenericReturnType();
                    if (type instanceof ParameterizedType) {
                        //获取泛型
                        Class<?> outClass = (Class<?>)((ParameterizedType) (type)).getActualTypeArguments()[0];
                        return select(sql,parameters,outClass);
                    }

                }
            }
        }
        return null;
    }

    private static <T> List<T> select(String sql,Object[] parameters,Class<T> clazz) throws SQLException {
        Connection con = DbConnection.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        for (int i = 0;parameters!= null && i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        List<T> retList = new ResultSetMapper().mapResultSetToObject(resultSet, clazz);
        DbConnection.release(con);
        return retList;
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

    public static String generateSql(String sql,String simpleNameForAnnotation){
        String dealName = sql.split(" ")[0];
        if (dealName == null || !dealName.equalsIgnoreCase(simpleNameForAnnotation)) {
            return null;
        }
        return sql;
    }



}
