/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DataModels.Appointment;
import Utilities.ActivityLog;
import Utilities.DateTimeManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kenneth Ham
 */
public class ManageAppointmentScreenController implements Initializable {

    @FXML
    private TableView<Appointment> tableManageAppointments;
    @FXML
    private Label txtHeader;
    @FXML
    private TableColumn<Appointment, Integer> colAppointmentId;
    @FXML
    private TableColumn<Appointment, String> colCustomerName;
    @FXML
    private TableColumn<Appointment, String> colUserName;
    private TableColumn<Appointment, String> colAppointmentLocation;
    @FXML
    private TableColumn<Appointment, String> colAppointmentStartTime;
    @FXML
    private TableColumn<Appointment, String> colAppointmentDuration;
    @FXML
    private TableColumn<Appointment, String> colDateLastUpdated;
    @FXML
    private ToggleGroup apptViewRange;
    @FXML
    private RadioButton btnAppointmentViewAll;
    @FXML
    private RadioButton btnAppointmentViewWeek;
    @FXML
    private RadioButton btnAppointmentViewMonth;
    @FXML
    private Button btnAddAppointment;
    @FXML
    private Button btnEditAppointment;
    @FXML
    private Button btnDeleteAppointment;
    @FXML
    private Button btnCancelManageAppointments;
    @FXML
    private TableColumn<Appointment, String> colAppointmentType;
    
