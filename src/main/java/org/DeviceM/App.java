package org.DeviceM;

import org.DeviceM.dao.Account;
import org.DeviceM.mapper.FunctionMapper;
import org.DeviceM.swing.frame.LoginFrame;
import org.DeviceM.util.Transaction;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App  {
    public static SqlSessionFactory sessionFactory;

    public static void main( String[] args ) throws IOException {
        connectDatabase();
        System.out.println("Database connected.");
        LoginFrame loginFrame = new LoginFrame();
    }

    public static void connectDatabase() throws IOException {
        String resource = "mybatis-conf.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }
}
