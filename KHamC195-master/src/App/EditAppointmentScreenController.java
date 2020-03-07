/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DataModels.Appointment;
import DataModels.Customer;
import DataModels.User;
import Utilities.DateTimeManager;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Kenneth Ham
 */
public class EditAppointmentScreenController implements Initializable {

    @FXML
    private ComboBox<String> comboBoxUserName;
    @FXML
    private ComboBox<String> comboBoxCustomerName;
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
    private DatePicker datePickerAppointmentStartDate;
    @FXML
    private ComboBox<String> comboBoxStartTime;
    @FXML
    private DatePicker datePickerAppointmentEndDate;
    @FXML
    private ComboBox<String> comboBoxEndTime;
    @FXML
    private Label txtHeader;
    @FXML
    private Button btnEditAppointmentSave;
    @FXML
    private Button btnCancelEditAppointment;
    
    private User userFoundInList;
    private Customer customerFoundInList;
    private Appointment apptToEdit;
    Stage stage = ApplicationStateController.getMainStage();
    private ObservableList<Customer> customerListItems = FXCollections.observableArrayList();
    private ObservableList<User> userListItems = FXCollections.observableArrayList();
    Customer selectedCustomerFromList;
    User selectedUserFromList;
    private String filterType;
    private int filterId;





    


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
    // CLASS METHODS
    
    public void setAppointmentToEdit(Appointment appointment){
        this.setApptToEdit(appointment);
        populateEditAppointmentScreen(appointment);
        
    }
    
    private void populateEditAppointmentScreen(Appointment appointment){
        LocalDate appointmentStartDate = LocalDate.parse(DateTimeManager.toDateString(appointment.getStart()));
        LocalTime appointmentStartTime = LocalTime.parse(DateTimeManager.toTimeString(appointment.getStart()));
        LocalDate appointmentEndDate = LocalDate.parse(DateTimeManager.toDateString(appointment.getEnd()));
        LocalTime appointmentEndTime = LocalTime.parse(DateTimeManager.toTimeString(appointment.getEnd()));
        ObservableList<User> usersList = DAO.UserDataAccess.getUsersList();
        System.out.println(usersList.size());
        ObservableList<Customer> customersList = DAO.CustomerDataAccess.getCustomers();
        System.out.println(usersList.size());
        int appointmentUserId = appointment.getUserId();
        int appointmentCustomerId = appointment.getCustomerId();
        
        
        txtAppointmentTitle.setText(appointment.getType());
        txtAppointmentDescription.setText(appointment.getDescription());
        txtAppointmentLocation.setText(appointment.getLocation());
        txtAppointmentContact.setText(appointment.getContact());
        txtAppointmentURL.setText(appointment.getUrl());
        datePickerAppointmentStartDate.setValue(appointmentStartDate);
        datePickerAppointmentEndDate.setValue(appointmentEndDate);
        comboBoxStartTime.getItems().addAll(ApplicationStateController.getAllowedAppointmentTimes());
        comboBoxEndTime.getItems().addAll(ApplicationStateController.getAllowedAppointmentTimes());
        
        // Get index of appointment time in comboBox item list to set selected times on view load
        System.out.println(appointmentStartTime.toString());
        int appointmentStartTimeIndex = comboBoxStartTime.getItems().indexOf(appointmentStartTime.toString());
        System.out.println(comboBoxStartTime.getItems());
        int appointmentEndTimeIndex = comboBoxEndTime.getItems().indexOf(appointmentEndTime.toString());
        
        // Lambda to compare ids in userList to match the userId for the appointment to set selected on view load
        usersList.forEach(user -> {
            this.getUserListItems().add(user);
            comboBoxUserName.getItems().add(user.getUsername());
            if(user.getUserId() == appointmentUserId){
                this.userFoundInList = user;
                this.selectedUserFromList = user;
            }
        });
        
        // Lambda to compare ids in custmerList to match the customerId for the appointment to set selected on view load
        customersList.forEach(customer -> {
            this.getCustomerListItems().add(customer);
            comboBoxCustomerName.getItems().add(customer.getCustomerName());
            if(customer.getCustomerId() == appointmentCustomerId){
                this.customerFoundInList = customer;
                this.selectedCustomerFromList = customer;
            }
        });
        
        comboBoxUserName.getSelectionModel().select(userFoundInList.getUsername());
        comboBoxCustomerName.getSelectionModel().select(customerFoundInList.getCustomerName());
        
        comboBoxStartTime.getSelectionModel().select(appointmentStartTimeIndex);
        comboBoxEndTime.getSelectionModel().select(appointmentEndTimeIndex);
        
    }


    @FXML
    private void handleStartTimeComboBox(ActionEvent event) {
        
    }



    @FXML
    private void handleAppointmentUserNameComboBox(ActionEvent event) {
        System.out.println(event.getEventType());
        String selectedUserName = comboBoxUserName.getValue();
        System.out.println(selectedUserName);
        
        this.userListItems.forEach(user -> {
            if(user.getUsername().matches(selectedUserName)){
                this.selectedUserFromList = user;
            }
        });
 
    }

