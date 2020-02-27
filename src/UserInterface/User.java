/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModels;

import DAO.QueryManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 *
 * @author Kenneth Ham
 */
public class User {
    
    private int id;
    private String username;
    private String password;
    private boolean active;
    private Calendar createdDate;
    private String createdBy;
    private Calendar lastUpdated;
    private String lastUpdatedBy;
    
    // CONSTANTS
    private final String SELECTALL = " * from user";
    
    public User(String user, String pass, boolean isActive, Calendar created, String createdBy, Calendar lastUpdated, String updatedBy){
        this.id = getLastId() + 1;
        this.username = user;
        this.password = pass;
        this.active = isActive;
        this.createdDate = created;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = updatedBy;
    }
    
    public User(int id, String user, String pass, boolean isActive, Calendar created, String createdBy, Calendar lastUpdated, String updatedBy){
        this.id = id;
        this.username = user;
        this.password = pass;
        this.active = isActive;
        this.createdDate = created;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = updatedBy;
    }
    
    // METHODS
    private int getLastId(){

        int lastId = 0;
        try {
            QueryManager.makeRequest("select", SELECTALL);
            ResultSet result = QueryManager.getResults();
            if(result.last()){
                lastId = result.getInt("id");
            }
        } catch (SQLException eSQL){
            System.out.println(eSQL);
        }
        return lastId;
    }
    
    // GETTERS & SETTERS
     public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Calendar getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Calendar lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
}
