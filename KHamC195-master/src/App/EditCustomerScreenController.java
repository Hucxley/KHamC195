/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DAO.QueryManager;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Customer selectedCustomer;
    private User currentUser = ApplicationStateController.getActiveUser();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setCustomerToEdit(Customer customer){
        this.selectedCustomer = customer;
        txtCustomerID.setText(String.valueOf(customer.getCustomerId()));
        checkBoxCustomerActive.setSelected(customer.getActive());
        txtCustomerName.setText(customer.getCustomerName());
        txtCustomerPhone.setText(customer.getPhoneNumber());
        txtAddress1.setText(customer.getStreetAddress());
        txtAddress2.setText(customer.getStreetAddress2());
        txtCustomerPhone.setText(customer.getPhoneNumber());
        txtCity.setText(customer.getCityName());
        txtCountryName.setText(customer.getCountryName());
        txtPostalCode.setText(customer.getPostalCode());
        
        
    }

    @FXML
    private void handleCustomerActiveToggle(ActionEvent event) {
        
        if(selectedCustomer != null){
            selectedCustomer.setActive(!selectedCustomer.getActive());
            this.checkBoxCustomerActive.setSelected(selectedCustomer.getActive());
        }
    }

    @FXML
    private void handleSaveCustomerChangesButton(ActionEvent event) throws IOException {
        Timestamp sqlNow = DateTimeManager.zdtLocalToTimestampSQL(ZonedDateTime.now());
        int isActive = 0;
        String currentUserName = currentUser.getUsername();
        int customerID = selectedCustomer.getCustomerId();
        boolean boolIsActive = checkBoxCustomerActive.isSelected();
        String name = txtCustomerName.getText().trim();
        String phone = txtCustomerPhone.getText();
        String address1 = txtAddress1.getText().trim();
        String address2 = txtAddress2.getText() + " ";
        String city = txtCity.getText().trim();
        String country = txtCountryName.getText().trim();
        String postalCode = txtPostalCode.getText();
        boolean isValidCustomer = true;
        int cityId = -1;
        int countryId = -1;
        int addressId = -1;
        
        if(name.length() == 0 || address1.length() == 0 || city.length() == 0 || country.length() == 0){
            isValidCustomer = false;
        }
        
        if(isValidCustomer){
            
        }
        cityId = DAO.QueryManager.dataTableHasValueInFieldName("city", "city", city);
        countryId = DAO.QueryManager.dataTableHasValueInFieldName("country", "country", country);
        addressId = DAO.QueryManager.getExistingAddress(address1, postalCode);
        
        if(!boolIsActive){
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Delete Customer Request");
            confirmAlert.setHeaderText("Deleting Customers Cannot Be Reversed!");
            confirmAlert.setContentText("Are you sure you want to remove this customer and all appointments?");

            Optional<ButtonType> response = confirmAlert.showAndWait();
            if(response.get() == ButtonType.OK){
                setCustomerInactive(this.getSelectedCustomer());
                deleteCustomerAppointments(this.getSelectedCustomer());
                
                try (PrintWriter auditLog = ActivityLog.getAuditLog()) {
                    auditLog.append(sqlNow + "[UTC]: Customer record (" + name + ") set inactive and all appointments deleted by: " + ApplicationStateController.getActiveUser() + ".\n");
                } catch (Exception ex){
                    System.out.println(ex);
                }

            } else {
                checkBoxCustomerActive.setSelected(true);
                confirmAlert.hide();
            }
        } else {
            if(countryId == -1){
                try
                {
                    String countryInsertQuery = " into country (country, createDate, createdBy, lastUpdate, lastUpdateBy) values ('";
                    countryInsertQuery += country + "', '" + sqlNow + "', '" + currentUserName + "', '" + sqlNow + "', '" + currentUserName + "')";
                    String countrySelectQuery = "* from country where country = '" + country + "'";
                    DAO.QueryManager.makeRequest("insert", countryInsertQuery);
                    DAO.QueryManager.makeRequest("select", countrySelectQuery);
                    ResultSet results = DAO.QueryManager.getResults();
                    while(results.next()){
                        if(results.getString("country").matches(country)){
                            countryId = results.getInt("countryId");
                        }
                    }

                } catch(SQLException ex){
                    System.out.println(ex);
                }
            }

        if(cityId == -1 && isValidCustomer){
            try
            {
                String cityInsertQuery = " into city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) values ('";
                cityInsertQuery += city + "', " + countryId + ", '" + sqlNow + "', '" + currentUserName + "', '" + sqlNow + "', '" + currentUserName + "')";
                String citySelectQuery = " * from city where city = '" + city + "'";
                DAO.QueryManager.makeRequest("insert", cityInsertQuery);
                DAO.QueryManager.makeRequest("select", citySelectQuery);
                ResultSet results = DAO.QueryManager.getResults();
                while(results.next()){
                    if(results.getString("city").toLowerCase().matches(city.toLowerCase())){
                        cityId = results.getInt("cityId");
                    }
                }

            } catch(SQLException ex){
                System.out.println(ex);
            }
        }
        
        if(addressId == -1 && isValidCustomer){
            try
            {
                String addressInsertQuery = " into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) values ('";
                addressInsertQuery += address1 + "', '";
                addressInsertQuery += address2 + "', " + cityId + ", '" + postalCode + "', '" + phone +"', '";
                addressInsertQuery += sqlNow + "', '" + currentUserName + "', '" + sqlNow + "', '" + currentUserName +"')";   
                String addressSelectQuery = "* from address where address = '" + address1 + "'";
                DAO.QueryManager.makeRequest("insert", addressInsertQuery);
                DAO.QueryManager.makeRequest("select", addressSelectQuery);
                ResultSet results = DAO.QueryManager.getResults();
                while(results.next()){
                    if(results.getString("address").toLowerCase().matches(address1.toLowerCase())){
                        addressId = results.getInt("addressId");
                    }
                }
                System.out.println(addressId);

            } catch(SQLException ex){
                System.out.println(ex);
            }
        } else {
            if(isValidCustomer){
                try 
                {
                    String addressUpdateQuery = " address SET ";
                    addressUpdateQuery += "address = '" + address1 + "', address2 = '" +address2 + "', ";
                    addressUpdateQuery += "cityId = " + cityId + ", postalCode = '" + postalCode +"', ";
                    addressUpdateQuery += "phone = '" + phone + "', ";
                    addressUpdateQuery += "lastUpdate = '" + sqlNow + "', " ;
                    addressUpdateQuery += "lastUpdateBy = '" + currentUserName + "' WHERE addressId = " +addressId ;
                    DAO.QueryManager.makeRequest("update", addressUpdateQuery);

                } catch(SQLException ex){
                    System.out.println(ex);
                }
            }
        }
        
        if(!isValidCustomer){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer Entry Error");
            alert.setHeaderText("Incomplete Customer Information Entered");
            alert.setContentText("To create a new customer record, you must at least enter their name, street address, city, and country. Please correct your entry and resubmit.");
            alert.showAndWait();
        }else{
            isActive = 1;
            try
            {
                String queryBody = " customer SET ";
                queryBody += "customerName = '" + name + "', " ;
                queryBody += "addressId = " + addressId + ", " ;
                queryBody += "active = " + isActive + ", " ;
                queryBody += "lastUpdate = '" + sqlNow + "', " ;
                queryBody += "lastUpdateBy = '" + currentUserName + "' WHERE customerId = " + customerID;     
                DAO.QueryManager.makeRequest("update", queryBody);

                try (PrintWriter auditLog = ActivityLog.getAuditLog()) {
                    auditLog.append(sqlNow + "[UTC]: Customer record (" + name + ") edited by: " + ApplicationStateController.getActiveUser() + ".\n");
                } catch (Exception ex){
                    System.out.println(ex);
                }

            } catch(SQLException ex){
                System.out.println(ex);
            }

        }
            
        
        }
        

        Parent root = FXMLLoader.load(getClass().getResource("ManageCustomersScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        
    }
    
    private void setCustomerInactive(Customer customer){
        try {
            String queryBody = " customer set active = 0 where customerId = " + customer.getCustomerId();
            QueryManager.makeRequest("update", queryBody);
            ResultSet customerRecord = QueryManager.getResults();
            while(customerRecord.next()){
                if(customerRecord.getInt("active") != 0){
                    System.out.println("error deleting customer record");
                }
            }
            
        } catch (SQLException ex){
            System.out.println(ex);
        }
    }
    
    private void deleteCustomerAppointments(Customer customer) throws IOException{
        try {
            String queryBody = (" from appointment where customerId = " + customer.getCustomerId());
            QueryManager.makeRequest("DELETE", queryBody);
            ResultSet appointments = QueryManager.getResults();
            if(appointments.next()){
                System.out.println("Appointment delete error");
            }
        } catch(SQLException ex) {
            System.out.println(ex);
        }
    
    }
    

    @FXML
    private void handleCancelEditCustomerButton(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("ManageCustomersScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }
    
}
