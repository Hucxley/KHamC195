/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DataModels.Customer;
import DataModels.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author Kenneth Ham
 */
public class ApplicationStateController extends Application {
    
    private static Stage mainStage;
    private static Customer selectedCustomer;
    private static User activeUser;
    private static ObservableList<String> allowedAppointmentTimes = FXCollections.observableArrayList();
    
    
    private static void populateAppointmentTimes(){
        // startHour is the hour appointments can start
        int startHour = 8;
        
        // totalAppointmentHours is the number of hours allowed for appointments after the startHour
        int totalAppointmentHours = 8;
        
        // currentHour is used for time display calculation during iteration for appointment time generation
        int currentHour;
        String displayHour = "";
        String prefix;
        for(int i = 0; i < totalAppointmentHours; i++){
            currentHour = startHour + i; // advance the current hour with i
            if(currentHour < 10){
                prefix = "0";
            }else { 
                prefix = "";
            }
            
            // displayHour concatenates time strings in 15 minute increments to insert in allowedAppointmentTimes list
            displayHour = prefix + currentHour + ":00";
            allowedAppointmentTimes.add(displayHour);
            displayHour = prefix + currentHour + ":15";
            allowedAppointmentTimes.add(displayHour);
            displayHour = prefix + currentHour + ":30";
            allowedAppointmentTimes.add(displayHour);
            displayHour = prefix + currentHour + ":45";
            allowedAppointmentTimes.add(displayHour);
            
        }
    }
    
    private static void setStage(Stage stage){
        mainStage = stage;
    }
    
    public static Stage getMainStage(){
        return ApplicationStateController.mainStage;
    }
    
    private static void setSelectedCustomer(Customer customer){
        selectedCustomer = customer;
    }
    
    public static Customer getSelectedCustomer(){
        return selectedCustomer;
    }
    
    static void setActiveUser(User user){
       activeUser = user;
    }
    
    public static User getActiveUser(){
        return activeUser;
    }

    public static ObservableList<String> getAllowedAppointmentTimes() {
        return allowedAppointmentTimes;
    }
    
    @Override
    public void start(Stage stage) throws Exception {

        populateAppointmentTimes();
        setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
    /* Stop override stop method behavior from user Jose Pareda
       from Stack Overflow https://stackoverflow.com/a/26620713
    */
    @Override
    public void stop() throws SQLException{
        setActiveUser(null);
        setSelectedCustomer(null);
        Connection db = DAO.DBConnection.getOpenConnection();
        if(db != null){
            System.out.println("closing db connection");
            db.close();
        }
        // TODO: verify session data closed
        System.out.println("Session data cleared");
        mainStage.close();
   
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    
}
