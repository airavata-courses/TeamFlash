package com.teamFlash.microservices.registry.forecast;

import com.teamFlash.microservices.registry.aurora.utils.Constants;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by girish on 12/11/16.
 */
public class DBConnection {
    static String url;
    static String db ;
    static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static String DB_URL = null;
    static String user ;
    static String password ;

    static Connection connection=null;


    public static void initializeFields()
    {
        Properties properties = new Properties();
        //InputStream input = null;

        try {
            //properties.load(new FileInputStream("dbconfig.properties"));

            System.out.println(Constants.DATABASE_CONNECTION_PROP_FILE);
            /*if(contextClassLoader==null){
                System.out.println("class loader is null");
            }*/

            System.out.println(DBConnection.class.getClassLoader());
            String absPath = Constants.DATABASE_CONNECTION_PROP_FILE;
            properties.load(DBConnection.class.getClassLoader().getResourceAsStream(absPath));
            /*if(propertiesStream==null){
                System.out.println("Unable to locate properties file");
            }*/

            //properties.load(propertiesStream);
         //   input = new FileInputStream("config.properties");
            // load a properties file
            //prop.load(input);
            // get the property value and print it out
            url=properties.getProperty("url");
            user=properties.getProperty("user");
            password=properties.getProperty("password");
            db=properties.getProperty("dbName");
            DB_URL=url+db;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if(connection==null||connection.isClosed())
        {
            try {
                initializeFields();
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(DB_URL,user,password);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException ex){
                ex.printStackTrace();
            }
        }
        return connection;
    }


}
