/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModels;

import Utilities.DateTimeManager;
import java.util.Calendar;

/**
 *
 * @author k16080h
 */
public class Customer {
    
    private int customerId;
    private String customerName;
    private int addressId;
    private String streetAddress;
    private String streetAddress2;
    private int cityId;
    private String cityName;
    private String countryName;
    private String postalCode;
    private boolean active;
    private Calendar createDate;
    private String createdBy;
    private Calendar lastUpdate;
    private String lastUpdatedDisplay;
    private String lastUpdateBy;
    private String phoneNumber;
    private String streetAddressDisplay;


    // CONSTRUCTORS
    public Customer(int customerId, String customerName, int addressId, boolean active, Calendar createDate, String createdBy, Calendar lastUpdated, String lastUpdateBy) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdated;
        this.lastUpdateBy = lastUpdateBy;
        this.lastUpdatedDisplay = DateTimeManager.toDateTimeString(lastUpdated);
    }
    
    
    // CLASS METHODS
    
    
    
    // GETTERS & SETTERRS

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    public void setPhoneNumber (String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Calendar createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLastUpdatedDisplay() {
        return lastUpdatedDisplay;
    }

    public void setLastUpdatedDisplay(String lastUpdatedDisplay) {
        this.lastUpdatedDisplay = lastUpdatedDisplay;
    }

    public String getStreetAddressDisplay() {
        return streetAddressDisplay;
    }
    
    public void setStreetAddressDisplay(){
        this.streetAddressDisplay = (this.streetAddress + " " + this.streetAddress2).trim();
    }

    
            
    
}
