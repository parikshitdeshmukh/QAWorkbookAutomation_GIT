package dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by SunilDeP on 5/23/2017.
 */
public class DBConnection {

    public Connection dbConnect(  String db_connect_string,
                            String db_userid,
                            String db_password){
        Connection conn=null;
        try{
            Class.forName( "com.microsoft.sqlserver.jdbc.SQLServerDriver" );
             conn= DriverManager.getConnection(
                    db_connect_string,
                    db_userid,
                    db_password);
            System.out.println( "connected" );
        }
        catch( Exception e ){
            e.printStackTrace();
        }

        return conn;
    }

}
