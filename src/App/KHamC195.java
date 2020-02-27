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
import DataModels.Customer

/**
 *
 * @author Kenneth Ham
 */
public class KHamC195 extends Application {
    
    private static Stage mainStage;
    private static Customer activeCustomer;
    
    private static void setStage(Stage stage){
        KHamC195.mainStage = stage;
    }
    
    static public Stage getMainStage(){
        return KHamC195.mainStage;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        setStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
