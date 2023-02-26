
package viewController.tables;

import dbController.DetailDBController;
import dbController.NormalDBController;
import dbModel.Account;
import dbModel.Normal;
import help.dateTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import viewController.DepositsController;

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
public class NormalDepositTableController implements Initializable {
    
    @FXML
    private TableView<Normal> normalTable;
    
    @FXML
    private TableColumn<Normal, Double> balanceColumn;
    
    @FXML
    private TableColumn<Normal, String> dateColumn, timeColumn, detailColumn, depositColumn, withdrawColumn;

    @FXML
    private ComboBox<String> actionCombo;

    @FXML
    private Button proceedBtn, clearBtn;

    @FXML
    private TextField amountText;

    private static int selectedMemId;
    private static final double yDates = 365.0;
    private int norType;
    private double amount;
    private double balance;
    private double interest;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedMemId = DepositsController.selectedMemId;

        //-------------- Initialize Table Columns ------------------------//
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("norDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("norTime"));
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        depositColumn.setCellValueFactory(new PropertyValueFactory<>("deposit"));
        withdrawColumn.setCellValueFactory(new PropertyValueFactory<>("withDraw"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        //----------- Calling Additional Methods -----------------------------//
        makeCalculation();
        prepareNodes();
        validateNodes();
        fillTable();
    }    
    
    //---------------------- Nodes Events -------------------------------------//
    @FXML
    public void proceedBtnEvent(ActionEvent event) {
        try {   
            amount = Double.parseDouble(amountText.getText());
            switch (actionCombo.getSelectionModel().getSelectedIndex()) {
                case 0:
                    break;
                case 1:
                    amount = -(amount);
            }
            if(selectedMemId != -1){               
                //---- get today Date and Time ----//
                dateTime dateTime = new dateTime();

                //---- create normal deposit object ----//
                Normal nextNormal = new Normal();

                nextNormal.setNorId(NormalDBController.getNextNormalId());
                nextNormal.setAccId(selectedMemId);
                nextNormal.setNorDate(dateTime.getNowDate());
                nextNormal.setNorTime(dateTime.getNowTime());
                nextNormal.setNorType(norType);
                nextNormal.setAmount(amount);
                nextNormal.setBalance(balance+amount);
                nextNormal.setInterest(interest);

                //---- create account object ----//
                Account account = new Account();
                account.setAccId(selectedMemId);
                account.setNorBalance(balance+amount);
                
                if(NormalDBController.addNewNormalDeposit(nextNormal, account)){
                    fillTable();
                    DepositsController.setDepositSummary();
                    System.out.println("sucess....:");
                }
                amountText.setText("");
                proceedBtnEnable();
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void actionComboEvent(ActionEvent event) {
        amountText.setText("");
        switch (actionCombo.getSelectionModel().getSelectedIndex()) {
            case 0:
                norType = 0;               
                break;
            case 1:
                norType = 2;
        }
    }
    //------------------------- Addtional Methods ----------------------------//
    
    private void fillTable() {
        try {
            normalTable.getItems().setAll(NormalDBController.getNormalDeposits(selectedMemId));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void prepareNodes() {
        actionCombo.getItems().setAll("Deposit","Withdraw");
        actionCombo.getSelectionModel().select(0);
        amountText.setText("");
        proceedBtnEnable();
    }   

    private void validateNodes() {
        amountText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[0-9]")) {
                event.consume();
            }
        });
        
        amountText.setOnKeyReleased((KeyEvent event) -> {
             if(!amountText.getText().trim().equals("") && Double.parseDouble(amountText.getText()) == 0){
                 amountText.setText("");
             }
             proceedBtnEnable(); 
        });
    }

    private void proceedBtnEnable() {
       boolean action1 = amountText.getText().trim().equals("");
        try {
            if (action1) {
                proceedBtn.setDisable(true);
            } else if (norType == 2 && Double.parseDouble(amountText.getText()) >= balance - DetailDBController.getDetail(6)) {
                proceedBtn.setDisable(true);
            } else {
                proceedBtn.setDisable(false);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void makeCalculation() {
        try {
            Normal normal = NormalDBController.getNextInterestDetail(selectedMemId);
            
            //---------- Calculate Transaction Interest ------------------//
            if(normal != null){
                int numOfDates = normal.getNumOfDates();                   
                balance = normal.getBalance();
                double rate = DetailDBController.getDetail(1);
                interest = balance*(rate/(100*yDates))*numOfDates;
            }else{      
                interest = 0;
                balance  = 0;
            }   
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
