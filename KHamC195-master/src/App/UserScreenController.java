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
    private Button btnViewAppointments;
    @FXML
    private Button btnLogoutUser;
    
    // Custom Instance Variables
    private static User activeUser; 
    @FXML
    private Button btnViewCustomers;
    @FXML
    private Button btnViewUsers;
    @FXML
    private Button btnViewReports;
    
    /**
     * Initializes the controller class.
    */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setActiveUser();
 
    }    


    @FXML
    private void handleLogoutUserButton(ActionEvent event) throws IOException {
        LoginScreenController.clearActiveUser();
        setActiveUser();
        Stage stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
    public User getActiveUser() {
        return UserScreenController.activeUser;
    }

    private static void setActiveUser() {
        if(UserScreenController.activeUser != null){
            UserScreenController.activeUser = LoginScreenController.getActiveUser();
        }
    }
   

    @FXML
    private void handleViewAppointmentsButton(ActionEvent event) throws IOException {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ManageAppointmentScreen.fxml"));
            loader.load();

            ManageAppointmentScreenController MASController = loader.getController();
            
            MASController.setAppointmentsFilter("all", -1);

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }catch(Exception ex){
            System.out.println(ex);
        }
    }


    private void handleUserAdminButton(ActionEvent event) throws IOException {
        Stage stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ManageUsersScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    @FXML
    private void handleViewCustomersButton(ActionEvent event) throws IOException {
        
        Stage stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ManageCustomersScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        
    }

    @FXML
    private void handleViewUsersButton(ActionEvent event) throws IOException {
        
        Stage stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ManageUsersScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        
    }

    @FXML
    private void handleViewReportsButton(ActionEvent event) throws IOException {
       
        Stage stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ReportSelectionScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

        
}
