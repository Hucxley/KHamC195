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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    
    Stage stage = ApplicationStateController.getMainStage();
    private Customer selectedCustomerFromList;
    private User selectedUserFromList;
    private ObservableList<User> userListItems = FXCollections.observableArrayList();
    private ObservableList<Customer> customerListItems = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> comboBoxAppointmentType;


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
        ObservableList<String> appointmentTypes = ApplicationStateController.getAllowedAppointmentTypes();
        
        // Lambda to populate user & customer drop down boxes and selection lists
        usersList.forEach(user -> {

            this.getUserListItems().add(user);
            comboBoxUserName.getItems().add(user);
        });
        
        // Lambda to compare ids in custmerList to match the customerId for the appointment to set selected on view load
        // Limits available customer selection to active customers only 
        customersList.forEach(customer -> {
            if(customer.getActive()){
                this.getCustomerListItems().add(customer);
                comboBoxCustomerName.getItems().add(customer);
            }

        });
        
        comboBoxStartTime.getItems().addAll(ApplicationStateController.getAllowedAppointmentTimes());
        comboBoxEndTime.getItems().addAll(ApplicationStateController.getAllowedAppointmentTimes());
        comboBoxAppointmentType.getItems().addAll(appointmentTypes);
    }

    

    @FXML
    private void handleStartTimeComboBox(ActionEvent event) {
    }

    @FXML
    private void handleAddAppointmentButton(ActionEvent event) throws IOException, ParseException {

        String currentActiveUsername = ApplicationStateController.getActiveUser().getUsername();
        boolean validAppointment = true;
        User selectedUser = this.getSelectedUserFromList();
        Customer selectedCustomer = this.getSelectedCustomerFromList();
        int selectedUserId = selectedUser.getUserId();
        int selectedCustomerId = selectedCustomer.getCustomerId();
        String type = comboBoxAppointmentType.getValue();
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
        
        Alert alert = new Alert(AlertType.ERROR);
        
        if(!calEnd.isAfter(calStart)){
            validAppointment = false;
            // TODO FIRE WARNING THAT END TIME MUST BE AFTER START TIME
            alert.setHeaderText("Appointment Start/End Error");
            alert.setContentText("The start date and time must be BEFORE the end date and time of the appointment.");
            alert.showAndWait();
        }
        
        // QUERY and HANDLE appointments for user or customer that overlap with the selected start and end times
        // SET validAppointment = false to block appointment creation and prompt user to change start/end time to avoid conflict
        try{
            String appointmentOverlapQuery = " * from appointment where ('" +sqlStartTime + "' between start and end) OR ('";
            appointmentOverlapQuery += sqlEndTime +"' between start and end) OR ";
            appointmentOverlapQuery += "(start between '" + sqlStartTime + "' and '" + sqlEndTime + "') OR ";
            appointmentOverlapQuery += "(end between '" + sqlStartTime + "' and '" + sqlEndTime + "') AND ";
            appointmentOverlapQuery += "(userId = " +selectedUserId +" OR customerId = " + selectedCustomerId + ")";

            DAO.QueryManager.makeRequest("select", appointmentOverlapQuery);
            ResultSet response = DAO.QueryManager.getResults();
            if(response.next()){
                validAppointment = false;
                // An overlapping appointment is found for user or customer show alert
                int conflictUserId = response.getInt("userId");
                int conflictCustomerId = response.getInt("customerId");
                // determine if conflict is with user or customer and change message to guide correction
                if(conflictUserId == selectedUser.getUserId()){
                    alert.setTitle(selectedUser.getUsername() + " Has a Conflict!");
                }else if(conflictCustomerId == selectedCustomer.getCustomerId()){
                    alert.setTitle(selectedCustomer.getCustomerName() + " Has a Conflict!");
                }else{
                    alert.setTitle("There may be a schedule conflict");
                }
                
                String conflictingStartTime = DateTimeManager.convertToZDT(response.getTimestamp("start")).withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("hh:mm a"));
                String conflictingEndTime = DateTimeManager.convertToZDT(response.getTimestamp("end")).withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("hh:mm a"));
                alert.setHeaderText("There is an appointment that conflicts with the selected time");
                alert.setContentText("The conflicting appointment begins at " + conflictingStartTime + " and ends at " +conflictingEndTime +". Please change your selection to avoid overlapping with this appointment.");
                alert.showAndWait();
            }
        } catch(SQLException ex){
            System.out.println(ex);
        }

        
        // TODO: ADD ADDITIONAL VALIDATION RULES HERE 
        
        
        if(validAppointment){
            try{
                String queryBody = " into appointment (userId, customerId, type, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) values (";
                queryBody += selectedUserId + ", ";
                queryBody += selectedCustomerId + ", '";
                queryBody += type + "', '";
                queryBody += title + "', '";
                queryBody += description + "', '";
                queryBody += location + "', '";
                queryBody += contact + "', '";
                queryBody += url + "', '";
                queryBody += sqlStartTime + "', '";
                queryBody += sqlEndTime + "', '";
                queryBody += sqlNow + "', '" + currentActiveUsername + "', '";
                queryBody += sqlNow + "', '";
                queryBody += currentActiveUsername + "')"; 
                
                DAO.QueryManager.makeRequest("insert", queryBody);
            
            } catch(SQLException ex){
                System.out.println(ex);
            }
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ManageAppointmentScreen.fxml"));
            loader.load();

            ManageAppointmentScreenController MASController = loader.getController();
            String MASFilterType = "all";
            int MASFilterId = -1;
            MASController.setAppointmentsFilter(MASFilterType, MASFilterId);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
  
        }
}

    @FXML
    private void handleAppointmentUserNameComboBox(ActionEvent event) {
        int selectedUserIndex = comboBoxUserName.getSelectionModel().getSelectedIndex();
        User userSelectedFromList = this.getUserListItems().get(selectedUserIndex);
        this.setSelectedUserFromList(userSelectedFromList);
    }

    @FXML
    private void handleAppointmentCustomerNameComboBox(ActionEvent event) {
        int selectedCustomerIndex = comboBoxCustomerName.getSelectionModel().getSelectedIndex();
        Customer customerSelectedFromList = this.getCustomerListItems().get(selectedCustomerIndex);
        this.setSelectedCustomerFromList(customerSelectedFromList);
    }

    @FXML
    private void handleCancelNewAppointmentButton(ActionEvent event) throws ParseException, IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ManageAppointmentScreen.fxml"));
        loader.load();

        ManageAppointmentScreenController MASController = loader.getController();
        String MASFilterType = "all";
        int MASFilterId = -1;
        MASController.setAppointmentsFilter(MASFilterType, MASFilterId);

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
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

    public Customer getSelectedCustomerFromList() {
        return selectedCustomerFromList;
    }

    public void setSelectedCustomerFromList(Customer selectedCustomerFromList) {
        this.selectedCustomerFromList = selectedCustomerFromList;
    }

    public User getSelectedUserFromList() {
        return selectedUserFromList;
    }

    public void setSelectedUserFromList(User selectedUserFromList) {
        this.selectedUserFromList = selectedUserFromList;
    }

    public ObservableList<User> getUserListItems() {
        return userListItems;
    }

    public void setUserListItems(ObservableList<User> userListItems) {
        this.userListItems = userListItems;
    }

    public ObservableList<Customer> getCustomerListItems() {
        return customerListItems;
    }

    public void setCustomerListItems(ObservableList<Customer> customerListItems) {
        this.customerListItems = customerListItems;
    }

    @FXML
    private void handleAppointmentTypeComboBox(ActionEvent event) {
    }



    
}
