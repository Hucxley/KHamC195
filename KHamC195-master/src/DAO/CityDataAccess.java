/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DataModels.City;
import Utilities.DateTimeManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Calendar;

/**
 *
 * @author Kenneth Ham
 */
public class CityDataAccess {
    public static City getById(int id){
        City foundCity = null;
        try{
            QueryManager.makeRequest("select", " * from city where cityId = '" + id + "'");
            ResultSet response = QueryManager.getResults();
            if(response.next()){
                int cityId = response.getInt("cityId");
                String cityName = response.getString("city");
                int countryId = response.getInt("countryId");
                ZonedDateTime createDate = DateTimeManager.dateStringToLocalZDT(response.getString("createDate"));
                String createdBy = response.getString("createdBy");
                ZonedDateTime lastUpdate = DateTimeManager.dateStringToLocalZDT(response.getString("lastUpdate"));
                String lastUpdateBy = response.getString("lastUpdateBy");
                foundCity = new City(cityId, cityName, countryId, createDate, createdBy, lastUpdate, lastUpdateBy);
                return foundCity;
            } else {
                System.out.println("city not found");
                return null;
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e);
        }
        
        return foundCity;
    }
    
}
