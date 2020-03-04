/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DataModels.Address;
import Utilities.DateTimeManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;

/**
 *
 * @author Kenneth Ham
 */
public class AddressDataAccess {
    public static Address getById(int id){
        Address foundAddress = null;
        try{
            QueryManager.makeRequest("select", " * from address where addressId = '" + id + "'");
            ResultSet response = QueryManager.getResults();
            if(response.next()){
                int addressId = response.getInt("addressId");
                String addressLine1 = response.getString("address");
                String addressLine2 = response.getString("address2");
                int cityId = response.getInt("cityId");
                String postalCode = response.getString("postalCode");
                String phone = response.getString("phone");
                Calendar createDate = DateTimeManager.convertToCalendar(response.getString("createDate"));
                String createdBy = response.getString("createdBy");
                Calendar lastUpdate = DateTimeManager.convertToCalendar(response.getString("lastUpdate"));
                String lastUpdateBy = response.getString("lastUpdateBy");
                foundAddress = new Address(addressId, addressLine1, addressLine2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy);
                System.out.println(phone);
                return foundAddress;
            } else {
                System.out.println("address not found");
                return null;
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e);
        }
        
        return foundAddress;
    }
    
}
