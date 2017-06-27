package com.core.work;

import dao.DBConnection;
import dao.Job_status_t;
import jdk.nashorn.internal.scripts.JO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SunilDeP on 5/24/2017.
 */
public class PullInfo {

    Job_status_t jobData= null;
    DBConnection db= new DBConnection();
    Connection conn = db.dbConnect("jdbc:sqlserver://192.168.17.64:1433;databaseName=QA_Reports_DB", "Grondadmin", "Welcome2Grond!");

    ResultSet rs = null;

    public List<Job_status_t> pullDataBaseNames(Connection conn) throws SQLException {
         List<Job_status_t> result = new ArrayList<Job_status_t>();


            PreparedStatement preparedStatement = null;




                String selectSQL = "SELECT  JOB_ID, Staging_DB, Production_DB, JOB_NAME FROM JOB_STATUS WHERE STATUS = ?";

                /*SELECT TOP 1000 [JOB_ID]
      ,[JOB_NAME]
      ,[EXECUTOR_NUMBER]
      ,[BUILD_NUMBER]
      ,[Staging_DB]
      ,[Production_DB]
      ,[USER_ID]
      ,[USER_NAME]
      ,[Date_Time]
      ,[QA_Date_Time]
      ,[STATUS]
      ,[TrendSheet_Status]
      ,[SalesforceIssueID]
      ,[GenerateTrendSheet]
      ,[SalesForceActivity]
      ,[SFintegrationStatus]
                FROM [QA_Reports_DB].[dbo].[JOB_STATUS]*/

                preparedStatement = conn.prepareStatement(selectSQL);
                preparedStatement.setString(1, "Incomplete");

                rs = preparedStatement.executeQuery();
                if (rs.getFetchSize() != 0) {
                    while (rs.next()) {

                        jobData = new Job_status_t(rs.getInt("JOB_ID"), rs.getString("Staging_DB"), rs.getString("Production_DB"), rs.getString("JOB_NAME"));
                        result.add(jobData);
                    }
               /* try{
                    //update the row as "InProcess"
                    String updateSQL = "update JOB_STATUS set STATUS= ? where JOB_ID =? ";

                    preparedStatement = conn.prepareStatement(updateSQL);
                    preparedStatement.setString(1, "InProcess");
                    preparedStatement.setInt(2, jobData.getJob_id());
                    preparedStatement.execute();

                } catch(SQLException e){

                    System.out.println(e.getMessage());

                } finally{

                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }

                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }


                }*/
            }else{
                    result=null;

                }
            return result;

        }

}










