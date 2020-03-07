/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModels;

import DAO.QueryManager;
import Utilities.DateTimeManager;
import java.time.ZonedDateTime;
import java.util.Calendar;

/**
 *
 * @author Kenneth Ham
 */
public class User {
    
    private int userId;
    private String username;
    private String password;
    private boolean active;
    private ZonedDateTime createdDate;
    private String createdBy;
    private ZonedDateTime lastUpdated;
    private String lastUpdatedBy;
    private String createdDateDisplay;
    private String lastUpdatedDisplay;
    
    // CONSTANTS

    
    
    // CONSTRUCTORS
    /**
     * 
     * @param user
     * @param pass
     * @param isActive
     * @param created
     * @param createdBy
     * @param lastUpdated
     * @param updatedBy Calendar
     */
    public User(String user, String pass, boolean isActive, ZonedDateTime created, String createdBy, ZonedDateTime lastUpdated, String updatedBy){
        this.userId = QueryManager.getLastIdFromTable("user");
        this.username = user;
        this.password = pass;
        this.active = isActive;
        this.createdDate = created;
        this.createdDateDisplay = DateTimeManager.toDateTimeString(created);
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = updatedBy;
        this.lastUpdatedDisplay = DateTimeManager.toDateTimeString(lastUpdated);
    }
    
    /**
     * 
     * @param id
     * @param user
     * @param pass
     * @param isActive
     * @param created
     * @param createdBy
     * @param lastUpdated
     * @param updatedBy 
     */
    public User(int id, String user, String pass, boolean isActive, ZonedDateTime created, String createdBy, ZonedDateTime lastUpdated, String updatedBy){
        this.userId = id;
        this.username = user;
        this.password = pass;
        this.active = isActive;
        this.createdDate = created;
        this.createdDateDisplay = DateTimeManager.toDateTimeString(created);
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = updatedBy;
        this.lastUpdatedDisplay = DateTimeManager.toDateTimeString(lastUpdated);
    }
    
    
    // CLASS METHODS

    @Override 
    public String toString(){
        return username;
    }
    
    
    // GETTERS & SETTERS
     public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getCreatedDateDisplay() {
        return createdDateDisplay;
    }

    public void setCreatedDateDisplay(String createdDateDisplay) {
        this.createdDateDisplay = createdDateDisplay;
    }

    public String getLastUpdatedDisplay() {
        return lastUpdatedDisplay;
    }

    public void setLastUpdatedDisplay(String lastUpdatedDisplay) {
        this.lastUpdatedDisplay = lastUpdatedDisplay;
    }
    
}
