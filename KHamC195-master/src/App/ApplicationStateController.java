/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DataModels.Customer;
import DataModels.User;
import java.sql.Connection;
import java.sql.SQLException;


/**
 *
 * @author Kenneth Ham
 */
public class ApplicationStateController extends Application {
    
    private static Stage mainStage;
    private static Customer selectedCustomer;
    private static User activeUser;
    
    private static void setStage(Stage stage){
        mainStage = stage;
    }
    
    public static Stage getMainStage(){
        return ApplicationStateController.mainStage;
    }
    
    private static void setSelectedCustomer(Customer customer){
        selectedCustomer = customer;
    }
    
    public static Customer getSelectedCustomer(){
        return selectedCustomer;
    }
    
    static void setActiveUser(User user){
       activeUser = user;
    }
    
    public static User getActiveUser(){
        return activeUser;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
    /* Stop override stop method behavior from user Jose Pareda
       from Stack Overflow https://stackoverflow.com/a/26620713
    */
    @Override
    public void stop() throws SQLException{
        setActiveUser(null);
        setSelectedCustomer(null);
        Connection db = DAO.DBConnection.getOpenConnection();
        if(db != null){
            System.out.println("closing db connection");
            db.close();
        }
        // TODO: verify session data closed
        System.out.println("Session data cleared");
        mainStage.close();
   
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
