
package viewController;

import animation.FadeUp;
import dbController.MemberDBController;
import dbModel.Member;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import viewController.tables.SharesTableController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class SharesController implements Initializable {
    @FXML
    private TextField memberIdText, nameText;
    
    @FXML
    private AnchorPane tablePane;
    
    private Pane pane;
    public static int selectedMemId;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            tablePane.getChildren().setAll(getContent("sharesTable.fxml"));
            new FadeUp(tablePane,20,0);
        } catch (IOException ex) {
            Logger.getLogger(SharesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        prepareClient();
    }   
    
    public Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/tables/" + a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }
    
    
    private void prepareClient() {
        try { 
            Member member = MemberDBController.searchMemberFromId(selectedMemId);
            if (member==null) {
                System.out.println("No Such Customer");
            } else {
                memberIdText.setText(member.getMemId()+"");
                nameText.setText(member.getName());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SharesTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SharesTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
