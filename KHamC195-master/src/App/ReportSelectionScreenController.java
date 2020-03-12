/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kenneth Ham
 */
public class ReportSelectionScreenController implements Initializable {

    @FXML
    private Label txtHeader;
    @FXML
    private Button btnViewScheduleReport;
    @FXML
    private Button btnViewMonthlyReport;
    @FXML
    private Button btnCancelReportsMenu;
    @FXML
    private Button btnViewAuditLog;
    
    private Stage stage = ApplicationStateController.getMainStage();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleViewScheduleReportButton(ActionEvent event) throws IOException {
        stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ReportUsersSchedule.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void handleViewMonthlyReportButton(ActionEvent event) throws IOException {
        stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ReportMonthlyAppointmentsScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void handleReportsMenuCancelButton(ActionEvent event) throws IOException {
        stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("UserScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void handleAuditLogButton(ActionEvent event) throws IOException {
        stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ReportAuditLogScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
}