    Stage stage = ApplicationStateController.getMainStage();
    private String appointmentFilterType;
    private int appointmentFilterId;
    private int customerIdFilter;
    private ObservableList appointmentsViewAllByType;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableManageAppointments.setPlaceholder(new Label("There are no appointments that match the filter you have selected."));
    }

    // CONTROLLER METHODS
    
    public void setAppointmentsFilter(String apptType, int id){
  
        if(apptType.matches("all")){
            this.setAppointmentFilterType(apptType);
            this.setAppointmentFilterId(-1);
        } else if(apptType.matches("user")){
            this.setAppointmentFilterType(apptType);
            this.setAppointmentFilterId(id);
        }else if (apptType.matches("customer")){
            this.setAppointmentFilterType(apptType);
            this.setAppointmentFilterId(id);
        } else {
            System.out.println("appointment filter error!");
        }
        

        
        selectAppointmentTableDisplay();

    }
    
    public void selectAppointmentTableDisplay(){
        try {
            if(this.getAppointmentFilterType().matches("all")){
                getAllAppointmentData();
            } else if(this.getAppointmentFilterType().matches("user")){
                getAppointmentsByUserId(this.getAppointmentFilterId());
            }else if(this.getAppointmentFilterType().matches("customer")){
                getAppointmentsByCustomerId(this.getAppointmentFilterId());
            }else{
                System.out.println("error getting appointment data");
            }
                
        } catch (Exception ex) {
            Logger.getLogger(ManageUsersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getAllAppointmentData(){
        ObservableList<Appointment> appointments = DAO.AppointmentDataAccess.getAllAppointments();
        appointments.forEach((Appointment appointment) -> {

            int userId = appointment.getUserId();
            int customerId = appointment.getCustomerId();
            ZonedDateTime calStart = appointment.getStart().toLocalDateTime().atZone(ZoneId.of("UTC"));
            ZonedDateTime calEnd = appointment.getEnd().toLocalDateTime().atZone(ZoneId.of("UTC"));
            int apptDurationMinutes = (int)ChronoUnit.MINUTES.between(calStart.toInstant(), calEnd.toInstant());
            String apptDurationDisplay = String.valueOf(apptDurationMinutes);
            appointment.setDurationDisplay(apptDurationDisplay);
            
            String userName = DAO.UserDataAccess.getById(userId).getUsername();
            appointment.setUserNameDisplay(userName);
            String customerName = DAO.CustomerDataAccess.getById(customerId).getCustomerName();
            appointment.setCustomerNameDisplay(customerName);
            appointment.setStartTimeDisplay(calStart.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss"))); 
            appointment.setLastUpdateDisplay(appointment.getLastUpdate().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss")));
            
        });
        
        this.setAppointmentsViewAllByType(appointments);
        populateAppointmentList(appointments);
            
           
    }
    
    public void getAppointmentsByUserId(int id){
        ObservableList<Appointment> appointments = DAO.AppointmentDataAccess.getAllAppointments();
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        appointments.forEach((Appointment appointment) -> {
            int userId = DAO.UserDataAccess.getById(appointment.getUserId()).getUserId();
            if(userId == id){
                int customerId = appointment.getCustomerId();
                ZonedDateTime calStart = appointment.getStart().toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime calEnd = appointment.getEnd().toLocalDateTime().atZone(ZoneId.of("UTC"));
                int apptDurationMinutes = (int)ChronoUnit.MINUTES.between(calStart.toInstant(), calEnd.toInstant());
                String apptDurationDisplay = String.valueOf(apptDurationMinutes);
                appointment.setDurationDisplay(apptDurationDisplay);

                String userName = DAO.UserDataAccess.getById(userId).getUsername();
                appointment.setUserNameDisplay(userName);
                String customerName = DAO.CustomerDataAccess.getById(customerId).getCustomerName();
                appointment.setCustomerNameDisplay(customerName);
                appointment.setStartTimeDisplay(calStart.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-mm-dd kk:mm:ss")));
                appointment.setLastUpdateDisplay(appointment.getLastUpdate().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss")));
                userAppointments.add(appointment);
            }
            
            
        });
        txtHeader.setText("Manage User Appointments");
        this.setAppointmentsViewAllByType(userAppointments);
        populateAppointmentList(userAppointments);
        
    }
    
    public void getAppointmentsByCustomerId(int id){
        ObservableList<Appointment> appointments = DAO.AppointmentDataAccess.getAllAppointments();
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        appointments.forEach((Appointment appointment) -> {
            int customerId = DAO.CustomerDataAccess.getById(appointment.getCustomerId()).getCustomerId();
            if(customerId == id){
                int userId = appointment.getUserId();
                ZonedDateTime calStart = appointment.getStart().toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime calEnd = appointment.getEnd().toLocalDateTime().atZone(ZoneId.of("UTC"));
                int apptDurationMinutes = (int)ChronoUnit.MINUTES.between(calStart.toInstant(), calEnd.toInstant());
                String apptDurationDisplay = String.valueOf(apptDurationMinutes);
                appointment.setDurationDisplay(apptDurationDisplay);
                String userName = DAO.UserDataAccess.getById(userId).getUsername();
                appointment.setUserNameDisplay(userName);
                String customerName = DAO.CustomerDataAccess.getById(customerId).getCustomerName();
                appointment.setCustomerNameDisplay(customerName);
                appointment.setStartTimeDisplay(calStart.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-mm-dd kk:mm:ss")));
                appointment.setLastUpdateDisplay(appointment.getLastUpdate().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss")));
                customerAppointments.add(appointment);
            }
            
            
        });
        
        txtHeader.setText("Manage Customer Appointments");
        this.setAppointmentsViewAllByType(customerAppointments);
        populateAppointmentList(customerAppointments);
        
    }
    
    public void populateAppointmentList(ObservableList<Appointment> appointmentList){

        tableManageAppointments.setItems(appointmentList);
        
        colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerNameDisplay"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userNameDisplay"));
        colAppointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAppointmentStartTime.setCellValueFactory(new PropertyValueFactory<>("startTimeDisplay"));
        colAppointmentDuration.setCellValueFactory(new PropertyValueFactory<>("durationDisplay"));
        colDateLastUpdated.setCellValueFactory(new PropertyValueFactory<>("lastUpdateDisplay"));
        
    }


    @FXML
    private void handleBtnAppointmentViewAll(ActionEvent event) {
        ObservableList appointmentsViewByType = this.getAppointmentsViewAllByType();
        ZonedDateTime now = ZonedDateTime.now();
        FilteredList<Appointment> allAppointments = new FilteredList<>(this.getAppointmentsViewAllByType());
        tableManageAppointments.setItems(allAppointments);
    }

    @FXML
    private void handleBtnAppointmentViewWeek(ActionEvent event) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault());
        ObservableList<Appointment> appointments = this.getAppointmentsViewAllByType();
        FilteredList<Appointment> appointmentsForWeek = new FilteredList<>(appointments);
        
        // LAMBDA TO FILTER DATES WITHIN NEXT 7 DAYS
        appointmentsForWeek.setPredicate(appointment -> {
            ZonedDateTime start = appointment.getStart();
            int diffBetween = (int) Duration.between(now, appointment.getStart()).toDays();
            return start.isAfter(now) && diffBetween >=0 && diffBetween < 7;
        });
        
        tableManageAppointments.setItems(appointmentsForWeek);
    }

    @FXML
    private void handleBtnAppointmentViewMonth(ActionEvent event) {
        ZonedDateTime now = ZonedDateTime.now().withZoneSameInstant(ZoneId.systemDefault());
        ObservableList<Appointment> appointments = this.getAppointmentsViewAllByType();
        FilteredList<Appointment> appointmentsForMonth = new FilteredList<>(appointments);
        
        // LAMBDA TO FILTER DATES WITHIN 30 DAYS DURATION
        appointmentsForMonth.setPredicate((Appointment appointment) -> {
            ZonedDateTime start = appointment.getStart();
            int diffBetween = (int) Duration.between(now, appointment.getStart()).toDays();
            return start.isAfter(now) && diffBetween >=0 && diffBetween < 30;
        });
        
        tableManageAppointments.setItems(appointmentsForMonth);
    }

    @FXML
    private void handleAddAppointmentButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("NewAppointmentScreen.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void handleEditAppointmentButton(ActionEvent event) {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EditAppointmentScreen.fxml"));
            loader.load();

            EditAppointmentScreenController EASController = loader.getController();
            Appointment appointmentToEdit = tableManageAppointments.getSelectionModel().getSelectedItem();

            if(appointmentToEdit != null){
                EASController.setAppointmentToEdit(appointmentToEdit);
                EASController.setFilterType(this.getAppointmentFilterType());
                EASController.setFilterId(this.getAppointmentFilterId());

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Selection Error!");
                alert.setHeaderText("You must have an appointment selected before you can edit");
                alert.setContentText("Please click on an appointment in the list to select it, then click the Edit Appointment button again.");
                alert.showAndWait();
            }

        }catch(IOException ex){
            System.out.println(ex);
        }
    }

    @FXML
    private void handleDeleteAppointmentButton(ActionEvent event) {
        // TODO add logic for handling appointment deletion
        Appointment appointmentToDelete = tableManageAppointments.getSelectionModel().getSelectedItem();
        Alert deleteAlert = new Alert(AlertType.CONFIRMATION);
        deleteAlert.setHeaderText("Warning! Deleted Appointments Cannot Be Recovered");
        deleteAlert.setContentText("Are you sure you want to delete the appointment with " + appointmentToDelete.getCustomerNameDisplay() + " scheduled for " + appointmentToDelete.getStartTimeDisplay() +"?");
        
        Optional<ButtonType> response = deleteAlert.showAndWait();
        if(response.get() == ButtonType.OK){
            try{
               int appointmentId = appointmentToDelete.getAppointmentId();
               String queryBody = " from appointment where appointmentId = " + appointmentId;
               DAO.QueryManager.makeRequest("delete", queryBody);
            } catch(Exception ex){
                System.out.println(ex);
            }
        }   
         
        try (PrintWriter auditLog = ActivityLog.getAuditLog()) {
            Timestamp sqlNow = DateTimeManager.zdtLocalToTimestampSQL(ZonedDateTime.now());
            String zdtLocalDeletedStartTime = appointmentToDelete.getStart().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"));
            auditLog.append(sqlNow + "[UTC]: Appointment record for (" + appointmentToDelete.getUserNameDisplay() + " and " + appointmentToDelete.getCustomerNameDisplay() + ") scheduled for " + zdtLocalDeletedStartTime +" deleted by: " + ApplicationStateController.getActiveUser() + ".\n");
        } catch (Exception ex){
                System.out.println(ex);
        }
         
         setAppointmentsFilter(this.getAppointmentFilterType(), this.getAppointmentFilterId());
    }

    @FXML
    private void handleCancelManageAppointmentsButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UserScreen.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public String getAppointmentFilterType() {
        return appointmentFilterType;
    }

    public void setAppointmentFilterType(String appointmentFilterType) {
        this.appointmentFilterType = appointmentFilterType;
    }

    public int getAppointmentFilterId() {
        return appointmentFilterId;
    }

    public void setAppointmentFilterId(int appointmentFilterId) {
        this.appointmentFilterId = appointmentFilterId;
    }

    public int getCustomerIdFilter() {
        return customerIdFilter;
    }

    public void setCustomerIdFilter(int customerIdFilter) {
        this.customerIdFilter = customerIdFilter;
    }

    public ObservableList getAppointmentsViewAllByType() {
        return appointmentsViewAllByType;
    }

    public void setAppointmentsViewAllByType(ObservableList appointmentsViewAllByType) {
        this.appointmentsViewAllByType = appointmentsViewAllByType;
    }
    
}
