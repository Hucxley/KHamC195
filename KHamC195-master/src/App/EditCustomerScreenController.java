/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DataModels.Customer;
import DataModels.User;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
        
        System.out.println(customer.getPhoneNumber());
        
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
        java.sql.Date sqlNow = new java.sql.Date(new Date().getTime());
        int isActive = 0;
        String currentUserName = currentUser.getUsername();
        int customerID = selectedCustomer.getCustomerId();
        boolean boolIsActive = checkBoxCustomerActive.isSelected();
        String name = txtCustomerName.getText();
        String phone = txtCustomerPhone.getText();
        String address1 = txtAddress1.getText();
        String address2 = txtAddress2.getText();
        String city = txtCity.getText();
        String country = txtCountryName.getText();
        String postalCode = txtPostalCode.getText();
 
        int cityId = DAO.QueryManager.dataTableHasValueInFieldName("city", "city", city);
        int countryId = DAO.QueryManager.dataTableHasValueInFieldName("country", "country", country);
        int addressId = DAO.QueryManager.dataTableHasValueInFieldName("address", "address", address1);
          
        if(countryId == -1){
            try
            {
                String countryInsertQuery = " into country (country) values '" + country + "'";
                String countrySelectQuery = " from country where country = '" + country + "'";
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

        if(cityId == -1){
            try
            {
                String cityInsertQuery = " into city (city, countryId) values '" + city + ", '" + countryId +"'";
                String citySelectQuery = " from city where city = '" + city + "'";
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
        
        if(addressId == -1){
             try
            {
                String addressInsertQuery = " into address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) values ('";
                addressInsertQuery += address1 + "', '";
                addressInsertQuery += address2 + "', " + cityId + ", '" + postalCode + "', '" + phone +"', '";
                addressInsertQuery += sqlNow + "', '" + currentUserName + "', '" + sqlNow + "', '" + currentUserName +"')";   
                String addressSelectQuery = "* from address where address = '" + address1 + "'";
                System.out.println(addressInsertQuery);
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
            

        if(boolIsActive){
            isActive = 1;
        }
        try{
            
            String queryBody = " customer SET ";
            queryBody += "customerName = '" + name + "', " ;
            queryBody += "addressId = " + addressId + ", " ;
            queryBody += "active = " + isActive + ", " ;
            queryBody += "lastUpdate = '" + sqlNow + "', " ;
            queryBody += "lastUpdateBy = '" + currentUserName + "' WHERE customerId = " + customerID;     
            DAO.QueryManager.makeRequest("update", queryBody);
            
        } catch(SQLException ex){
            System.out.println(ex);
        }
        
        Parent root = FXMLLoader.load(getClass().getResource("ManageCustomersScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    
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
