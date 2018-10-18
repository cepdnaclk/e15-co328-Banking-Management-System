/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewController.tables;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import viewController.DepositsController;
/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class FixedDepositTablesController implements Initializable {
    private static AnchorPane mainPain;
    private Pane pane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       mainPain = DepositsController.mainPane;
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
