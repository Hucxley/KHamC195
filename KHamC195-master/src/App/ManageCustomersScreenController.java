/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import DAO.QueryManager;
import DataModels.Address;
import DataModels.City;
import DataModels.Country;
import DataModels.Customer;
import Utilities.DateTimeManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kenneth Ham
 */
public class ManageCustomersScreenController implements Initializable {

    @FXML
    private Label txtHeader;
    @FXML
    private TableView<Customer> tableManageCustomers;
    @FXML
    private TableColumn<Customer, Integer> colCustomerId;
    @FXML
    private TableColumn<Customer, String> colCustomerName;
    @FXML
    private TableColumn<Customer, Boolean> colCustomerActive;
    @FXML    
    private TableColumn<Customer, String> colCustomerStreetAddress;
    @FXML
    private TableColumn<Customer, String> colCustomerCity;
    @FXML
    private TableColumn<Customer, String> colCustomerCountry;
    @FXML
    private TableColumn<Customer, String> colCustomerPostalCode;
    @FXML
    private TableColumn<Customer, String> colDateLastUpdated;
    @FXML
    private Button btnAddCustomer;
    @FXML
    private Button btnEditCustomer;
    @FXML
    private Button btnDeleteCustomer;
    @FXML
    private Button btnCancelManageCustomers;
    
    Stage stage = ApplicationStateController.getMainStage();



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       try {
           getAllCustomerData();
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(ManageUsersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // INSTANCE METHODS
    /**
     * 
     * @throws SQLException
     * @throws ParseException 
     */
    public void getAllCustomerData() throws SQLException, ParseException{
    ObservableList <Customer> customerList = FXCollections.observableArrayList();
    ResultSet response = QueryManager.getDataFromTable("customer");
    while(response.next()){
        int customerId = response.getInt("customerId");
        String customerName = response.getString("customerName");
        boolean active = response.getBoolean("active");
        int customerAddressId = response.getInt("addressId");
        Calendar createdDate = DateTimeManager.convertToCalendar(response.getString("createDate"));
        String createdBy = response.getString("createdBy");
        Calendar lastUpdated = DateTimeManager.convertToCalendar(response.getString("lastUpdate"));
        String lastUpdatedBy = response.getString("lastUpdateBy");
        Customer nextCustomer = new Customer(customerId, customerName, customerAddressId, active, createdDate, createdBy, lastUpdated, lastUpdatedBy);
        customerList.add(nextCustomer);
    };
    
    // LAMBDA to consume each customer object in list and set remaining values through database calls before addding customer to tableview
    customerList.forEach(customer -> {
        int addressId = customer.getAddressId();
        Address customerAddress = DAO.AddressDataAccess.getById(addressId);
        City customerCity = DAO.CityDataAccess.getById(customerAddress.getCityId());
        Country customerCountry = DAO.CountryDataAccess.getById(customerCity.getCountryId());
        customer.setStreetAddress(customerAddress.getAddress());
        customer.setStreetAddress2(customerAddress.getAddress2());
        customer.setStreetAddressDisplay(); // CONCAT Address and Address2 for display in table in single cell
        customer.setCityName(customerCity.getCityName());
        customer.setCountryName(customerCountry.getCountryName());
        customer.setPostalCode(customerAddress.getPostalCode());
        tableManageCustomers.getItems().add(customer);
    });
    
    colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
    colCustomerActive.setCellValueFactory(new PropertyValueFactory<>("active"));
    colCustomerStreetAddress.setCellValueFactory(new PropertyValueFactory<>("streetAddressDisplay"));
    colCustomerCity.setCellValueFactory(new PropertyValueFactory<>("cityName"));
    colCustomerCountry.setCellValueFactory(new PropertyValueFactory<>("countryName"));
    colCustomerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
    colDateLastUpdated.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedDisplay"));
        
    }

    // FXML METHODS
    @FXML
    private void handleAddCustomerButton(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("NewCustomerScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void handleEditCustomerButton(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("EditCustomerScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void handleDeleteCustomerButton(ActionEvent event) {
        
        //TODO: add modal confirmation
    }

    @FXML
    private void handleCancelManageCustomersButton(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("UserScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    
}
