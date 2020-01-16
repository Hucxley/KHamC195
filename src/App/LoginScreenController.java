/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DAO.DBConnection;
import DAO.QueryManager;
import DAO.UserDataAccess;
import DataModels.User;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
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
    private Button button;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtUsername;  
    @FXML
    private AnchorPane loginAnchor;
    
    Stage stage;
    
    private static User activeUser;
    
    private static void setActiveUser(User user){
        LoginScreenController.activeUser = user;
    }
    
    public static void clearActiveUser(){
        LoginScreenController.activeUser = null;
    }
    
    public static User getActiveUser(){
        return LoginScreenController.activeUser;
    }

 
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception{

        String inputUser = txtUsername.getText().trim();
        String inputPass = txtPassword.getText().trim();
        boolean authenticatedUser = false;
        DBConnection.startConnection();       
        User currentUser;
        
        currentUser = UserDataAccess.getByUsername(txtUsername.getText().trim());        
        authenticatedUser = (inputUser.equals(currentUser.getUsername()) && inputPass.equals(currentUser.getPassword()));
        
        System.out.println(authenticatedUser);
        
        if(!authenticatedUser){
            label.setText("Login Unsuccessful.\nPlease try again.");
        }else{
            setActiveUser(currentUser);
            stage = KHamC195.getMainStage();
            label.setText("Login Successful!");
            Parent root = FXMLLoader.load(getClass().getResource("UserScreen.fxml"));
        
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
