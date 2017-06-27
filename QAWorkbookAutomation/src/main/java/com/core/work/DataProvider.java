package com.core.work;

import dao.DBConnection;
import dao.DataSource;
import dao.Job_status_t;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by SunilDeP on 6/7/2017.
 */
public class DataProvider {

    private DataSource ds;
    private Connection conn;
    List<Job_status_t> data;
    int lastReturned;
    final int SIZE = 10;
    private static Object mutex= new Object();

    private DataProvider() {
        try {
            ds  = DataSource.getInstance();
            conn = ds.getConnection();
            PullInfo pullInfo = new PullInfo();
            data=  pullInfo.pullDataBaseNames(conn);
            lastReturned=-1;
        }catch (Exception e){
            e.printStackTrace();
           // System.exit(1);
        }

    }

    private static DataProvider provider ;

    static DataProvider getInstance(){
        if(provider == null){
            synchronized (mutex) {
                provider = new DataProvider();
            }
        }
        return provider;
    }

    synchronized List<Job_status_t> getData(){

        synchronized (this) {
            int fromIndex = lastReturned + 1;
            if (fromIndex >= data.size()) {
                return Collections.emptyList();
            }
            int endIndex = lastReturned + SIZE;
            if (endIndex >= data.size()) {
                endIndex = data.size() - 1;
            }
            lastReturned=endIndex;
            return data.subList(fromIndex, endIndex + 1);

        }
    }
}
