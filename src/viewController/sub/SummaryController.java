
package viewController.sub;

import animation.FadeInLeft;
import dbController.MemberDBController;
import dbModel.Member;
import viewController.DashboardController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class SummaryController implements Initializable {
    @FXML
    private TextField nameText, typeText, statusText, contactText;
 
    @FXML
    private AnchorPane summaryPane;

    @FXML
    private Button configure, backToBtn;

    @FXML
    private ComboBox<String> searchClientCombo;
    
    //---------------------- Normal Attributes -------------------------------//
    
    Pane pane;
    public static int selectedMemId;
    private static ObservableList<String> result;
    private static String selectClient;
    
    //---------------------- Initialize and Startup Actions ------------------//
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()-> {
            backToBtn.setVisible(false);
            searchableCombo();
        });
        try {
            summaryPane.getChildren().setAll(getContent("localSummary.fxml"));
            
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    //---------------------- Nodes Events ------------------------------------//
  
    @FXML
    public void backToBtnEvent(ActionEvent event) {
        try {
            summaryPane.getChildren().setAll(getContent("localSummary.fxml"));
            new FadeInLeft(summaryPane, 20, 0);
            typeText.setText("");
            statusText.setText("");
            nameText.setText("");
            contactText.setText("");
            backToBtn.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(SummaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    private void prepareClient(){
        try {
            String[] breaks = selectClient.split(" ");
            int clientId = Integer.parseInt(breaks[0]);
            Member member = MemberDBController.searchMemberFromId(clientId);
            if (member == null) {
                System.out.println("No Such Customer");
            } else {
                typeText.setText(member.isIsMember() ? "Member" : "Non-Member");
                statusText.setText(member.isIsActive() ? "Active" : "Inactive");
                nameText.setText(member.getName());
                contactText.setText(member.getContact());
                selectedMemId = member.getMemId();
                summaryPane.getChildren().setAll(getContent("clientSummary.fxml"));
                new FadeInLeft(summaryPane, 20, 0);
            }
            searchClientCombo.getEditor().setText("");
            backToBtn.setVisible(true);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SummaryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SummaryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SummaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void searchableCombo() {
        searchClientCombo.getEditor().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.DOWN || event.getCode()== KeyCode.UP) {
                    event.consume();
                }else{
                    String text = searchClientCombo.getEditor().getText();
                    try {
                        result = MemberDBController.likeSearchClient(text);
                        
                        searchClientCombo.hide();
                        searchClientCombo.getItems().setAll(result);
                        if (result.isEmpty()) {
                            searchClientCombo.hide();
                        } else {
                            searchClientCombo.show();
                        }

                    } catch (ClassNotFoundException | SQLException ex) {
                    }
                    event.consume();               
                }
            }
        });
        
        searchClientCombo.setOnKeyReleased((KeyEvent event) -> {
            try {
                if (event.getCode() == KeyCode.ENTER) {
                    selectClient = searchClientCombo.getSelectionModel().getSelectedItem();
                    searchClientCombo.getSelectionModel().clearSelection();
                    searchClientCombo.getEditor().setText("");
                    prepareClient();
                    event.consume();
                }
            } catch (NumberFormatException ex) {
                searchClientCombo.getEditor().setText("");
            }catch (NullPointerException ex){
                searchClientCombo.getEditor().setText("");
            }
        });
        
    }
}
