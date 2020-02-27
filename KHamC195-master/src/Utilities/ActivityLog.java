/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kenneth Ham
 */
public class ActivityLog {
    private static FileHandler LOGFILE;
    private static final Logger USERLOG = Logger.getLogger("KHamC195.Utilities.ActivityLog");
    
    private static void initLogFile() throws IOException{
        LOGFILE = new FileHandler("./activity-log.txt", true);
        USERLOG.addHandler(LOGFILE);
        USERLOG.setLevel(Level.INFO);
        
    }
    
    public static void logUserActivity(String logInfo){
        //String utcTimeStamp = DateTimeManager
        USERLOG.info(logInfo);
       
        
    }

    
    
}
