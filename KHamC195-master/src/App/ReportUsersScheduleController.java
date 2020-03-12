/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DataModels.Appointment;
import DataModels.User;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kenneth Ham
 */
public class ReportUsersScheduleController implements Initializable {

    @FXML
    private Label txtHeader;
    @FXML
    private ComboBox<User> comboBoxUserName;
    @FXML
    private Button btnReportViewBack;
    @FXML
    private ListView<String> listViewScheduleReport;
    @FXML
    private TextArea txtAreaAppointmentDetails;
    @FXML
    private Label labelSearchError;
    
    private User selectedUser = null;
    private ObservableList<User> users = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private Appointment selectedAppointment = null;
    private ObservableList<String> appointmentSummaries = FXCollections.observableArrayList();
    @FXML
    private VBox vBoxAppointmentListContainer;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateUserSelectorList();
        labelSearchError.setVisible(false);
    }    

    @FXML
    private void handleComboBoxUserName(ActionEvent event) {

        int userSelectedIndex = comboBoxUserName.getSelectionModel().getSelectedIndex();
        User userSelected = users.get(userSelectedIndex);
        this.setSelectedUser(userSelected);
        System.out.println(userSelected.getUsername());

        getAppointmentsForUser(userSelected);
    }

    private void populateListAppointmentListView(User selectedUser) {
        
    }
    
    private void populateDetailsTextBox(Appointment selectedAppointment) {
        System.out.println(selectedAppointment.getAppointmentId());
        txtAreaAppointmentDetails.setWrapText(true);
        txtAreaAppointmentDetails.clear();
        List<String> detailsList = new ArrayList(); 
        Appointment appt = selectedAppointment;
        TextArea details = txtAreaAppointmentDetails;
        String strFullDateTime = appt.getStart().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("EEEE', 'MMMM dd', ' yyyy', 'hh:mm a z"));
        String apptSummary = appt.getDurationDisplay() + " minute appointment with " + appt.getCustomerNameDisplay();
        String type = "Meeting Type: " + appt.getType();
        String title = "Meeting title: " + appt.getTitle();
        String description = "Description/Details: " + appt.getDescription();
        String url = "Link to join meeting: " + appt.getUrl();
        String location = "Meeting Room/Location: " + appt.getLocation();
        String contact = "Contact Info: " + appt.getContact();
        String lastUpdateZDT = "Last updated: " +appt.getLastUpdate().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
        
        detailsList.add(strFullDateTime);
        detailsList.add(apptSummary); 
        detailsList.add(title);
        detailsList.add(description);
        detailsList.add(url);
        detailsList.add(location);
        detailsList.add(contact);
        detailsList.add(lastUpdateZDT);

        // append text and add new line separator in the appointment details text area
        detailsList.forEach(item -> {
            details.appendText(item + "\n");
       });
    }
       
    
    private void getAppointmentsForUser(User userSelected){
        this.getAppointments().clear();
        this.getAppointmentSummaries().clear();
        listViewScheduleReport.getItems().clear();
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        LocalDate eom = today.withDayOfMonth(today.lengthOfMonth());
        String eomString = eom.toString() + "T23:59:59.0";
        System.out.println(now);
        System.out.println(eomString);
        
        String queryBody = "* from appointment where userId = " + userSelected.getUserId() + " AND ";
        queryBody += "start between '" + now + "' AND '" + eomString +"'";
        
        System.out.println(queryBody);
        try{
            DAO.QueryManager.makeRequest("select", queryBody);
            ResultSet response = DAO.QueryManager.getResults();
            while(response.next()){
                Appointment foundAppointment = DAO.AppointmentDataAccess.getByAppointmentId(response.getInt("appointmentId"));
                int userId = foundAppointment.getUserId();
                int customerId = foundAppointment.getCustomerId();
                ZonedDateTime calStart = foundAppointment.getStart().toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime calEnd = foundAppointment.getEnd().toLocalDateTime().atZone(ZoneId.of("UTC"));
                int apptDurationMinutes = (int)ChronoUnit.MINUTES.between(calStart.toInstant(), calEnd.toInstant());
                String apptDurationDisplay = String.valueOf(apptDurationMinutes);
                foundAppointment.setDurationDisplay(apptDurationDisplay);

                String userName = DAO.UserDataAccess.getById(userId).getUsername();
                foundAppointment.setUserNameDisplay(userName);
                String customerName = DAO.CustomerDataAccess.getById(customerId).getCustomerName();
                foundAppointment.setCustomerNameDisplay(customerName);
                foundAppointment.setStartTimeDisplay(calStart.withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MM-dd-yyyy kk:mm:ss"))); 
                foundAppointment.setLastUpdateDisplay(foundAppointment.getLastUpdate().withZoneSameInstant(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss")));
                appointments.add(foundAppointment);
                appointmentSummaries.add(foundAppointment.getStartTimeDisplay() + ": " + foundAppointment.getCustomerNameDisplay() + " - " + foundAppointment.getDurationDisplay() + " minutes");
            }
            
            listViewScheduleReport.getItems().clear();
            listViewScheduleReport.getItems().setAll(appointmentSummaries);
            
            if(this.getAppointments().isEmpty()){
                labelSearchError.setVisible(true);
            }else{
                labelSearchError.setVisible(false);
            }
        } catch(SQLException ex){
            System.out.println(ex);
        }
        
    }


    @FXML
    private void handleReportViewBackButton(ActionEvent event) throws IOException {
        Stage stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ReportSelectionScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
    @FXML
    private void handleAppointmentListViewMouseClick(MouseEvent event) {
        ObservableList<Appointment> appointments = this.getAppointments();
        Appointment foundAppointment = null;
        int selectedAppointmentIndex = listViewScheduleReport.getSelectionModel().getSelectedIndex();
        if(selectedAppointmentIndex == -1){
           event.consume();
            
        }else{
            foundAppointment = appointments.get(selectedAppointmentIndex);
            this.setSelectedAppointment(foundAppointment);
            System.out.println(foundAppointment);
            populateDetailsTextBox(foundAppointment);            
        }

    }

    @FXML
    private void handleAppointmentListViewMouseEnter(MouseEvent event) {

    }
    
    private void populateUserSelectorList(){
        ObservableList<User> usersList = DAO.UserDataAccess.getUsersList();
        this.setUsers(usersList);
        comboBoxUserName.getItems().addAll(usersList);
    }


    public ObservableList<User> getUsers() {
        return users;
    }

    public void setUsers(ObservableList<User> users) {
        this.users = users;
    }
    
    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public ObservableList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ObservableList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Appointment getSelectedAppointment() {
        return selectedAppointment;
    }

    public void setSelectedAppointment(Appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    public ObservableList<String> getAppointmentSummaries() {
        return appointmentSummaries;
    }

    public void setAppointmentSummaries(ObservableList<String> appointmentSummaries) {
        this.appointmentSummaries = appointmentSummaries;
    }





    
}
