/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DataModels.Customer;
import DataModels.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Kenneth Ham
 */
public class NewAppointmentScreenController implements Initializable {

    @FXML
    private TextField txtAppointmentTitle;
    @FXML
    private TextField txtAppointmentDescription;
    @FXML
    private TextField txtAppointmentLocation;
    @FXML
    private TextField txtAppointmentContact;
    @FXML
    private TextField txtAppointmentURL;
    @FXML
    private ComboBox<String> comboBoxStartTime;
    @FXML
    private DatePicker datePickerAppointmentStartDate;
    @FXML
    private DatePicker datePickerAppointmentEndDate;
    @FXML
    private ComboBox<String> comboBoxEndTime;
    @FXML
    private Button btnAppointmentAdd;
    @FXML
    private ComboBox<User> comboBoxUserName;
    @FXML
    private Label txtHeader;
    @FXML
    private ComboBox<Customer> comboBoxCustomerName;
    @FXML
    private Button btnCancelNewAppointment;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        populateComboBoxes();
    }   
    
    
    // INSTANCE METHODS 
        private void populateComboBoxes() {
        ObservableList<User> usersList = DAO.UserDataAccess.getUsersList();
        ObservableList<Customer> customersList = DAO.CustomerDataAccess.getCustomers();
        
 
        comboBoxStartTime.getItems().addAll(ApplicationStateController.getAllowedAppointmentTimes());
        comboBoxEndTime.getItems().addAll(ApplicationStateController.getAllowedAppointmentTimes());
        
        // Add User Names to ComboBox
        comboBoxUserName.getItems().addAll(usersList);
        comboBoxCustomerName.getItems().addAll(customersList);
    }

    

    @FXML
    private void handleStartTimeComboBox(ActionEvent event) {
    }

    @FXML
    private void handleAddAppointmentButton(ActionEvent event) {
    }



    @FXML
    private void handleAppointmentUserNameComboBox(ActionEvent event) {
    }

    @FXML
    private void handleAppointmentCustomerNameComboBox(ActionEvent event) {
    }

    @FXML
    private void handleCancelNewAppointmentButton(ActionEvent event) {
    }

    @FXML
    private void handleAppointmentStartDatePicker(ActionEvent event) {
    }

    @FXML
    private void handleAppointmentEndDatePicker(ActionEvent event) {
    }

    @FXML
    private void handleEndTimeComboBox(ActionEvent event) {
    }


    
}
