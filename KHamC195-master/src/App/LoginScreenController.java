/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DAO.AppointmentDataAccess;
import DAO.DBConnection;
import DAO.UserDataAccess;
import DataModels.Appointment;
import DataModels.User;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author k16080h
 */
public class LoginScreenController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtUsername;  
    @FXML
    private AnchorPane loginAnchor;
    
    Stage stage; 
    
    private static void setActiveUser(User user){
        ApplicationStateController.setActiveUser(user);
    }
    
    public static void clearActiveUser(){
        ApplicationStateController.setActiveUser(null);
    }
    
    public static User getActiveUser(){
        return ApplicationStateController.getActiveUser();
    }
    @FXML
    private Button btnLoginSubmit;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleLoginSubmitButton(ActionEvent event) throws IOException {
        
        String inputUser = txtUsername.getText().trim();
        String inputPass = txtPassword.getText().trim();
        ZonedDateTime currentTime = ZonedDateTime.now();
        boolean authenticatedUser = false;
        User currentUser;
        
        if(!authenticatedUser){
            DBConnection.startConnection();       


            currentUser = UserDataAccess.getByUsername(txtUsername.getText().trim());
            if(currentUser != null){
                authenticatedUser = (inputUser.equals(currentUser.getUsername()) && inputPass.equals(currentUser.getPassword()));
                if(authenticatedUser){
                    ArrayList<Appointment> appointments = (ArrayList) AppointmentDataAccess.getAppointmentsByUserId(currentUser.getUserId());
                    System.out.println(appointments.size());
                    appointments.forEach((Appointment appointment) -> {
                       ZonedDateTime startTime = appointment.getStart();
                        int diffBetween = startTime.compareTo(currentTime);
                        System.out.println("difference between appointments: " + diffBetween);
                        
                    });
                    
                    logUserInfo(currentUser);
                    setActiveUser(currentUser);
                }
            }
        }
        
        System.out.println("user authenticated: " + authenticatedUser);
        
        if(!authenticatedUser){
            label.setText("Unable verify username/password. Please try again.");
        }else{
            
            stage = ApplicationStateController.getMainStage();
            label.setText("Login Successful!");
            Parent root = FXMLLoader.load(getClass().getResource("UserScreen.fxml"));
        
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
    }
    
    private static void logUserInfo(User user){
        String timestampNow = ZonedDateTime.now(ZoneId.of("UTC")).toString();
        String userActivity = timestampNow + ": user: " + user.getUsername() + " logged in.";
        Utilities.ActivityLog.logUserActivity(userActivity);
    }

    private Appointment QueryManager(String select, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    
}
