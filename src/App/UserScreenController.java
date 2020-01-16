/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DataModels.User;
import java.io.IOException;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author k16080h
 */
public class UserScreenController implements Initializable {

    @FXML
    private Label txtHeader;
    @FXML
    private Button btnViewAll;
    @FXML
    private Button btnAddNew;
    @FXML
    private Button btnLogoutUser;
    
    private static User activeUser; 
    private final String viewName = "Home Screen";
    
    /**
     * Initializes the controller class.
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setActiveUser();
        setHeaderText();
    }    

    @FXML
    private void handleViewAllButton(ActionEvent event) {
    }

    @FXML
    private void handleAddNewButton(ActionEvent event) {
    }

    @FXML
    private void handleLogoutUserButton(ActionEvent event) throws IOException {
        LoginScreenController.clearActiveUser();
        setActiveUser();
        Stage stage = KHamC195.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    
    public User getActiveUser() {
        return UserScreenController.activeUser;
    }

    private static void setActiveUser() {
        UserScreenController.activeUser = LoginScreenController.getActiveUser();
    }
   
    private void setHeaderText(){
        User currentUser = getActiveUser();
        txtHeader.setText(currentUser.getUsername() + "'s " + viewName);
    }
        
}
