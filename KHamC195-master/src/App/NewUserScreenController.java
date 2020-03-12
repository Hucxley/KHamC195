/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DataModels.User;
import Utilities.ActivityLog;
import Utilities.DateTimeManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author k16080h
 */
public class NewUserScreenController implements Initializable {

    @FXML
    private Label txtHeader;
    @FXML
    private Button btnAddNewUser;
    @FXML
    private Button btnCancelNewUser;    
    @FXML
    private Label txtNewUserError;
    @FXML
    private TextField txtNewUsername;
    @FXML
    private PasswordField txtNewPassword;
    @FXML
    private PasswordField txtNewPasswordConfirm;

    Stage stage = ApplicationStateController.getMainStage();
    private String username;
    private String password;
    private String passwordConfirm;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleAddNewUserButton(ActionEvent event) throws IOException {
        txtNewUserError.setVisible(false);
        this.username = txtNewUsername.getText();
        this.password = txtNewPassword.getText();
        this.passwordConfirm = txtNewPasswordConfirm.getText();
        if(!this.password.matches(this.passwordConfirm)){
            // PASSWORDS DON'T MATCH SHOW LABEL
            txtNewUserError.setText("Password Confirmation does not match. Please try again...");
            txtNewUserError.setVisible(true);
        }else {
            try {
                createNewUserRecord();
            }catch (ParseException parseEx){
                System.out.println(parseEx);
            }
        }
        
    }

    @FXML
    private void handleCancelNewUserButton(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("ManageUsersScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        
    }
    
    private void createNewUserRecord() throws ParseException, IOException{
        Boolean successfulInsert = false;
        Timestamp sqlNow = DateTimeManager.zdtLocalToTimestampSQL(ZonedDateTime.now());
        try {
            User existingUser = DAO.UserDataAccess.getByUsername(this.username);
            if(existingUser != null){
                txtNewUserError.setText("That Username is not available. Please try a different username");
                txtNewUserError.setVisible(true);
            }else{
                String currentUser = ApplicationStateController.getActiveUser().getUsername();
                int isActive = 1;
                String queryBody = " into user (username, password, active, createDate, createdBy, lastUpdate, lastUpdateBy) values (";
                queryBody += "'" + this.username + "', " ;
                queryBody += "'" + this.password + "', " ;
                queryBody += "'" + isActive + "', " ;
                queryBody += "'" + sqlNow + "', " ;
                queryBody += "'" + currentUser + "', " ;
                queryBody += "'" + sqlNow + "', " ;
                queryBody += "'" + currentUser + "')" ;
                
                DAO.QueryManager.makeRequest("INSERT", queryBody);
                
                // TODO: RETURN TO USER MANAGEMENT SCREEN IF SUCCESSFUL.
                User newUser = DAO.UserDataAccess.getByUsername(this.username);
                if(newUser != null){
                    successfulInsert = true;
                }
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        
        if(successfulInsert){
            try (PrintWriter auditLog = ActivityLog.getAuditLog()) {
                auditLog.append(sqlNow + "[UTC]: New User record created for " + this.username + " by: " + ApplicationStateController.getActiveUser() + ".\n");
            } catch (Exception ex){
                System.out.println(ex);
            }
            Parent root = FXMLLoader.load(getClass().getResource("ManageUsersScreen.fxml"));
        
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }

    }
    
}
