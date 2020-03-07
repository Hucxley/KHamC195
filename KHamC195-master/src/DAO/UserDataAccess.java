/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DataModels.User;
import Utilities.DateTimeManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kenneth Ham
 */
public class UserDataAccess {
    
    public static User getById(int id){
        User foundUser = null;
        try{
            QueryManager.makeRequest("select", " * from user where userId = '" + id + "'");
            ResultSet response = QueryManager.getResults();
            if(response.next()){
                int userId = response.getInt("userId");
                String userName = response.getString("userName");
                String password = response.getString("password");
                boolean active = response.getBoolean("active");
                Timestamp createDate = response.getTimestamp("createDate");
                
                ZonedDateTime createdDate = DateTimeManager.convertToZDT(createDate);

                // ZonedDateTime createdDate = DateTimeManager.convertToZDT(response.getTimestamp("createDate"));
                String createdBy = response.getString("createdBy");
                ZonedDateTime lastUpdated = DateTimeManager.convertToZDT(response.getTimestamp("lastUpdate"));
                String lastUpdatedBy = response.getString("lastUpdateBy");

                foundUser = new User(userId, userName, password, active, createdDate, createdBy, lastUpdated, lastUpdatedBy);
                return foundUser;
            } else {
                System.out.println("user not found");
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        
        return foundUser;
    }
    
    public static User getByUsername(String user){
        User foundUser = null;
        try{
            QueryManager.makeRequest("select", " * from user where userName = '" + user + "'");
            ResultSet response = QueryManager.getResults();
            if(response.next()){
                int userId = response.getInt("userId");
                String userName = response.getString("userName");
                String password = response.getString("password");
                boolean active = response.getBoolean("active");
                ZonedDateTime createdDate = DateTimeManager.convertToZDT(response.getTimestamp("createDate"));
                String createdBy = response.getString("createdBy");
                ZonedDateTime lastUpdated = DateTimeManager.convertToZDT(response.getTimestamp("lastUpdate"));
                String lastUpdatedBy = response.getString("lastUpdateBy");

                foundUser = new User(userId, userName, password, active, createdDate, createdBy, lastUpdated, lastUpdatedBy);
                return foundUser;
            } else {
                System.out.println("user not found");
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        
        return foundUser;
    }

    public static ObservableList getUsersList() {
        User foundUser = null;
        ObservableList<User> userList = FXCollections.observableArrayList();
        
        try {
            ResultSet response = null;
            String tableQuery = " * from user where active = true";
            QueryManager.makeRequest("select", tableQuery.toLowerCase());
            response = QueryManager.getResults();
            
            while(response.next()){
                int userId = response.getInt("userId");
                String userName = response.getString("userName");
                String password = response.getString("password");
                boolean active = response.getBoolean("active");
                ZonedDateTime createdDate = DateTimeManager.convertToZDT(response.getTimestamp("createDate"));
                String createdBy = response.getString("createdBy");
                ZonedDateTime lastUpdated = DateTimeManager.convertToZDT(response.getTimestamp("lastUpdate"));
                String lastUpdatedBy = response.getString("lastUpdateBy");

                foundUser = new User(userId, userName, password, active, createdDate, createdBy, lastUpdated, lastUpdatedBy);
                
                userList.add(foundUser);
                
            }
        
        } catch(SQLException ex){
            System.out.println(ex);
        }
        
        return userList;
    }
    
}
