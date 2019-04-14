package com.zhangpk.annotation;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created By zhangpk On 2019/4/14
 **/
//映射result到指定类型
public class ResultSetMapper {

    public <T> List<T> mapResultSetToObject(ResultSet resultSet,Class<T> clazz){
        List<T> outList =null;
        try {

            if (resultSet != null) {
                //getMetaData() 获取有关resultSet中的列的名称和类型信息
                ResultSetMetaData rsmd = resultSet.getMetaData();

                while (resultSet.next()) {
                    T thisBean = clazz.newInstance();
                    //自身打印看看其count值
                    System.out.println(rsmd.getColumnCount());
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        //取得列名
                        String columnName = rsmd.getColumnName(i + 1);
                        //获取列的值 -注意取值通过resultset来取
                        //可以得出 其结构类型K V结构
                        Object columValue = resultSet.getObject(i + 1);

                        Field field = clazz.getDeclaredField(columnName);
                        if (field != null && columValue != null) {
                            System.out.println("field.getName 值： " + field.getName());
                            System.out.println("cloumnName 值： " + columnName);
                            BeanUtils.setProperty(thisBean,field.getName(),columValue);
                        }
                    }
                    if (outList == null) {
                        outList = new ArrayList<T>();
                    }
                    outList.add(thisBean);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (outList == null) {
            //返回空集合 而不是null
            return Collections.emptyList();
        }
        return outList;
    }
}
