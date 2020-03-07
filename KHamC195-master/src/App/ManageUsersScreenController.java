/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DAO.QueryManager;
import DataModels.User;
import Utilities.DateTimeManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author k16080h
 */
public class ManageUsersScreenController implements Initializable {

    @FXML
    private Label txtHeader;
    @FXML
    private TableView<User> tableManageUsers;
    @FXML
    private TableColumn<User, Integer> colUserId;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, Boolean> colActiveUser;
    @FXML
    private TableColumn<User, String> colDateCreated;
    @FXML
    private TableColumn<User, String> colCreatedBy;
    @FXML
    private TableColumn<User, String> colDateLastUpdated;
    @FXML
    private TableColumn<User, String> colLastUpdatedBy;
    @FXML
    private Button btnAddUser;
    @FXML
    private Button btnEditUser;
    @FXML
    private Button btnDeleteUser;
    
    // State Manager Imports
    Stage stage = ApplicationStateController.getMainStage();
    @FXML
    private Button btnViewUserAppointments;
    @FXML
    private Button btnCancelManageUsers1;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getAllUserData();
        } catch (SQLException ex) {
            Logger.getLogger(ManageUsersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ManageUsersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Instance Methods
    
    public void getAllUserData() throws SQLException, ParseException{
        ObservableList <User> userList = FXCollections.observableArrayList();
        ResultSet response = QueryManager.getDataFromTable("user");
        while(response.next()){
            int userId = response.getInt("userId");
            String userName = response.getString("userName");
            String password = response.getString("password");
            boolean active = response.getBoolean("active");
            ZonedDateTime createdDate = DateTimeManager.dateStringToLocalZDT(response.getString("createDate"));
            String createdBy = response.getString("createdBy");
            ZonedDateTime lastUpdated = DateTimeManager.dateStringToLocalZDT(response.getString("lastUpdate"));
            String lastUpdatedBy = response.getString("lastUpdateBy");
            User nextUser = new User(userId, userName, password, active, createdDate, createdBy, lastUpdated, lastUpdatedBy);
            userList.add(nextUser);
        };
        userList.forEach(user -> {
            tableManageUsers.getItems().add(user);
        });
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colActiveUser.setCellValueFactory(new PropertyValueFactory<>("active"));
        colDateCreated.setCellValueFactory(new PropertyValueFactory<>("createdDateDisplay"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colDateLastUpdated.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedDisplay"));
        colLastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        
    }

    @FXML
    private void handleAddUserButton(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("NewUserScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        
    }
    
    @FXML
    private void handleEditUserButton(ActionEvent event) {
    }

    @FXML
    private void handleDeleteUserButton(ActionEvent event) {
    }


    @FXML
    private void handleCancelManageUsersButton(ActionEvent event) throws IOException {
        

        Parent root = FXMLLoader.load(getClass().getResource("UserScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void handleViewUserAppointmentsButton(ActionEvent event) {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ManageAppointmentScreen.fxml"));
            loader.load();

            ManageAppointmentScreenController MASController = loader.getController();
            MASController.setAppointmentsFilter("user", tableManageUsers.getSelectionModel().getSelectedItem().getUserId());

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }catch(IOException ex){
            System.out.println(ex);
        }
    }


    
}
