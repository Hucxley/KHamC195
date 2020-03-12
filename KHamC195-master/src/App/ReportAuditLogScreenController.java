/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Kenneth Ham
 */
public class ReportAuditLogScreenController implements Initializable {

    @FXML
    private Label txtHeader;
    @FXML
    private AnchorPane anchorTextAreaAuditLog;
    @FXML
    private TextArea textAreaAuditLog;
    @FXML
    private Button btnReportViewBack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try { 
            initAuditLog();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReportAuditLogScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    private void initAuditLog() throws FileNotFoundException {
        ObservableList<String> logEntries = FXCollections.observableArrayList();
        Scanner fileScanner = new Scanner(new FileReader("AuditLog.txt"));
        while(fileScanner.hasNextLine()){
            String logEntry = fileScanner.nextLine();
            System.out.println(logEntry);
            logEntries.add(logEntry);
        }
        FXCollections.reverse(logEntries);
        textAreaAuditLog.setText("Audit Log Report (from newest event to oldest)\n");
        logEntries.forEach(entry -> {
            textAreaAuditLog.appendText(entry + "\n");
        });
        
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
    
}
