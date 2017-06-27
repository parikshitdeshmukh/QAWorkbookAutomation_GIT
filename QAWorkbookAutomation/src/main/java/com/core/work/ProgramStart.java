package com.core.work;

/**
 * Created by SunilDeP on 5/23/2017.
 */

/*import dao.*;
import dao.DataSource;
import dao.Job_status_t;*/

import javax.sql.ConnectionPoolDataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.core.work.*;


public class ProgramStart {


    public static void main(String[] arg) throws SQLException, InterruptedException, IOException {


       /* String cmd = "C:\\work\\QAWorkbookAutomation\\QA Workbook\\run.bat";
        String wb="C:\\work\\QAWorkbookAutomation\\QA_Workbook\\New23.xlsm";
        String stageDB="GrondStaging_Adena_T_201705050057";
        String prodDB="CPRMProd_Adena_T_201705050448";

        Runtime.getRuntime().exec("wscript C:\\work\\QAWorkbookAutomation\\QA_Workbook\\myvb.vbs "+wb+" "+stageDB+" "+prodDB);*/

        // QA_Trendsheet_V1.xlsm!'Trend_Sheet "abc","abc"'

     /*  IndexedThread thread = new IndexedThread();


        int ActiveThread =0;

        Thread t1= new Thread();
        Job_status_t jobData= null;

        DBConnection db = new DBConnection();
        DataSource ds =  null;
        try {
            ds  = DataSource.getInstance();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }



       // Connection conn = db.dbConnect("jdbc:sqlserver://192.168.17.64:1433;databaseName=QA_Reports_DB", "Grondadmin", "Welcome2Grond!");
        Connection conn;
        PullInfo info=new PullInfo();

        boolean isRunning = true;
        while (isRunning){

                if (ActiveThread == 4) {

                } else {

                    if (!thread.isAlive()) {

                        try{
                            conn = ds.getConnection();
                            jobData=info.pullDataBaseNames(conn);
                            if (jobData==null)
                                break;
                            thread = new IndexedThread(jobData);

                            thread.start();
                            // Thread.sleep(1000);
                            ++ActiveThread;

                        }catch(Exception e){
                            e.printStackTrace();
                        }


                    }
                    if (!thread2.isAlive()) {

                        try {
                            conn = ds.getConnection();
                            jobData=info.pullDataBaseNames(conn);
                            if (jobData==null) {
                                break;
                            }
                            thread = new IndexedThread(jobData);

                            thread.start();
                            //Thread.sleep(1000);

                            ++ActiveThread;
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    } if (!thread3.isAlive()) {
                        conn = ds.getConnection();
                        jobData=info.pullDataBaseNames(conn);
                        if (jobData==null)
                            break;
                        thread = new IndexedThread(jobData);

                        thread.start();
                        Thread.sleep(1000);
                        ++ActiveThread;

                    } if (!thread.isAlive()) {
                        conn = ds.getConnection();
                        jobData=info.pullDataBaseNames(conn);
                        if (jobData==null)
                            break;
                        thread = new IndexedThread(jobData);

                        thread.start();
                        Thread.sleep(1000);
                        ++ActiveThread;

                    }

                }


          /*  String url = "jdbc:odbc:DRIVER={Microsoft Excel Driver (*.xls)};"
                    + "DBQ=X:/book1.xlsx;ReadOnly=0;";*/
/*

    private Connection getExcelRef() {
        try {
            con = DriverManager.getConnection(url);
            return con;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    ArrayList getDataFromDB() {
        try {
            Connection con = null;
            con = DBConnection.getConnection();
            ArrayList list = new ArrayList();
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery("select * from student");
            while (rset.next()) {
                Student student = new Student(rset.getString("name"), rset.getInt("roll"), rset.getInt("marks"));
                list.add(student);
            }
            return list;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }

    public void setDataIntoSheet() {
        PreparedStatement ps;
        ArrayList<Student> list = getDataFromDB();
        int i = 0;
        for (Student s : list) {
            try {
                ps = getExcelRef().prepareStatement("insert into [Sheet1$](NAME,ROLL,MARKS) values(?,?,?) ");
                ps.setString(1, s.getName());
                ps.setInt(2, s.getRoll());
                ps.setInt(3, s.getMarks());
                i = ps.executeUpdate();

            } catch (SQLException ex) {

            } finally {
                try {
                    con.close();
                } catch (SQLException ex) {

                }
            }

        }
        if (i > 0) {
            System.out.println("Data imported successfully");
        } else {
            System.out.println("Problem in data insertion");
        }
    }

}


            }*/

       /* ExecutorService executor = Executors.newFixedThreadPool(5);//creating a pool of 5 threads

        Runnable worker = new WorkerThread();
        try {
            executor.execute(worker);//calling execute method of ExecutorService

        } catch (Exception e) {

        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }*/

        WorkerThread w= new WorkerThread();
        w.startProgram();

        System.out.println("Finished all threads");
    }
}