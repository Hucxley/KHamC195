/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DataModels.Appointment;
import Utilities.DateTimeManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Kenneth Ham
 */
public class AppointmentDataAccess {
    public static ArrayList<Appointment> getAppointmentsByUserId(int id){
        ArrayList<Appointment> appointmentList = new ArrayList();
        try{
            QueryManager.makeRequest("select", "* from appointment where userId = " + id);
            ResultSet response = QueryManager.getResults();
            while(response.next()){
                int appointmentId = response.getInt("appointmentId");
                int customerId = response.getInt("customerId");
                int userId = response.getInt("userId");
                String title = response.getString("title");
                String description = response.getString("description");
                String location = response.getString("location");
                String contact = response.getString("contact");
                String type = response.getString("type");
                String url = response.getString("url");
                ZonedDateTime start = response.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime end = response.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime createDate = response.getTimestamp("createDate").toLocalDateTime().atZone(ZoneId.of("UTC"));
                String createdBy = response.getString("createdBy");
                ZonedDateTime lastUpdate = response.getTimestamp("lastUpdate").toLocalDateTime().atZone(ZoneId.of("UTC"));
                String lastUpdateBy = response.getString("lastUpdateBy");
                
                Appointment appointment = new Appointment(
                        appointmentId, 
                        customerId, 
                        userId, 
                        title, 
                        description, 
                        location, 
                        contact, 
                        type, 
                        url, 
                        start, 
                        end, 
                        createDate, 
                        createdBy, 
                        lastUpdate, 
                        lastUpdateBy
                );
                
                appointmentList.add(appointment);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return appointmentList;
        
    }
public static ArrayList<Appointment> getAppointmentsByCustomerId(int id){
        ArrayList<Appointment> appointmentList = new ArrayList();
        try{
            QueryManager.makeRequest("select", " * from appointment where customerId = " + id);
            ResultSet response = QueryManager.getResults();
            while(response.next()){
                int appointmentId = response.getInt("appointmentId");
                int customerId = response.getInt("customerId");
                int userId = response.getInt("userId");
                String title = response.getString("title");
                String description = response.getString("description");
                String location = response.getString("location");
                String contact = response.getString("contact");
                String type = response.getString("type");
                String url = response.getString("url");
                ZonedDateTime start = DateTimeManager.convertToZDT(response.getTimestamp("start"));
                ZonedDateTime end = DateTimeManager.convertToZDT(response.getTimestamp("end"));
                ZonedDateTime createDate = DateTimeManager.convertToZDT(response.getTimestamp("createDate"));
                String createdBy = response.getString("createdBy");
                ZonedDateTime lastUpdate = DateTimeManager.convertToZDT(response.getTimestamp("lastUpdate"));
                String lastUpdateBy = response.getString("lastUpdateBy");
                
                Appointment appointment = new Appointment(
                        appointmentId, 
                        customerId, 
                        userId, 
                        title, 
                        description, 
                        location, 
                        contact, 
                        type, 
                        url, 
                        start, 
                        end, 
                        createDate, 
                        createdBy, 
                        lastUpdate, 
                        lastUpdateBy
                );
                
                appointmentList.add(appointment);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return appointmentList;
        
    }
    
    public static Appointment getByAppointmentId(int id){
        Appointment foundAppointment = null;
        try{
            QueryManager.makeRequest("select", " * from appointment where appointmentId = " + id);
            ResultSet response = QueryManager.getResults();
            while(response.next()){
                int appointmentId = response.getInt("appointmentId");
                int customerId = response.getInt("customerId");
                int userId = response.getInt("userId");
                String title = response.getString("title");
                String description = response.getString("description");
                String location = response.getString("location");
                String contact = response.getString("contact");
                String type = response.getString("type");
                String url = response.getString("url");
                ZonedDateTime start = DateTimeManager.convertToZDT(response.getTimestamp("start"));
                ZonedDateTime end = DateTimeManager.convertToZDT(response.getTimestamp("end"));
                ZonedDateTime createDate = DateTimeManager.convertToZDT(response.getTimestamp("createDate"));
                String createdBy = response.getString("createdBy");
                ZonedDateTime lastUpdate = DateTimeManager.convertToZDT(response.getTimestamp("lastUpdate"));
                String lastUpdateBy = response.getString("lastUpdateBy");
                
                foundAppointment = new Appointment(
                        appointmentId, 
                        customerId, 
                        userId, 
                        title, 
                        description, 
                        location, 
                        contact, 
                        type, 
                        url, 
                        start, 
                        end, 
                        createDate, 
                        createdBy, 
                        lastUpdate, 
                        lastUpdateBy
                );
            }
        } catch (SQLException ex){
            System.out.println(ex);
        }
        
        return foundAppointment;
    }
    
    public static ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try{
            QueryManager.makeRequest("select", " * from appointment");
            ResultSet response = QueryManager.getResults();
            while(response.next()){
                int appointmentId = response.getInt("appointmentId");
                int customerId = response.getInt("customerId");
                int userId = response.getInt("userId");
                String title = response.getString("title");
                String description = response.getString("description");
                String location = response.getString("location");
                String contact = response.getString("contact");
                String type = response.getString("type");
                String url = response.getString("url");
                System.out.println(response.getTimestamp("start"));
                System.out.println(response.getTimestamp("end"));
                ZonedDateTime start = DateTimeManager.convertToZDT(response.getTimestamp("start"));
                ZonedDateTime end = DateTimeManager.convertToZDT(response.getTimestamp("end"));
                ZonedDateTime createDate = DateTimeManager.convertToZDT(response.getTimestamp("createDate"));
                String createdBy = response.getString("createdBy");
                ZonedDateTime lastUpdate = DateTimeManager.convertToZDT(response.getTimestamp("lastUpdate"));
                String lastUpdateBy = response.getString("lastUpdateBy");
                
                Appointment appointment = new Appointment(
                        appointmentId, 
                        customerId, 
                        userId, 
                        title, 
                        description, 
                        location, 
                        contact, 
                        type, 
                        url, 
                        start, 
                        end, 
                        createDate, 
                        createdBy, 
                        lastUpdate, 
                        lastUpdateBy
                );
                
                appointmentList.add(appointment);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return appointmentList;
        
    }
    
}
