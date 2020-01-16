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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

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
                Calendar createdDate = DateTimeManager.convertToCalendar(response.getString("createDate"));
                String createdBy = response.getString("createdBy");
                Calendar lastUpdated = DateTimeManager.convertToCalendar(response.getString("lastUpdate"));
                String lastUpdatedBy = response.getString("lastUpdateBy");

                foundUser = new User(userId, userName, password, active, createdDate, createdBy, lastUpdated, lastUpdatedBy);
                return foundUser;
            } else {
                System.out.println("user not found");
                return null;
            }
        } catch (SQLException | ParseException e) {
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
                Calendar createdDate = DateTimeManager.convertToCalendar(response.getString("createDate"));
                String createdBy = response.getString("createdBy");
                Calendar lastUpdated = DateTimeManager.convertToCalendar(response.getString("lastUpdate"));
                String lastUpdatedBy = response.getString("lastUpdateBy");

                foundUser = new User(userId, userName, password, active, createdDate, createdBy, lastUpdated, lastUpdatedBy);
                return foundUser;
            } else {
                System.out.println("user not found");
                return null;
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e);
        }
        
        return foundUser;
    }
    
}
