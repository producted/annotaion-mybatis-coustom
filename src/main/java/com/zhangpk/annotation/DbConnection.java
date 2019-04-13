package com.zhangpk.annotation;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created By zhangpk On 2019/4/13
 **/
public class DbConnection {

    private static DataSource db = null;

    static {

        try {
            //类装载db.properties文件
            InputStream in = DbConnection.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(in);


            //创建db数据源
            db = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
            //抛出类初始化错误
            throw new ExceptionInInitializerError();
        }
    }

    public static Connection getConnection() throws SQLException {
        return db.getConnection();
    }

    public static void release(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }

}
