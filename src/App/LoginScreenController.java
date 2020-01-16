/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DAO.DBConnection;
import java.net.URL;
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
    
    private final String username = "test";
    private final String password = "test";
    private static String activeUser;
    
    private static void setActiveUser(String user){
        LoginScreenController.activeUser = user;
    }
    
    public static void clearActiveUser(){
        LoginScreenController.activeUser = null;
    }
    
    public static String getActiveUser(){
        return LoginScreenController.activeUser;
    }

 
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception{

        if(txtUsername.getText().equals(this.username) && txtPassword.getText().equals(this.password)){
            setActiveUser(txtUsername.getText().trim());
            stage = KHamC195.getMainStage();
            label.setText("Login Successful!");
            DBConnection.startConnection();
            Parent root = FXMLLoader.load(getClass().getResource("UserScreen.fxml"));
        
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        }else{
            label.setText("Login Unsuccessful.\nPlease try again.");
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
