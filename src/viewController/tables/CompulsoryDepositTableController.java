/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewController.tables;

import dbController.CompulsoryDBController;
import dbController.DetailDBController;
import dbController.NormalDBController;
import dbModel.Account;
import dbModel.Compulsory;
import dbModel.Normal;
import help.dateTime;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import viewController.DepositsController;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class CompulsoryDepositTableController implements Initializable {

    /**
     * Initializes the controller class.
     */ 
    @FXML
    private TableView<Compulsory> compulsoryTable;
    
    @FXML
    private TableColumn<Compulsory, Double> amountColumn, balanceColumn;

    @FXML
    private TableColumn<Compulsory, String> dateColumn, timeColumn, detailColumn;

    @FXML
    private Button proceedBtn, deleteBtn;

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
        amountText.setText("");
        //-------------- Initialize Table Columns ------------------------//
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("comDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("comTime"));
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));    
        
        //----------- Calling Additional Methods -----------------------------//
        makeCalculation();
        proceedBtnEnable();
        validateNodes();
        fillTable();
        
    }     
    
    //---------------------- Nodes Events -------------------------------------//
    @FXML
    public void proceedBtnEvent(ActionEvent event) {
        try {   
            amount = Double.parseDouble(amountText.getText());
            if(selectedMemId != -1){               
                //---- get today Date and Time ----//
                dateTime dateTime = new dateTime();

                //---- create normal deposit object ----//
                Compulsory compulsory = new Compulsory();

                compulsory.setComId(CompulsoryDBController.getNextComId());
                compulsory.setAccId(selectedMemId);
                compulsory.setComDate(dateTime.getNowDate());
                compulsory.setComTime(dateTime.getNowTime());
                compulsory.setComType(0);
                compulsory.setAmount(amount);
                compulsory.setBalance(balance+amount);
                compulsory.setInterest(interest);

                //---- create account object ----//
                Account account = new Account();
                account.setAccId(selectedMemId);
                account.setComBalance(balance+amount);
                
                if(CompulsoryDBController.addNewCompulsoryDeposit(compulsory, account)){
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
    //------------------------- Addtional Methods ----------------------------//
    
    private void fillTable() {
        try {
            compulsoryTable.getItems().setAll(CompulsoryDBController.getCompulsoryDeposits(selectedMemId));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(NormalDepositTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    private void proceedBtnEnable() {
       boolean action1 = amountText.getText().trim().equals("");            
       if(action1){
           proceedBtn.setDisable(true);
       }else{
           proceedBtn.setDisable(false);      
       }
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
    
     private void makeCalculation() {
        try {
            Compulsory compulsory = CompulsoryDBController.getNextInterestDetail(selectedMemId);
            
            //---------- Calculate Transaction Interest ------------------//
            if(compulsory != null){
                int numOfDates = compulsory.getNumOfDates();                   
                balance = compulsory.getBalance();
                double rate = DetailDBController.getDetail(2);
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
