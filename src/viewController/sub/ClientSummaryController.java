
package viewController.sub;

import animation.FadeUp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import viewController.DepositsController;
import viewController.SanasaController;
import viewController.SharesController;

public class ClientSummaryController implements Initializable {
    
    private Pane pane;
    private static AnchorPane mainPain;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainPain = SanasaController.mainPane;
    }    
    
    @FXML
    public void actionBtnEvent(ActionEvent event) {
        try {
            Button btn = (Button) event.getSource();
            switch(btn.getId()){
                case "makePayBtn":
                    mainPain.getChildren().setAll(getContent("subView/makePayment.fxml"));
                    new FadeUp(mainPain, 20, 0);
                    break;
                case "takeLoanBtn":
                    mainPain.getChildren().setAll(getContent("subView/takeLoan.fxml"));
                    new FadeUp(mainPain, 20, 0);
                    break;
                case "doDepositBtn":
                    DepositsController.selectedMemId = SummaryController.selectedMemId;
                    mainPain.getChildren().setAll(getContent("deposits.fxml"));   
                    break;
                case "shareBtn":
                    SharesController.selectedMemId = SummaryController.selectedMemId;
                    mainPain.getChildren().setAll(getContent("shares.fxml")); 
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientSummaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/"+ a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.5);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }
}
