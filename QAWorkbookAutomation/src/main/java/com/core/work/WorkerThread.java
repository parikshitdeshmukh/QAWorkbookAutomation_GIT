package com.core.work;

import dao.DataSource;
import dao.Job_status_t;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkerThread{
    private DataSource ds;
    private Connection conn;
    List<Job_status_t> dataToWork;


    public void startProgram() {

       // File dir = new File("X:\\CPRM\\Required_Backups\\QA_Workbook\\SourceCode");


       // DataProvider provider = DataProvider.getInstance();
        //List<Job_status_t> dataToWork = provider.getData();

        try {
            ds = DataSource.getInstance();
            conn = ds.getConnection();
            PullInfo pullInfo = new PullInfo();
            dataToWork = pullInfo.pullDataBaseNames(conn);
        }catch (Exception e){
            e.printStackTrace();
        }

       // PullInfo pullInfo= new PullInfo();
        //List<Job_status_t> dataToWork = pullInfo.pullDataBaseNames();

        //System.out.println(Thread.currentThread().getName() + " : " + provider.lastReturned);
        for(Job_status_t data :dataToWork){
            try{
                new IndexedThread(data).excelWork();
                Thread.sleep(2000);
            }catch(Exception e){
                e.printStackTrace();
            }
        }




    }


}
