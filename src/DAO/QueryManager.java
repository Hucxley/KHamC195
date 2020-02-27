/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import static DAO.DBConnection.DB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Kenneth Ham
 */
public class QueryManager {
    
    private static String query;
    private static String queryType;
    private static Statement statement;
    private static ResultSet results;
    
    public static void makeRequest(String type, String q) {
        
        try {
            queryType = type;
            query = q;
            statement = DB.createStatement();
            if(queryType.equals("select")){
                results = statement.executeQuery(queryType + query);
            } else {
                statement.executeUpdate(queryType + query);
            }
 
        } catch (SQLException eSQL){
            System.out.println("sql error: "+ eSQL);
        } catch (Exception e) {
            System.out.println("general error: "+ e);
        }
        
    }
    
    public ResultSet getDataFromTable(String tableName){
        
        String q = " * from '" + tableName + "'";
        ResultSet response = null;
        
        try {
            QueryManager.makeRequest("select", q);
            response = QueryManager.results;
            if(response.next()){
               return response;
            } 
        } catch (SQLException eSQL){
            System.out.println("SQL Error: " + eSQL);
        }
        
        return response;
    }
    
    public static ResultSet getResults(){
        return results;
    }
    
    
}
