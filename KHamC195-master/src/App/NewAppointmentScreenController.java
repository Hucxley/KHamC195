/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import java.net.URL;
import java.util.ResourceBundle;
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
    private DatePicker datePickerAppointmentDate;
    @FXML
    private ComboBox<String> comboBoxStartTime;
    @FXML
    private Button btnAppointmentAdd;
    @FXML
    private ComboBox<Integer> comboBoxDuration;
    @FXML
    private ComboBox<String> comboBoxUserName;
    @FXML
    private Label txtHeader;
    @FXML
    private ComboBox<?> comboBoxCustomerName;
    @FXML
    private Button btnCancelNewAppointment;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleAppointmentDatePicker(ActionEvent event) {
    }

    @FXML
    private void handleStartTimeComboBox(ActionEvent event) {
    }

    @FXML
    private void handleAddAppointmentButton(ActionEvent event) {
    }


    @FXML
    private void handleAppointmentDurationComboBox(ActionEvent event) {
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
    
}
