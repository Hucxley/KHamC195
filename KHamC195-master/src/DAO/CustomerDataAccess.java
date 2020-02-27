/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DataModels.Customer;
import Utilities.DateTimeManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;

/**
 *
 * @author Kenneth Ham
 */
public class CustomerDataAccess {
    public static Customer getById(int id){
        Customer foundCustomer = null;
        try{
            QueryManager.makeRequest("select", " * from customer where customerId = '" + id + "'");
            ResultSet response = QueryManager.getResults();
            if(response.next()){
                int customerId = response.getInt("customerId");
                String customerName = response.getString("customerName");
                int customerAddressId = response.getInt("addressId");
                boolean active = response.getBoolean("active");
                Calendar createDate = DateTimeManager.convertToCalendar(response.getString("createDate"));
                String createdBy = response.getString("createdBy");
                Calendar lastUpdate = DateTimeManager.convertToCalendar(response.getString("lastUpdate"));
                String lastUpdateBy = response.getString("lastUpdateBy");

                foundCustomer = new Customer(customerId, customerName, customerAddressId, active, createDate, createdBy, lastUpdate, lastUpdateBy);
                return foundCustomer;
            } else {
                System.out.println("customer not found");
                return null;
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e);
        }
        
        return foundCustomer;
    }
}
