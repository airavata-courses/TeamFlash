package com.teamFlash.microservices.registry.forecast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by girish on 12/11/16.
 */
public class JobDAO {

    public int getMaxJobID()
    {
        int res = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "select max(job_id) as MAX_JOB_ID from user_job_dtls ";
        try
        {
            conn = DBConnection.getConnection();
            pstmt=conn.prepareStatement(query);
            rs = pstmt.executeQuery(query);
            while(rs.next())
            {
                res = Integer.parseInt(rs.getString("MAX_JOB_ID"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;
    }

    public void insertJobDetails(String user,int jobid)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        //ResultSet rs = null;
        String query = "Insert into user_job_dtls values(?,?) ";
        System.out.println("inside insertJobDetails: "+user+" / "+jobid);
        try
        {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1,user);
            pstmt.setInt(2,jobid);

            pstmt.executeUpdate();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(conn!=null){
                try{
                    conn.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Integer> getJobs(String user)
    {
        List<Integer> listJobs = new ArrayList<Integer>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "select* from  user_job_dtls  where user_name = ?";
        try
        {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1,user);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                int jobid = rs.getInt("job_id");
                listJobs.add(jobid);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("user: "+user+" joblists below");
        System.out.println(listJobs);
        return listJobs;
    }

}
