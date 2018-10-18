
package viewController;

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
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import viewController.sub.ViewAllMembersTableController;

public class MemberController implements Initializable {
    
    //--------------------------- FXML Attributes ----------------------------//
    @FXML
    private AnchorPane contentPane;

    @FXML
    private ComboBox<String> searchMemCombo, clientTypeCombo;
   
    @FXML
    private Button viewBtn;
   
    //---------------------- Normal Attributes -------------------------------//
    Pane pane;
    private static boolean viewBtnStatus;
    
    //---------------------- Initialize and Startup Actions ------------------//
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewBtnStatus =true;
        clientTypeCombo.getItems().setAll("Member - Active",
                                          "Member - Inactive",
                                          "Non Member - Active",
                                          "Non Member - Inactive",
                                          "Applicant");
        clientTypeCombo.getSelectionModel().select(0);
        prepareNode();
    }    
    
    //---------------------- Nodes Events -------------------------------------//
    @FXML
    public void viewBtnEvent(ActionEvent event) {
        if (viewBtnStatus) {
            viewBtnStatus = false;
            prepareNode();
        } else {
            viewBtnStatus = true;
            prepareNode();
        }
    }
    
    @FXML
    public void clientTypeComboEvent(ActionEvent event) {
        ViewAllMembersTableController.setTableData(clientTypeCombo.getSelectionModel().getSelectedIndex());
    }

    //------------------------- Addtional Methods ----------------------------//
    public Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/subView/"+ a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }

    private void prepareNode() {
        try{
            if(viewBtnStatus){
                contentPane.getChildren().setAll(getContent("viewAllMembersGraphic.fxml"));
                new FadeUp(contentPane, 20, 0);
                viewBtn.setText("Table View");
                clientTypeCombo.setVisible(false);
                searchMemCombo.setVisible(true);               
            }else{
                contentPane.getChildren().setAll(getContent("viewAllMembersTable.fxml"));
                new FadeUp(contentPane, 20, 0);
                viewBtn.setText("Graphic View");
                ViewAllMembersTableController.setTableData(clientTypeCombo.getSelectionModel().getSelectedIndex());
                clientTypeCombo.setVisible(true);
                searchMemCombo.setVisible(false);
            }
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
