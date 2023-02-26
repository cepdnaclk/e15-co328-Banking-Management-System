package viewController;

import animation.FadeUp;
import dbController.CompulsoryDBController;
import dbController.FixedDBController;
import dbController.LoanTakenDBController;
import dbController.NormalDBController;
import dbModel.LoanTaken;
import help.dateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import reportBeans.FixedScheduleDeposit;
import reportBeans.LoanSchedule;
import reportBeans.ScheduleDeposit;
import viewController.sub.TakeLoanController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class ReportsController implements Initializable {
    //--------------------------- FXML Attributes ----------------------------//
    @FXML
    private Button printBtn;
    
    @FXML
    private HBox depositHBox, loanHBox, rangeDateBox;
  
    @FXML
    private VBox singleDateBox, fixedTypeBox, loanModelBox;
    
    @FXML
    private AnchorPane changePane;
     
    @FXML
    private DatePicker singleDatePick;

    @FXML
    private TableView<?> reportTable;

    @FXML
    private Button refreshBtn;

    @FXML
    private ComboBox<String> designCombo, dateTypeCombo, reportTypeCombo, depositScheduleCombo, clientTypeCombo, fixedTypeCombo;

    @FXML
    private ComboBox<String> loanScheduleCombo, loanModelCombo;
            
    @FXML
    private TextField todayDateText, todayTimeText;
    
    @FXML
    private Button cancelBtn;

    @FXML
    private CheckBox previewCheck;

    //---------------------- Normal Attributes -------------------------------//
    
    private Pane pane;
    private static AnchorPane mainPain;
    private ObservableList<LoanTaken> clientLoans;
    //---------------------- Initialize and Startup Actions ------------------//
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainPain = SanasaController.mainPane;      
        prepareNodes();
        prepareReportType();
        prepareDatePickers();
        setDateTime();
        System.out.println();
    }    
    
    //---------------------- Nodes Events -------------------------------------//
    @FXML
    public void reportTypeComboEvent(ActionEvent event) {
        prepareReportType();
    }
    
    @FXML
    public void dateTypeComboEvent(ActionEvent event) {
        prepareDatePickers();
    }
    
    @FXML
    public void depositScheduleComboEvent(ActionEvent event) {
        prepareDepositSchedule();
       
    }
    
    @FXML
    public void loanScheduleComboEvent(ActionEvent event) {
        prepareLoanSchedule();
    }
    
    @FXML
    public void printBtnEvent(ActionEvent event) throws ClassNotFoundException, SQLException{
        setDateTime();
        switch(reportTypeCombo.getSelectionModel().getSelectedIndex()){
            case 0:  
                showDepositSchedule();
                break;
            case 1:     
                showLoanSchedule();
                break;
            case 2:  
                showSharesSchedule();
                break;
            case 3:
                showLoanProfit();
                break;
            case 4:
                showClientReport();
        }
    } 
    
    @FXML
    public void cancelBtnEvent(ActionEvent event) {
        try {
            mainPain.getChildren().setAll(getContent("dashboard.fxml"));
            new FadeUp(mainPain, 20, 0);
        } catch (IOException ex) {
            Logger.getLogger(TakeLoanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //------------------------- Addtional Methods ----------------------------//
    
    private Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/" + a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }

    private void setDateTime() {
        dateTime dateTime = new dateTime();
        todayDateText.setText(dateTime.getNowDate());
        todayTimeText.setText(dateTime.getNowTime());
    }
    
    private void prepareReportType() {
        depositHBox.setVisible(false);
        loanHBox.setVisible(false);          
        prepareLoanSchedule();
        switch(reportTypeCombo.getSelectionModel().getSelectedIndex()){
            case 0:             
                depositHBox.setVisible(true); 
                dateTypeCombo.getItems().setAll("Single");
                dateTypeCombo.getSelectionModel().select(0);
                prepareDepositSchedule();
                break;
            case 1:
                loanHBox.setVisible(true);   
                dateTypeCombo.getItems().setAll("Single");
                dateTypeCombo.getSelectionModel().select(0);
                singleDatePick.setDisable(true);
                break;
            case 2:
                dateTypeCombo.getItems().setAll("Single","Range");
                dateTypeCombo.getSelectionModel().select(0);
                singleDatePick.setDisable(false);
                break;
            case 3:
                singleDatePick.setDisable(false);
                break;
            case 4:
                singleDatePick.setDisable(false);
        }
    } 

    private void prepareDatePickers() {
         switch(dateTypeCombo.getSelectionModel().getSelectedIndex()){
            case 0: 
                System.out.println("0");
                singleDateBox.setVisible(true);
                rangeDateBox.setVisible(false);
                break;
            case 1:
                System.out.println("1");
                singleDateBox.setVisible(false);
                rangeDateBox.setVisible(true);        
        }       
    }

    private void prepareDepositSchedule() {
         switch(depositScheduleCombo.getSelectionModel().getSelectedIndex()){
            case 0:    
                fixedTypeBox.setVisible(false);
                singleDatePick.setDisable(false);
                clientTypeCombo.getItems().setAll("All",
                                                  "Member",
                                                  "Non-Member");
                clientTypeCombo.getSelectionModel().select(0);
                break;
            case 1:
                fixedTypeBox.setVisible(false);
                singleDatePick.setDisable(false);
                clientTypeCombo.getItems().setAll("Member");
                clientTypeCombo.getSelectionModel().select(0);
                break;
            case 2:
                fixedTypeBox.setVisible(true);
                singleDatePick.setDisable(true);
                singleDatePick.setValue(LocalDate.now());
                clientTypeCombo.getItems().setAll("All",
                                                  "Member",
                                                  "Non-Member");
                clientTypeCombo.getSelectionModel().select(0);
        }
    }
      
    private void prepareLoanSchedule() {
        switch(loanScheduleCombo.getSelectionModel().getSelectedIndex()){
            case 0:    
                loanModelBox.setVisible(false);
                break;
            case 1:
                loanModelBox.setVisible(true);
        }
    }

    private void prepareNodes() {
        try {
            fixedTypeBox.setVisible(false);
            loanModelBox.setVisible(false);
            reportTypeCombo.getItems().setAll("Deposit Schedules",
                    "Loan Schedules",
                    "Shares Schedules",
                    "Loan Profit",
                    "Client Report");
            
            dateTypeCombo.getItems().setAll("Single","Range");
            
            depositScheduleCombo.getItems().setAll("Normal Deposit Schedule",
                    "Compulsory Deposit Schedule",
                    "Fixed Deposit Schedule");
            
            clientTypeCombo.getItems().setAll("All",
                    "Member",
                    "Non-Member");
            
            designCombo.getItems().setAll("Header","Non Header");
            
            fixedTypeCombo.getItems().setAll("Both","Only Withdraw","Not Withdraw");
            
            loanScheduleCombo.getItems().setAll("All Models", "Single Models");
            
            clientLoans = LoanTakenDBController.getclientLoans();
            for (LoanTaken clientLoan : clientLoans) {
                loanModelCombo.getItems().add(clientLoan.getLoanName());
            }
            reportTypeCombo.getSelectionModel().select(0);
            dateTypeCombo.getSelectionModel().select(0);
            depositScheduleCombo.getSelectionModel().select(0);
            clientTypeCombo.getSelectionModel().select(0);
            designCombo.getSelectionModel().select(0);
            fixedTypeCombo.getSelectionModel().select(0);
            loanScheduleCombo.getSelectionModel().select(0);
            loanModelCombo.getSelectionModel().select(0);
            singleDatePick.setValue(LocalDate.now());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String fixedLengthString(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }
    
    //------------------------- Print Reports Methods ------------------------//

    private void showDepositSchedule() throws ClassNotFoundException, SQLException {        
        String clientType = "";
        String fixedType = "";
        double total = 0;
        ObservableList<ScheduleDeposit> deSchedules = null;
        ObservableList<FixedScheduleDeposit> feSchedules = null;
        HashMap<String,Object> map = new HashMap<>();
        
        map.put("name", depositScheduleCombo.getSelectionModel().getSelectedItem());
        map.put("dateText", "Date: " + todayDateText.getText());
        map.put("timeText","Time: " + todayTimeText.getText());
        
        switch(clientTypeCombo.getSelectionModel().getSelectedItem()){
            case "All": 
                clientType = "";   
                map.put("clientType","Client Type: All");
                break;
            case "Member": 
                clientType = "and isMember = true";  
                map.put("clientType","Client Type: Members");
                break;
            case "Non-Member": 
                clientType = "and isMember = false"; 
                map.put("clientType","Client Type: Non-Members");
        }
        
        switch(fixedTypeCombo.getSelectionModel().getSelectedIndex()){
            case 0: 
                fixedType = "";   
                map.put("fixedType","Withdraw and Not Withdraw");
                break;
            case 1: 
                fixedType = "and isWithdraw = true ";  
                map.put("fixedType","Only Withdraw");
                break;
            case 2: 
                fixedType = "and isWithdraw = false "; 
                map.put("fixedType","Only not Withdraw");
        }
        
        switch(depositScheduleCombo.getSelectionModel().getSelectedIndex()){
            case 0:
                deSchedules = NormalDBController.getNormalSchedule(singleDatePick.getValue().toString(), clientType);
                for (ScheduleDeposit deSchedule : deSchedules) {
                    total +=deSchedule.getBalance();
                }
                map.put("records","Number Of Records : " + deSchedules.size());
                map.put("totalBalance","Total Balance : "+ total);
                if(designCombo.getSelectionModel().getSelectedIndex() == 0){
                    viewReport(getClass().getResourceAsStream("/reports/DepositScheduleHeader.jrxml"), map, deSchedules);
                }else{
                    viewReport(getClass().getResourceAsStream("/reports/DepositSchedule.jrxml"), map, deSchedules);
                }               
                break;
            case 1:
                deSchedules = CompulsoryDBController.getComSchedule(singleDatePick.getValue().toString());
                for (ScheduleDeposit deSchedule : deSchedules) {
                    total +=deSchedule.getBalance();
                }
                map.put("records","Number Of Records : " + deSchedules.size());
                map.put("totalBalance","Total Balance : "+ total);
                if(designCombo.getSelectionModel().getSelectedIndex() == 0){
                    viewReport(getClass().getResourceAsStream("/reports/DepositScheduleHeader.jrxml"), map, deSchedules);
                }else{
                    viewReport(getClass().getResourceAsStream("/reports/DepositSchedule.jrxml"), map, deSchedules);
                }       
                break;
            case 2:
                feSchedules = FixedDBController.getFixSchedule(clientType, fixedType);
                for (FixedScheduleDeposit feSchedule : feSchedules) {
                    total +=feSchedule.getAmount();
                }
                map.put("records","Number Of Records : " + feSchedules.size());
                map.put("totalBalance","Total Balance : "+ total);
                if(designCombo.getSelectionModel().getSelectedIndex() == 0){
                    viewReport(getClass().getResourceAsStream("/reports/FixedDepositScheduleHeader.jrxml"), map, feSchedules);
                }else{
                    viewReport(getClass().getResourceAsStream("/reports/FixedDepositSchedule.jrxml"), map, feSchedules);
                }       
                break;
        }
                   
    }

    private void showLoanSchedule() throws ClassNotFoundException, SQLException {
        int columnLength = 14;
        double total = 0;
        ObservableList<Integer> debtors = LoanTakenDBController.getDebtors();
        ObservableList<LoanSchedule> loanSchedules = FXCollections.observableArrayList();
        
        String heading = fixedLengthString("ID", columnLength);
        HashMap<String,Object> map = new HashMap<>();
        
        
        map.put("dateText", "Date: " + todayDateText.getText());
        map.put("timeText","Time: " + todayTimeText.getText());
        
        switch(loanScheduleCombo.getSelectionModel().getSelectedIndex()){
            case 0:
                for (LoanTaken clientLoan : clientLoans) {
                    heading += fixedLengthString(clientLoan.getLoanName().split(" ")[0], 12);
                }
                map.put("heading", heading);
                for (Integer debtor : debtors) {
                    String record = fixedLengthString(debtor + "", columnLength);
                    ObservableList<LoanTaken> loans = LoanTakenDBController.getClientLoans(debtor);
                    for (LoanTaken clientLoan : clientLoans) {
                        int loanId = clientLoan.getTakenId();
                        boolean b = false;
                        for (LoanTaken loan : loans) {
                            if (loanId == loan.getTakenId()) {
                                record += fixedLengthString(loan.getBalance() + "", columnLength);
                                b = true;
                                break;
                            }
                        }
                        if (!b) {
                            record += fixedLengthString("", columnLength);
                        }
                    }
                    LoanSchedule loanSchedule = new LoanSchedule();
                    loanSchedule.setRecord(record);
                    loanSchedules.add(loanSchedule);
                }
                map.put("name", "Summary Of Loan Schedule");
                map.put("records","Number Of Records : " + debtors.size());
                map.put("clientType","All Loan Models With Loans");
                if(designCombo.getSelectionModel().getSelectedIndex() == 0){
                    viewReport(getClass().getResourceAsStream("/reports/LoanScheduleHeader.jrxml"), map, loanSchedules);
                }else{
                    viewReport(getClass().getResourceAsStream("/reports/LoanSchedule.jrxml"), map, loanSchedules);
                }       
                break;
            case 1:
                ObservableList<LoanSchedule> loanModelSchedule = LoanTakenDBController.getLoanModelSchedule(loanModelCombo.getSelectionModel().getSelectedItem());
                for (LoanSchedule loanSchedule : loanModelSchedule) {
                    total += loanSchedule.getBalance();
                }
                map.put("name", loanModelCombo.getSelectionModel().getSelectedItem()+" Schedule");
                map.put("records","Number Of Records : " + loanModelSchedule.size());
                map.put("totalBalance","Total Balance : "+ total);
                if(designCombo.getSelectionModel().getSelectedIndex() == 0){
                    viewReport(getClass().getResourceAsStream("/reports/LoanModelScheduleHeader.jrxml"), map, loanModelSchedule);
                }else{
                    viewReport(getClass().getResourceAsStream("/reports/LoanModelSchedule.jrxml"), map, loanModelSchedule);
                }       
                
        }
    }

    private void showSharesSchedule() {
        
    }

    private void showLoanProfit() {
        
    }

    private void showClientReport() {
        
    }   
    
    private void viewReport(InputStream st, HashMap<String, Object> map, ObservableList<?> list) {
        try {            
            JasperReport cReport = JasperCompileManager.compileReport(st);           
            JasperPrint pReport = JasperFillManager.fillReport(cReport, map ,new JRBeanCollectionDataSource(list));
            JasperViewer.viewReport(pReport, false);
        } catch (JRException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
   
}
