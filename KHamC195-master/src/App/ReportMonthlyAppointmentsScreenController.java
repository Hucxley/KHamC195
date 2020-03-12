/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class ReportMonthlyAppointmentsScreenController implements Initializable {

    @FXML
    private Label txtHeader;
    @FXML
    private Button btnReportViewBack;
    @FXML
    private Label labelPresentationValue;
    @FXML
    private Label labelScrumValue;
    @FXML
    private Label labelConsultationValue;
    @FXML
    private Label labelFeedbackValue;
    @FXML
    private Label labelProgressValue;
    @FXML
    private Label labelFollowUpValue;
    @FXML
    private Label labelMarketingValue;
    @FXML
    private Label labelSalesValue;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setReportValues();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }    

    @FXML
    private void handleReportViewBackButton(ActionEvent event) throws IOException {
        
        Stage stage = ApplicationStateController.getMainStage();
        Parent root = FXMLLoader.load(getClass().getResource("ReportSelectionScreen.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setReportValues() throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDateTime firstOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).plusDays(1).atStartOfDay();
        System.out.println(firstOfMonth);
        System.out.println(endOfMonth);
        
        String countQuery = " COUNT(IF(type='Presentation', 1, null))from appointment where start between '" + firstOfMonth +"' and '" +endOfMonth + "'";

        DAO.QueryManager.makeRequest("select", countQuery);
        ResultSet response = DAO.QueryManager.getResults();
        if(response.next()){
            labelPresentationValue.setText(response.getString(1));
        }else{
            labelPresentationValue.setText("0");
        }
        
        countQuery = " COUNT(IF(type='Scrum', 1, null))from appointment where start between '" + firstOfMonth +"' and '" +endOfMonth + "'";

        DAO.QueryManager.makeRequest("select", countQuery);
        response = DAO.QueryManager.getResults();
        if(response.next()){
            labelScrumValue.setText(response.getString(1));
        }else{
            labelScrumValue.setText("0");
        }
        
        countQuery = " COUNT(IF(type='Consultation', 1, null))from appointment where start between '" + firstOfMonth +"' and '" +endOfMonth + "'";

        DAO.QueryManager.makeRequest("select", countQuery);
        response = DAO.QueryManager.getResults();
        if(response.next()){
            labelConsultationValue.setText(response.getString(1));
        }else{
            labelConsultationValue.setText("0");
        }        
        
        countQuery = " COUNT(IF(type='Feedback', 1, null))from appointment where start between '" + firstOfMonth +"' and '" +endOfMonth + "'";

        DAO.QueryManager.makeRequest("select", countQuery);
        response = DAO.QueryManager.getResults();
        if(response.next()){
            labelFeedbackValue.setText(response.getString(1));
        }else{
            labelFeedbackValue.setText("0");
        }        
        
        countQuery = " COUNT(IF(type='Progress Update', 1, null))from appointment where start between '" + firstOfMonth +"' and '" +endOfMonth + "'";

        DAO.QueryManager.makeRequest("select", countQuery);
        response = DAO.QueryManager.getResults();
        if(response.next()){
            labelProgressValue.setText(response.getString(1));
        }else{
            labelProgressValue.setText("0");
        }        
        
        countQuery = " COUNT(IF(type='Follow Up', 1, null))from appointment where start between '" + firstOfMonth +"' and '" +endOfMonth + "'";

        DAO.QueryManager.makeRequest("select", countQuery);
        response = DAO.QueryManager.getResults();
        if(response.next()){
            labelFollowUpValue.setText(response.getString(1));
        }else{
            labelFollowUpValue.setText("0");
        }
        
        countQuery = " COUNT(IF(type='Marketing', 1, null))from appointment where start between '" + firstOfMonth +"' and '" +endOfMonth + "'";

        DAO.QueryManager.makeRequest("select", countQuery);
        response = DAO.QueryManager.getResults();
        if(response.next()){
            labelMarketingValue.setText(response.getString(1));
        }else{
            labelMarketingValue.setText("0");
        }
        
        countQuery = " COUNT(IF(type='Sales', 1, null))from appointment where start between '" + firstOfMonth +"' and '" +endOfMonth + "'";

        DAO.QueryManager.makeRequest("select", countQuery);
        response = DAO.QueryManager.getResults();
        if(response.next()){
            labelSalesValue.setText(response.getString(1));
        }else{
            labelSalesValue.setText("0");
        }
              
    }
    
}