    @FXML
    private void handleEditAppointmentSaveButton(ActionEvent event) throws ParseException, IOException {

        String currentActiveUsername = ApplicationStateController.getActiveUser().getUsername();
        boolean validAppointmentEdit = false;
        Appointment editedAppointment = this.getApptToEdit();
        User selectedUser = selectedUserFromList;
        Customer selectedCustomer = selectedCustomerFromList;
        int selectedUserId = selectedUser.getUserId();
        int originalUserId = editedAppointment.getUserId();
        int selectedCustomerId = selectedCustomer.getCustomerId();
        int originalCustomerId = editedAppointment.getCustomerId();
        String title = txtAppointmentTitle.getText();
        String description = txtAppointmentDescription.getText();
        String location = txtAppointmentLocation.getText();
        String contact = txtAppointmentContact.getText();
        String url = txtAppointmentURL.getText();
        String strStartDate = datePickerAppointmentStartDate.getValue().toString();
        String strEndDate = datePickerAppointmentEndDate.getValue().toString();
        String strStartTime = comboBoxStartTime.getSelectionModel().getSelectedItem();
        String strEndTime = comboBoxEndTime.getSelectionModel().getSelectedItem();
        String strNewStartDateTime = strStartDate + " " + strStartTime + ":00.0";
        String strNewEndDateTime = strEndDate + " " + strEndTime + ":00.0";
        ZonedDateTime calStart = DateTimeManager.dateStringToLocalZDT(strNewStartDateTime);
        ZonedDateTime calEnd = DateTimeManager.dateStringToLocalZDT(strNewEndDateTime);
        ZonedDateTime zdtSQLNow = ZonedDateTime.now();
        
        Timestamp sqlStartTime = DateTimeManager.zdtLocalToTimestampSQL(calStart);
        Timestamp sqlEndTime = DateTimeManager.zdtLocalToTimestampSQL(calEnd);
        Timestamp sqlNow = DateTimeManager.zdtLocalToTimestampSQL(zdtSQLNow);
        
        if(calEnd.isBefore(calStart)){
            // TODO FIRE WARNING THAT END TIME MUST BE AFTER START TIME
        }
        
        if(selectedCustomerId != originalCustomerId){
            // TODO: ALERT TO CREATE NEW APPOINTMENT ON CONFIRMATION
        }
        
        if(selectedUserId != originalUserId){
            // TODO: ALERT TO CONFIRM CHANGE OF USER FOR THE APPOINTMENT
            
            
        } 
        
        // TODO: ADD ADDITIONAL VALIDATION RULES HERE 
        
        validAppointmentEdit = true;
        if(validAppointmentEdit){
            try{
                String queryBody = " appointment SET ";
                queryBody += "userId = " + selectedUserId +", ";
                queryBody += "customerId = " + selectedCustomerId + ", ";
                queryBody += "title = '" + title + "', ";
                queryBody += "description = '" + description + "', ";
                queryBody += "location = '" + location + "', ";
                queryBody += "contact = '" + contact + "', ";
                queryBody += "url = '" + url + "', ";
                queryBody += "start = '" + sqlStartTime + "', ";
                queryBody += "end = '" + sqlEndTime + "', ";
                queryBody += "lastUpdate = '" + sqlNow + "', ";
                queryBody += "lastUpdateBy = '" +currentActiveUsername + "'";
                queryBody += "where appointmentId = " + editedAppointment.getAppointmentId(); 
                
                DAO.QueryManager.makeRequest("update", queryBody);
            
            } catch(SQLException ex){
                System.out.println(ex);
            }
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ManageAppointmentScreen.fxml"));
            loader.load();

            ManageAppointmentScreenController MASController = loader.getController();
            String MASFilterType = this.getFilterType();
            int MASFilterId = this.getFilterId();
            MASController.setAppointmentsFilter(MASFilterType, MASFilterId);

            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

            
        }

    }

    @FXML
    private void handleCancelEditAppointmentButton(ActionEvent event) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ManageAppointmentScreen.fxml"));
        loader.load();

        ManageAppointmentScreenController MASController = loader.getController();
        String MASFilterType = this.getFilterType();
        int MASFilterId = this.getFilterId();
        MASController.setAppointmentsFilter(MASFilterType, MASFilterId);

        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void handleAppointmentCustomerNameComboBox(ActionEvent event) {
        System.out.println(event.getEventType());
        String selectedCustomerName = comboBoxCustomerName.getSelectionModel().getSelectedItem();
       
        // Lambda to compare selected item in custmerList to determine if changed, set if changed
        this.customerListItems.forEach(customer -> {
            if(customer.getCustomerName().matches(selectedCustomerName)){
                this.selectedCustomerFromList = customer;
            }
        });
    }

    @FXML
    private void handleAppointmentEndDatePicker(ActionEvent event) {
    }

    @FXML
    private void handleEndTimeComboBox(ActionEvent event) {
    }

    @FXML
    private void handleAppointmentStartDatePicker(ActionEvent event) {
    }

    public Appointment getApptToEdit() {
        return apptToEdit;
    }

    public void setApptToEdit(Appointment apptToEdit) {
        this.apptToEdit = apptToEdit;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public ObservableList<Customer> getCustomerListItems() {
        return customerListItems;
    }

    public void setCustomerListItems(ObservableList<Customer> customerListItems) {
        this.customerListItems = customerListItems;
    }

    public ObservableList<User> getUserListItems() {
        return userListItems;
    }

    public void setUserListItems(ObservableList<User> userListItems) {
        this.userListItems = userListItems;
    }
    
}
