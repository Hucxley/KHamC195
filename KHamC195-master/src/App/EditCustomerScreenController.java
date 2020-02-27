/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DataModels.Customer;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kenneth Ham
 */
public class EditCustomerScreenController implements Initializable {

    @FXML
    private TextField txtCustomerID;
    @FXML
    private CheckBox checkBoxCustomerActive;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtCustomerPhone;
    @FXML
    private TextField txtAddress1;
    @FXML
    private TextField txtAddress2;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtPostalCode;
    @FXML
    private Button btnSaveCustomerChanges;
    @FXML
    private Button btnCancelEditCustomer;
    @FXML
    private TextField txtCountryName;
    @FXML
    private Label txtHeader;
    
    private Stage stage = ApplicationStateController.getMainStage();
    private Customer selectedCustomer = ApplicationStateController.getSelectedCustomer();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleCustomerActiveToggle(ActionEvent event) {
        
        if(selectedCustomer != null){
            selectedCustomer.setActive(!selectedCustomer.getActive());
            this.checkBoxCustomerActive.setSelected(selectedCustomer.getActive());
        }
    }

    @FXML
    private void handleSaveCustomerChangesButton(ActionEvent event) {
        
        //TODO: add logic to update customer record in db
        
    }

    @FXML
    private void handleCancelEditCustomerButton(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("ManageCustomersScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
}
