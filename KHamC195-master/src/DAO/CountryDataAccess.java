/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DataModels.Country;
import Utilities.DateTimeManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;

/**
 *
 * @author Kenneth Ham
 */
public class CountryDataAccess {
    public static Country getById(int id){
        Country foundCountry = null;
        try{
            QueryManager.makeRequest("select", " * from country where countryId = '" + id + "'");
            ResultSet response = QueryManager.getResults();
            if(response.next()){
                int countryId = response.getInt("countryId");
                String countryName = response.getString("country");
                Calendar createDate = DateTimeManager.convertToCalendar(response.getString("createDate"));
                String createdBy = response.getString("createdBy");
                Calendar lastUpdate = DateTimeManager.convertToCalendar(response.getString("lastUpdate"));
                String lastUpdateBy = response.getString("lastUpdateBy");
                foundCountry = new Country(countryId, countryName, createDate, createdBy, lastUpdate, lastUpdateBy);
                return foundCountry;
            } else {
                System.out.println("country not found");
                return null;
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e);
        }
        
        return foundCountry;
    }
    
}
