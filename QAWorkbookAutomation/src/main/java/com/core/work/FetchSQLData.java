package com.core.work;

import dao.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by SunilDeP on 5/24/2017.
 */
public class FetchSQLData<T> {

   /* public void List getData(List m, String sql, String dbURL, String id, String pass, String sqlType) throws SQLException {


        String stageDb = (String) m.get(1);
        String prodDb = (String) m.get(2);


        switch (sqlType) {

            case "AllTrend":
                getAllTrend(sql, dbURL, id, pass);


        }

        return

    }*/



        public   List<T> getData(String sql, String dbURL, String id, String pass, Class<T> t) throws SQLException {


         /*   List list = doThis(sql, dbURL,id,pass);

            return list;

        }

            public List<AllTrend_t> doThis(String sql, String dbURL, String id, String pass) throws SQLException {*/

        List list =new ArrayList<T>();
       // List IBNRDataList = new ArrayList<T>();


        ResultSet rs = null;
        PreparedStatement preparedStatement = null;
        DBConnection db = new DBConnection();

        Connection stageConn = db.dbConnect(dbURL, id, pass);


        try {
            String selectSQL = sql;

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

            preparedStatement = stageConn.prepareStatement(selectSQL);
            // preparedStatement.setString(1, "Incomplete");

            // execute select SQL stetement
            rs = preparedStatement.executeQuery();


            while (rs.next()) {
                if(t.equals(AllTrend_t.class)){
                    list.add(new AllTrend_t(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),
                            rs.getString(6),rs.getInt(7),rs.getDouble(8) ));
                }else if(t.equals(IBNRData_t.class)){

                        list.add(new IBNRData_t(rs.getString(1), rs.getString(2),rs.getString(3),
                                rs.getString(4),rs.getDouble(5), rs.getDouble(6)));

                }else if (t.equals(OtherChecks_t.class)){

                    list.add(new OtherChecks_t(rs.getString(1), rs.getString(2),rs.getString(3),
                            rs.getString(4),rs.getString(5)));

                }
                /*allTrendList.add(new AllTrend_t(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),
                        rs.getString(6),rs.getInt(7),rs.getDouble(8) ));*/

            }


        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (stageConn != null && !stageConn.isClosed()) {
                stageConn.close();
            }

        }
        return list;

    }





}



