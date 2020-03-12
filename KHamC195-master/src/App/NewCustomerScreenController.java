/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import Utilities.DateTimeManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
public class NewCustomerScreenController implements Initializable {

    @FXML
    private TextField txtCustomerID;
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
    private TextField txtCountryName;
    @FXML
    private Label txtHeader;
    @FXML
    private Button btnAddNewCustomer;
    @FXML
    private Button btnCancelNewCustomer;
    
    private Stage stage;
    @FXML
    private CheckBox checkBoxCustomerActive;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleAddNewCustomerButton(ActionEvent event) throws IOException {
        String name = txtCustomerName.getText().trim();
        String phone = txtCustomerPhone.getText();
        String address1 = txtAddress1.getText().trim();
        String address2 = txtAddress2.getText();
        String city = txtCity.getText().trim();
        String postalCode = txtPostalCode.getText();
        String country = txtCountryName.getText().trim();
        int active = 1;
        Timestamp sqlNow = DateTimeManager.zdtLocalToTimestampSQL(ZonedDateTime.now());
        String currentUserName = ApplicationStateController.getActiveUser().getUsername();
        int cityId = -1;
        int countryId = -1;
        int addressId = -1;
        boolean isValidCustomer = true;
        if(address1.length() == 0 || name.length() == 0 || city.length() == 0 || country.length() == 0){
            isValidCustomer = false;
        }
        
        if(isValidCustomer){
            cityId = DAO.QueryManager.dataTableHasValueInFieldName("city", "city", city);
            countryId = DAO.QueryManager.dataTableHasValueInFieldName("country", "country", country);
            addressId = DAO.QueryManager.getExistingAddress(address1, postalCode);
        }
          
        if(countryId == -1 && isValidCustomer){
            try
            {
                String countryInsertQuery = " into country (country, createDate, createdBy, lastUpdate, lastUpdateBy) values ('";
                countryInsertQuery += country + "', '" + sqlNow + "', '" + currentUserName + "', '" + sqlNow + "', '" + currentUserName + "')";
                String countrySelectQuery = " * from country where country = '" + country + "'";
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
                String addressSelectQuery = " * from address where address = '" + address1 + "'";
                DAO.QueryManager.makeRequest("insert", addressInsertQuery);
                DAO.QueryManager.makeRequest("select", addressSelectQuery);
                ResultSet results = DAO.QueryManager.getResults();
                while(results.next()){
                    if(results.getString("address").toLowerCase().matches(address1.toLowerCase())){
                        addressId = results.getInt("addressId");
                    }
                }
            } catch(SQLException ex){
                System.out.println(ex);
            }
        
        }
        
        if(!isValidCustomer){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Customer Entry Error");
            alert.setHeaderText("Incomplete Customer Information Entered");
            alert.setContentText("To create a new customer record, you must at least enter their name, street address, city, and country. Please correct your entry and resubmit.");
            alert.showAndWait();
        }else{
            if(countryId > 0 && cityId > 0 && addressId > 0){
                try {
                    String customerInsertQuery = " into customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) values (";
                    customerInsertQuery += "'" + name + "', ";
                    customerInsertQuery += "" + addressId + ", ";
                    customerInsertQuery += "'" + active + "', ";
                    customerInsertQuery += "'" + sqlNow + "', ";
                    customerInsertQuery += "'" + currentUserName + "', ";
                    customerInsertQuery += "'" + sqlNow + "', ";
                    customerInsertQuery += "'" + currentUserName + "')";
                    DAO.QueryManager.makeRequest("insert", customerInsertQuery);
                } catch(SQLException ex){
                    System.out.println(ex);
                }
            }


            stage = ApplicationStateController.getMainStage();
            Parent root = FXMLLoader.load(getClass().getResource("ManageCustomersScreen.fxml"));

            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }

    }

    @FXML
    private void handleCancelNewCustomerButton(ActionEvent event) throws IOException {
        
        stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ManageCustomersScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    
}
