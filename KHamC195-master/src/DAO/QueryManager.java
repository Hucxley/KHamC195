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
    
    private static String requestBody;
    private static String requestType;
    private static Statement statement;
    private static ResultSet results;
    
    
    /**
     * 
     * @param queryType queryType of request ('select', 'insert', 'update', 'delete')
     * @param queryBody request body ( "* from $tableName $conditional")
     */
    public static void makeRequest(String queryType, String queryBody) throws SQLException {
        
        try {
            requestType = queryType;
            requestBody = " " + queryBody;
            statement = DB.createStatement();
            if(queryType.equals("select")){
                results = statement.executeQuery(requestType + requestBody);
            } else {
                statement.executeUpdate(queryType + requestBody);
            }
 
        } catch (SQLException eSQL){
            System.out.println("sql request error: "+ eSQL);
        } catch (Exception e) {
            System.out.println("general error: "+ e);
        } finally {
            
        }
        
        
    }
    
    public static ResultSet getDataFromTable(String tableName) throws SQLException{
        ResultSet response = null;
        String tableQuery = " * from " + tableName;
        QueryManager.makeRequest("select", tableQuery.toLowerCase());
        response = QueryManager.getResults();
        
        return response;
    }
    
    /**
     * 
     * @return results from most recent DB query
     */    
    public static ResultSet getResults(){
        return results;
    }
    
    /**
     * 
     * @param tableName name of table to query
     * @return $tableNameId found on last row, 0 if no results
     */
    public static int getLastIdFromTable(String tableName){
        String queryString = "* from '" + tableName + "'";
        String idColumnName = tableName + "Id";
        int lastId = 0;
        try {
            QueryManager.makeRequest("select", queryString.toLowerCase());
            ResultSet result = QueryManager.getResults();
            if(result.last()){
                lastId = result.getInt(idColumnName);
            }
        } catch (SQLException eSQL){
            System.out.println(eSQL);
        }
        return lastId;
    }
    
    
}
