
package viewController.tables;

import dbController.SharesDBController;
import dbModel.Shares;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import viewController.SharesController;

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
public class SharesTableController implements Initializable {
    @FXML
    private TableView<Shares> shareTable;

    @FXML
    private TableColumn<Shares, Double> amountColumn;

    @FXML
    private TableColumn<Shares, String> timeColumn;
    
    @FXML
    private TableColumn<Shares, String> dateColumn;

    @FXML
    private TableColumn<Shares, Double> balanceColumn;

    private static int selectedMemId;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedMemId = SharesController.selectedMemId;
        System.out.println(selectedMemId);
        //-------------- Initialize Table Columns ------------------------//
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("shaDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("shaTime"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        
        fillTable();
    }    
    
    private void fillTable() {
        try {
            shareTable.getItems().setAll(SharesDBController.getMemberShares(selectedMemId));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
