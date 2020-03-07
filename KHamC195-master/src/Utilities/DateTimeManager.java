/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 *
 * @author Kenneth Ham
 */
public class DateTimeManager {
    
    public static ZonedDateTime dateStringToLocalZDT(String date) throws ParseException{
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss.S"); 
        String strDate = date;

        LocalDateTime localDateTime = LocalDateTime.parse(strDate, df);

        //Convert to a ZonedDate Time with system default timezone
        ZoneId zid = ZoneId.systemDefault();

        ZonedDateTime zdtLocal = localDateTime.atZone(zid);
       
        
        return zdtLocal;
    }
    
    public static Timestamp zdtLocalToTimestampSQL(ZonedDateTime zdt){
        
        ZonedDateTime zdtUTC = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime localDateTime = zdtUTC.toLocalDateTime();
        
        //Create Timestamp values from Instants to update database
        Timestamp timestampSQL = Timestamp.valueOf(localDateTime); //this value can be inserted into database
        System.out.println("Timestamp to be inserted: " + timestampSQL); 
        
        return timestampSQL;
    }
    
    public static String getCurrentTimeUTC(){
        return ZonedDateTime.now(ZoneOffset.UTC).toString();
    }
    
    public static String toDateTimeString(ZonedDateTime zdt) {
        
        String strDateTime;
        ZonedDateTime zdtLocal = zdt.toLocalDateTime().atZone(ZoneId.systemDefault());
        strDateTime = zdtLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss"));
        return strDateTime;
    }
    
    public static String toDateString(ZonedDateTime zdt){
        String strDate;
        
        LocalDate zdtLocal = zdt.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
        strDate = zdtLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        return strDate;
    }
    
    public static String toTimeString(ZonedDateTime zdt){
        String strTime;
        
        LocalTime zdtLocal = zdt.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime();
        strTime = zdtLocal.format(DateTimeFormatter.ofPattern("kk:mm"));
        
        return strTime;
    }

    public static ZonedDateTime convertToZDT(Timestamp date) {
        return date.toLocalDateTime().atZone(ZoneId.of("UTC"));
    }
    
}
