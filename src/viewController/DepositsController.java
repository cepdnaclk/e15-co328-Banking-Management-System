/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package viewController;

import animation.FadeInLeft;
import animation.FadeInRight;
import dbController.AccountDBController;
import dbController.CompulsoryDBController;
import dbController.DetailDBController;
import dbController.FixedDBController;
import dbController.MemberDBController;
import dbController.NormalDBController;
import dbModel.Account;
import dbModel.Compulsory;
import dbModel.Member;
import dbModel.Normal;
import help.dateTime;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import viewController.sub.SummaryController;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class DepositsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField memberIdText, statusText, typeText, nameText;
    
    @FXML
    private ComboBox<String> searchClientCombo;
    
    @FXML
    private Label label1, label2, label3; 

    @FXML
    public AnchorPane tablePane;
    
    private Pane pane;
    public static AnchorPane mainPane;
    public static int selectedMemId;
    private static int position;
    private static int tableCount;
    private static final double yDates = 365.0;
    private static ObservableList<String> result;
    private static String selectClient;
    private static Label labelStatic1, labelStatic2, labelStatic3;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainPane = tablePane;
        labelStatic1 = label1;
        labelStatic2 = label2;
        labelStatic3 = label3;
        tableCount = 0;
        position = 0;        
        //----------- Calling Additional Methods -----------------------------//
         Platform.runLater(()-> {
            prepareClient();
            viewTables(true);
            searchableCombo();
        });       
    }    
    
    //---------------------- Nodes Events -------------------------------------//
    
    @FXML
    public void navigateEvent(ActionEvent event) {
        Button btn = (Button) event.getSource();
        if(btn.getId().equals("rightBtn")){
            position = (position == tableCount) ? 0 : ++position ;
            viewTables(true);
        }else{
            position = (position == 0) ? tableCount : --position ;
            viewTables(false);
        }
    }
    
    //------------------------- Addtional Methods ----------------------------//

    private void viewTables(boolean direction) {
        try{
            switch(position){
                case 0:
                    tablePane.getChildren().setAll(getContent("normalDepositTable.fxml"));                    
                    break;
                case 1:
                    tablePane.getChildren().setAll(getContent("fixedDepositTables.fxml"));                   
                    break;
                case 2:
                    tablePane.getChildren().setAll(getContent("compulsoryDepositTable.fxml"));                       
            }
            
            if(direction){
                new FadeInRight(tablePane,20,0);
            }else{
                new FadeInLeft(tablePane,20,0);
            }
            
        }catch(IOException ex){
            Logger.getLogger(DepositsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/tables/"+ a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }

    private void prepareClient(){
        try {                    
            Member member = MemberDBController.searchMemberFromId(selectedMemId);            
            if (member == null) {
                System.out.println("No Such Customer");
            } else {
                String type = "";
                
                if (member.isIsMember()) {
                    type = "Member";
                } else if (member.isIsApplicant()) {
                    type = "Applicant";
                } else {
                    type = "Non Member";
                }

                memberIdText.setText(member.getMemId() + "");
                statusText.setText((member.isIsActive()) ? "Active" : "Inactive");
                typeText.setText(type);
                nameText.setText(member.getName());
                
                position = member.isIsMember() ? position : 0;
                tableCount = member.isIsMember() ? 2 : 1 ;
                calculateNormalInterest();
                calculateFixedInterest();
                if(member.isIsMember()){
                    calculateCompulsoryInterest();
                }
                viewTables(true);
                setDepositSummary();
            }
            searchClientCombo.getEditor().setText("");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SummaryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
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
                    
                    String[] breaks = selectClient.split(" ");
                    selectedMemId = Integer.parseInt(breaks[0]);
                    prepareClient();
                    searchClientCombo.getSelectionModel().clearSelection();
                    searchClientCombo.getEditor().setText("");
                    event.consume();
                }
            } catch (NumberFormatException ex) {
                searchClientCombo.getEditor().setText("");
            } catch (NullPointerException ex){
                searchClientCombo.getEditor().setText("");
            }          
        });    
    }   
    
    private void calculateNormalInterest() {
        try {
            if(NormalDBController.getLastInterestDayCount(selectedMemId) >= DetailDBController.getDetail(4)){   //30
                
                Normal normal = NormalDBController.getNextInterestDetail(selectedMemId);
                if(normal != null){
                    int accId = normal.getAccId();
                    int numOfDates = normal.getNumOfDates();
                    double interest = normal.getInterest();
                    double balance = normal.getBalance();

                    double rate = DetailDBController.getDetail(1);

                    //---- interest calculate equation ----//
                    double finalInterest = balance*(rate/(100*yDates))*numOfDates;
                    
                    System.out.println("Normal : "+ (interest+finalInterest));
                    if((interest+finalInterest)>=DetailDBController.getDetail(5)){   //300
                        //---- get today Date and Time ----//
                        dateTime dateTime = new dateTime();
                        
                        //---- create normal deposit object ----//
                        Normal nextNormal = new Normal();

                        nextNormal.setNorId(NormalDBController.getNextNormalId());
                        nextNormal.setAccId(accId);
                        nextNormal.setNorDate(dateTime.getNowDate());
                        nextNormal.setNorTime(dateTime.getNowTime());
                        nextNormal.setNorType(1);
                        nextNormal.setAmount(interest+finalInterest);
                        nextNormal.setBalance(balance+interest+finalInterest);
                        nextNormal.setInterest(-interest);
                        
                        //---- create account object ----//
                        Account  account = new Account ();
                        account.setAccId(accId);
                        account.setNorBalance(balance+interest+finalInterest);
                        
                        if(NormalDBController.addNewNormalDeposit(nextNormal, account)){
                            System.out.println("Interest Auto Generated..!");
                        }
                    }else{
                        System.out.println("end");
                        return;
                    }  
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DepositsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DepositsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void calculateCompulsoryInterest() {
        try {
            if(CompulsoryDBController.getLastInterestDayCount(selectedMemId)>=DetailDBController.getDetail(4)){  //30
                
                Compulsory compulsory = CompulsoryDBController.getNextInterestDetail(selectedMemId);
                int accId = compulsory.getAccId();
                int numOfDates = compulsory.getNumOfDates();
                double interest = compulsory.getInterest();
                double balance = compulsory.getBalance();
                
                double rate = DetailDBController.getDetail(2);
                
                //---- interest calculate equation ----//
                double finalInterest = balance*(rate/(100*yDates))*numOfDates;
                
                System.out.println(balance);
                System.out.println(numOfDates);
                System.out.println("Compulsory : "+ (interest + finalInterest));
                if ((interest + finalInterest) >= 100) {   //300   DetailDBController.getDetail(5)
                    //---- get today Date and Time ----//
                    dateTime dateTime = new dateTime();
                    System.out.println("entered");
                    //---- create normal deposit object ----//
                    Compulsory nextCompulsory = new Compulsory();

                    nextCompulsory.setComId(CompulsoryDBController.getNextComId());
                    nextCompulsory.setAccId(accId);
                    nextCompulsory.setComDate(dateTime.getNowDate());
                    nextCompulsory.setComTime(dateTime.getNowTime());
                    nextCompulsory.setComType(1);
                    nextCompulsory.setAmount(interest + finalInterest);
                    nextCompulsory.setBalance(balance + interest + finalInterest);
                    nextCompulsory.setInterest(-interest);

                    //---- create account object ----//
                    Account account = new Account();
                    account.setAccId(accId);
                    account.setComBalance(balance + interest + finalInterest);

                    if (CompulsoryDBController.addNewCompulsoryDeposit(nextCompulsory, account)) {
                        System.out.println("Interest Auto Generated..!");
                    }
                } else {
                    System.out.println("end");
                    return;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DepositsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DepositsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   

    private void calculateFixedInterest() {
        System.out.println("fixed cal code DepositController");
    }

    public  static void setDepositSummary() {
        try {
            labelStatic1.setText(AccountDBController.getNormalBalance(selectedMemId)+"");
            labelStatic2.setText(AccountDBController.getCompulsoryBalance(selectedMemId)+"");
            labelStatic3.setText(FixedDBController.getActiveFixedDepositCount(selectedMemId)+"");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DepositsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DepositsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
}
