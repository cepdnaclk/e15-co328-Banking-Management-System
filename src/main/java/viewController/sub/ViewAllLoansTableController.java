
package viewController.sub;

import dbController.LoanDBController;
import dbModel.Loan;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewAllLoansTableController implements Initializable {
    @FXML
    private TableView<Loan> viewLoanTable;
    
    @FXML
    private TableColumn<Loan,String> methodColumn;

    @FXML
    private TableColumn<Loan, String> nameColumn;

    @FXML
    private TableColumn<Loan, Double> rateColumn;

    @FXML
    private TableColumn<Loan,String> maxColumn;

    @FXML
    private TableColumn<Loan, String> idColumn;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillTable();
        setTable();
    }    
    
    private void fillTable() {
        try {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("loanId"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("loanName"));
            rateColumn.setCellValueFactory(new PropertyValueFactory<>("rate"));
            maxColumn.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));
            methodColumn.setCellValueFactory(new PropertyValueFactory<>("methodName"));
            
            viewLoanTable.setItems(LoanDBController.getAllLoanModel()); 
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewAllMembersTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewAllMembersTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    private void setTable() {
        viewLoanTable.setRowFactory((TableView<Loan> tableView) -> {
            TableRow<Loan> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("Delete");
            menuItem.setOnAction((ActionEvent event) -> {
                try {
                    Loan selectLoanModel = viewLoanTable.getSelectionModel().getSelectedItem();
                    int loanId = selectLoanModel.getLoanId();
                    
                    if (LoanDBController.deleteLoanModel(loanId)) {
                        System.out.println("Safely Deleted....!");
                    }else{
                        System.out.println("Delete Unsucessfull...!");
                    }
                    viewLoanTable.getItems().remove(row.getItem());
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(ViewAllLoansTableController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            contextMenu.getItems().addAll(menuItem);
            
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            
            return row ;
        });  
    }
}
