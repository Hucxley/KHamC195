/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Kenneth Ham
 */
public class DateTimeManager {
    
    public static Calendar convertToCalendar(String date) throws ParseException{
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateObj = dateFormat.parse(date);
        cal.setTime(dateObj);
        
        return cal;
    }
    
}
