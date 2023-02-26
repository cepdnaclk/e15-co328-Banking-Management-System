
package viewController;

import animation.FadeUp;
import help.MessageBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController implements Initializable {
    @FXML
    private AnchorPane contentPane;
    
    @FXML
    private Button newClientBtn;
  
    
    Pane pane;
    
    private static AnchorPane parentPane;
    private static AnchorPane mainPain;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        parentPane = contentPane;    
        mainPain = SanasaController.mainPane;
        try {
            contentPane.getChildren().setAll(getContent("summary.fxml"));
            new FadeUp(contentPane, 20, 0);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    @FXML
    public void newClientBtnEvent(ActionEvent event) {
       try {
            mainPain.getChildren().setAll(getContent("addNewClient.fxml"));
            new FadeUp(mainPain, 20, 0);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void backUpBtnEvent(ActionEvent event) {
        MessageBox.showMessageBox("Message","Can't Backup Yet..!");
    }
    
    public Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/subView/" + a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }
    
    public static AnchorPane getParentPane(){
        return parentPane;
    }
}
