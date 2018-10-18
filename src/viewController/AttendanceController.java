/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewController;

import animation.FadeUp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class AttendanceController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane contentPane;
    
    private Pane pane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            contentPane.getChildren().setAll(getContent("attendanceMarkTable.fxml"));
            new FadeUp(contentPane,20,0);
        } catch (IOException ex) {
            Logger.getLogger(SharesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/tables/"+ a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }
}
