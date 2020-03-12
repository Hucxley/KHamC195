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
import Utilities.ActivityLog;
import Utilities.DateTimeManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    @FXML
    private Button btnViewCustomerAppointments;



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
            ZonedDateTime createdDate = DateTimeManager.dateStringToLocalZDT(response.getString("createDate"));
            String createdBy = response.getString("createdBy");
            ZonedDateTime lastUpdated = DateTimeManager.dateStringToLocalZDT(response.getString("lastUpdate"));
            String lastUpdatedBy = response.getString("lastUpdateBy");
            Customer nextCustomer = new Customer(customerId, customerName, customerAddressId, active, createdDate, createdBy, lastUpdated, lastUpdatedBy);
            customerList.add(nextCustomer);
        };
        tableManageCustomers.getItems().clear();
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
            customer.setPhoneNumber(customerAddress.getPhone());
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
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EditCustomerScreen.fxml"));
            loader.load();

            EditCustomerScreenController ECSController = loader.getController();
            ECSController.setCustomerToEdit(tableManageCustomers.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }catch(IOException ex){
            System.out.println(ex);
        }
        
    }

    @FXML
    private void handleDeleteCustomerButton(ActionEvent event) {

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Customer Request");
        confirmAlert.setHeaderText("Deleting Customers Cannot Be Reversed!");
        confirmAlert.setContentText("Are you sure you want to remove this customer and all appointments?");
        Customer selectedCustomer = tableManageCustomers.getSelectionModel().getSelectedItem();
        
        Optional<ButtonType> response = confirmAlert.showAndWait();
        if(response.get() == ButtonType.OK){
            setCustomerInactive(selectedCustomer);
            deleteCustomerAppointments(selectedCustomer);
            try (PrintWriter auditLog = ActivityLog.getAuditLog()) {
                Timestamp sqlNow = DateTimeManager.zdtLocalToTimestampSQL(ZonedDateTime.now());
                auditLog.append(sqlNow + "[UTC]: Customer record and appointments for " + selectedCustomer.getCustomerName() + " deleted by: " + ApplicationStateController.getActiveUser() + ".\n");
            } catch (Exception ex){
                    System.out.println(ex);
            }
        }else{
            confirmAlert.hide();
        }
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
    
    private void deleteCustomerAppointments(Customer customer){
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
        
        try {
            getAllCustomerData();
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(ManageUsersScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void handleCancelManageCustomersButton(ActionEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("UserScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void handleViewCustomerAppointmentsButton(ActionEvent event) {
        Customer selectedCustomer = tableManageCustomers.getSelectionModel().getSelectedItem();
        boolean customerIsActive = selectedCustomer.getActive();
        if(!customerIsActive){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Customer Inactive");
            alert.setContentText("Inactive Customers do not have appointments. Please select another customer.");
            
            alert.showAndWait();
            
        }else{
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("ManageAppointmentScreen.fxml"));
                loader.load();

                ManageAppointmentScreenController MASController = loader.getController();
                 int custId = tableManageCustomers.getSelectionModel().getSelectedItem().getCustomerId();

                MASController.setAppointmentsFilter("customer", custId);

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
         
    }

    
}
