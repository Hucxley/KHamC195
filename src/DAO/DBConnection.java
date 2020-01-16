/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Kenneth Ham
 */
public class DBConnection {
    
    static Connection DB;
    private static final String DBNAME = "U04RJP";
    private static final String DBURL = "jdbc:mysql://3.227.166.251/"+DBNAME;
    private static final String USERNAME = "U04RJP";
    private static final String PASSWORD = "53688321832";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    public static void startConnection(){
        try {
            Class.forName(DRIVER);
            DB = (Connection) DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            if(DB != null){
                // DB is accessible
                System.out.println("db connected");
                Statement st = DB.createStatement();
                ResultSet rs = st.executeQuery("select * from user");
                System.out.println(rs);
            }
            
        } catch (ClassNotFoundException ex){
            System.out.println("driver class not found!");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
}
