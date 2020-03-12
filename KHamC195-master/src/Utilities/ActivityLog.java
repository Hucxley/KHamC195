/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kenneth Ham
 */
public class ActivityLog {

    
    public static void logUserActivity(String logInfo){
                
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(
                    new File("ActivityLog.txt"),
                    true /* append = true */));
            pw.append(logInfo + "\n");
            pw.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
       
        
    }
    
    public static PrintWriter getAuditLog() throws FileNotFoundException{
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new FileOutputStream(
            new File("AuditLog.txt"),
            true /* append = true */));
        } catch(FileNotFoundException ex){
            System.out.println(ex);
        }
        
        return pw;

    }

    
    
}
