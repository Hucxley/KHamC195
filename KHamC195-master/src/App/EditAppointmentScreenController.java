/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DataModels.Appointment;
import DataModels.Customer;
import DataModels.User;
import Utilities.ActivityLog;
import Utilities.DateTimeManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.stage.Stage;
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
    private ComboBox<String> comboBoxAppointmentType;
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
    private Customer selectedCustomerFromList;
    private User selectedUserFromList;
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
        ObservableList<Customer> customersList = DAO.CustomerDataAccess.getCustomers();
        ObservableList<String> appointmentTypes = ApplicationStateController.getAllowedAppointmentTypes();
        int appointmentUserId = appointment.getUserId();
        int appointmentCustomerId = appointment.getCustomerId();
        
        
        txtAppointmentTitle.setText(appointment.getTitle());
        txtAppointmentDescription.setText(appointment.getDescription());
        txtAppointmentLocation.setText(appointment.getLocation());
        txtAppointmentContact.setText(appointment.getContact());
        txtAppointmentURL.setText(appointment.getUrl());
        datePickerAppointmentStartDate.setValue(appointmentStartDate);
        datePickerAppointmentEndDate.setValue(appointmentEndDate);
        comboBoxStartTime.getItems().addAll(ApplicationStateController.getAllowedAppointmentTimes());
        comboBoxEndTime.getItems().addAll(ApplicationStateController.getAllowedAppointmentTimes());
        comboBoxAppointmentType.getItems().addAll(appointmentTypes);
        
        // Get index of appointment time in comboBox item list to set selected times on view load
        int appointmentStartTimeIndex = comboBoxStartTime.getItems().indexOf(appointmentStartTime.toString());
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
            if(customer.getActive()){
                this.getCustomerListItems().add(customer);
                comboBoxCustomerName.getItems().add(customer.getCustomerName());
                if(customer.getCustomerId() == appointmentCustomerId){
                    this.customerFoundInList = customer;
                    this.selectedCustomerFromList = customer;
                }
            }

        });
        
        comboBoxUserName.getSelectionModel().select(userFoundInList.getUsername());
        comboBoxCustomerName.getSelectionModel().select(customerFoundInList.getCustomerName());
        comboBoxAppointmentType.getSelectionModel().select(appointment.getType());
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
        boolean validAppointmentEdit = true;
        Appointment editedAppointment = this.apptToEdit;
        User selectedUser = selectedUserFromList;
        Customer selectedCustomer = selectedCustomerFromList;
        int selectedUserId = selectedUser.getUserId();
        int originalUserId = editedAppointment.getUserId();
        int selectedCustomerId = selectedCustomer.getCustomerId();
        int originalCustomerId = editedAppointment.getCustomerId();
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
            validAppointmentEdit = false;
            alert.setHeaderText("Appointment Start/End Error");
            alert.setContentText("The start date and time must be BEFORE the end date and time of the appointment.");
            alert.showAndWait();

        };
        // QUERY and HANDLE appointments for user or customer that overlap with the selected start and end times
        // SET validAppointmentEdit = false to block appointment creation and prompt user to change start/end time to avoid conflict
        try{
            System.out.println(editedAppointment.getAppointmentId());
            String appointmentOverlapQuery = " * from appointment where ('" +sqlStartTime + "' between start and end) OR ('";
            appointmentOverlapQuery += sqlEndTime +"' between start and end) OR ";
            appointmentOverlapQuery += "(start between '" + sqlStartTime + "' and '" + sqlEndTime + "') OR ";
            appointmentOverlapQuery += "(end between '" + sqlStartTime + "' and '" + sqlEndTime + "') AND ";
            appointmentOverlapQuery += "(userId != " +selectedUserId + " OR customerId != " + selectedCustomerId +") AND ";
            appointmentOverlapQuery += "appointmentId <> " + editedAppointment.getAppointmentId();
            System.out.println(appointmentOverlapQuery);
            DAO.QueryManager.makeRequest("select", appointmentOverlapQuery);
            ResultSet response = DAO.QueryManager.getResults();
            if(response.next()){
                if(response.getInt("appointmentId") != editedAppointment.getAppointmentId()){
                    validAppointmentEdit = false;
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
            }
        } catch(SQLException ex){
            System.out.println(ex);
        }
        
        // TODO: ADD ADDITIONAL VALIDATION RULES HERE 
        if(validAppointmentEdit){
            try{
                String queryBody = " appointment SET ";
                queryBody += "userId = " + selectedUserId +", ";
                queryBody += "customerId = " + selectedCustomerId + ", ";
                queryBody += "type = '" + type + "', ";
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
            
            try (PrintWriter auditLog = ActivityLog.getAuditLog()) {
                auditLog.append(sqlNow + "[UTC]: Appointment record for (" + selectedUser.getUsername() + " and " + selectedCustomer.getCustomerName() + ") on " +calStart.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-mm-dd kk:mm")) + " edited by: " + ApplicationStateController.getActiveUser() + ".\n");
            } catch (Exception ex){
                System.out.println(ex);
            }
            
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ManageAppointmentScreen.fxml"));
            loader.load();

            ManageAppointmentScreenController MASController = loader.getController();
            String MASFilterType = this.getFilterType();
            int MASFilterId = this.getFilterId();
            MASController.setAppointmentsFilter(MASFilterType, MASFilterId);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
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

    @FXML
    private void handleAppointmentTypeComboBox(ActionEvent event) {
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
    
}
