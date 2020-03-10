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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private boolean allNotificationsViewed = true;
    
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
    
    private ResourceBundle languages;


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println(rb.getLocale());
        System.out.println(Locale.getDefault());
        languages = rb;
        btnLoginSubmit.setText(languages.getString("SUBMIT"));
        txtUsername.setPromptText(languages.getString("USERNAME_PROMPT"));
        txtPassword.setPromptText(languages.getString("PASSWORD_PROMPT"));
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
                    appointments.forEach(appointment -> {
                        System.out.println(currentTime);
                        ZonedDateTime startTime = appointment.getStart();
                        System.out.println("appointment start time: " + startTime );
                        int diffBetween = (int) currentTime.until(startTime, ChronoUnit.MINUTES);
                        System.out.println("time until next appointments: " + diffBetween);
                        if(diffBetween >= 0 && diffBetween <= 15){
                            this.setAllNotificationsViewed(false);
                            Alert confirmAlert = new Alert(Alert.AlertType.WARNING);
                            
                            confirmAlert.setTitle(languages.getString("ALERT_TITLE"));
                            confirmAlert.setHeaderText(java.text.MessageFormat.format(languages.getString("ALERT_HEADER"), new Object[] {diffBetween}));
                            confirmAlert.setContentText(languages.getString("ALERT_TEXT"));

                            Optional<ButtonType> response = confirmAlert.showAndWait();
                             if(response.get() == ButtonType.OK){
                                 this.setAllNotificationsViewed(true);
                             }
                            
                        }
                        
                    });
                    
                    logUserInfo(currentUser);
                    setActiveUser(currentUser);
                }
            }
        }
        
        System.out.println("user authenticated: " + authenticatedUser);
        
        if(!authenticatedUser){
            label.setText(languages.getString("LOGIN_ERROR"));
        }else{
            if(this.getAllNotificationsViewed()){
                
                stage = ApplicationStateController.getMainStage();
                label.setText("Login Successful!");
                Parent root = FXMLLoader.load(getClass().getResource("UserScreen.fxml"));

                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
                
            }
        }
    }

    public boolean getAllNotificationsViewed() {
        return allNotificationsViewed;
    }

    public void setAllNotificationsViewed(boolean allNotificationsViewed) {
        this.allNotificationsViewed = allNotificationsViewed;
    }
 
    
    private static void logUserInfo(User user){
        String timestampNow = ZonedDateTime.now(ZoneId.of("UTC")).toString();
        String userActivity = timestampNow + ": user: " + user.getUsername() + " logged in.";
        Utilities.ActivityLog.logUserActivity(userActivity);
    }


    
}
