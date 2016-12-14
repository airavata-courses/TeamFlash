package com.teamFlash.microservices.registry.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Created by girish on 9/24/16.
 */
public class LoggerDAO {

     String url;
     String db ;
     String JDBC_DRIVER = "com.mysql.jdbc.Driver";
     String DB_URL = null;
     String user ;
     String password ;

    LoggerDAO()
    {
        initializeFields();
    }

    public void initializeFields()
    {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            url=prop.getProperty("url");
            user=prop.getProperty("user");
            password=prop.getProperty("password");
            db=prop.getProperty("dbName");
            DB_URL=url+db;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertLog(Log log)
    {
        Connection conn=null;
        PreparedStatement pstmt = null;

        try
        {
            String userID = log.getUserID();
            String requestID = log.getRequestID();
            String microservice = log.getMicroservice();
            String description = log.getLogDescription();
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,user,password);
            System.out.println("Trying to initiate connection"); 
            Date date = new Date(System.currentTimeMillis());
            Timestamp now = new Timestamp(date.getTime());
             System.out.println("Date: "+date);
            String sql = "Insert into user_request_log_dtls values (?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            System.out.println("After prepared statement"); 
            pstmt.setString(1,requestID);
            pstmt.setString(2,userID);
            pstmt.setString(3,microservice);
            pstmt.setString(4,description);
            pstmt.setTimestamp(5,now);
            pstmt.executeUpdate();
            System.out.println("After execute query"); 
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception occured in insertLog..."+e.getMessage()); 
        }
        finally
        {
           if (conn != null) {
                try {
                     System.out.println("Inside try to cllose connection");
                         conn.close();
                    } catch (SQLException e) {
                         e.printStackTrace();
                 }
               }
        }
    }
    
    public StringBuilder fetchLog(String id)
    {
    	StringBuilder res=new StringBuilder();
        Connection conn=null;
        PreparedStatement pstmt = null;
        try
        {
            
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,user,password);
            System.out.println("Trying to initiate connection");
            
            String sql = "select * from user_request_log_dtls where request_id = '"+id+"'";
            pstmt = conn.prepareStatement(sql);
            System.out.println("sql query: "+sql);
            //pstmt.setString(1, id);
            System.out.println("After prepared statement"); 
            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next())
            {
              String requestID = ""+rs.getString("request_id");
              System.out.println("Request ID :"+requestID); 
              String userID = ""+rs.getString("username");
              System.out.println("User ID :"+userID); 
              Date dateCreated = rs.getDate("log_date");
              String date="";
              if(dateCreated!=null)
              {
            	  date=date+dateCreated.toString(); 
              }
              System.out.println("Date created :"+date); 
              String description = ""+rs.getString("log_description");
              System.out.println("Description :"+description); 
              String microservice = ""+rs.getString("microservice");
              System.out.println("Microservice :"+microservice); 
              res.append(requestID + ",");
              res.append(userID + ",");
              res.append(date + ",");
              res.append(description + ",");
              res.append(microservice + ";");
              
            }
            
            System.out.println("After execute query with result :"+res.toString()); 
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Exception occured in fetchLog..."+e.getMessage());
        }
        finally
        {
           if (conn != null) {
                try {
                     System.out.println("Inside try to close connection");
                         conn.close();
                    } catch (SQLException e) {
                         e.printStackTrace();
                         System.out.println("Exception occured while closing connection in fetchLog..."+e.getMessage());
                 }
               }
        }
        return res;
    }


    public void deleteLog(String requestID)
    {
        Connection conn=null;
        PreparedStatement pstmt = null;

        try
        {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,user,password);
            String sql = "delete FROM user_request_log_dtls where request_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,requestID);
            pstmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
