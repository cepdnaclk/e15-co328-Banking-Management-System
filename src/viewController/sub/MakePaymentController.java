
package viewController.sub;

import dbController.LoanTakenDBController;
import dbController.MemberDBController;
import dbController.PaymentDBController;
import dbModel.Member;
import dbModel.Payment;
import dbSubModel.NextPayment;
import dbSubModel.PreviousPayments;
import help.MessageBox;
import help.dateTime;
import java.io.InputStream;


import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import viewController.ReportsController;

public class MakePaymentController implements Initializable {
    
    //--------------------------- FXML Attributes ----------------------------//
    
    @FXML
    private TextField memberIdText, nameText, todayDateText, todayTimeText, amountText, balanceText, finalBalanceText, totalText;

    @FXML
    private TextField comText, norText, sharesText;
            
    @FXML
    private Button rightBtn, proceedBtn, leftBtn, cancelBtn, calculateBtn, refreshBtn;

    @FXML
    private Label label1, label2, label3, label4, label5, label6, label7;

    @FXML
    private Label paymentName;

    @FXML
    private Pane depositPane;
    
    @FXML
    private ToggleButton yesToggle;

    @FXML
    private TableView<NextPayment> totalTable;
    
    @FXML
    private TableColumn<NextPayment, Double> paymentColumn;
      
    @FXML
    private TableView<NextPayment> loanTable;

    @FXML
    private TableColumn<NextPayment, Double> amountColumn, balanceColumn, interestColumn, fineColumn, installmentColumn;
   
    @FXML
    private TableColumn<NextPayment, String> loanColumn;
    
    @FXML
    private TableView<PreviousPayments> paymentsShowTable;
    
    @FXML
    private TableColumn<PreviousPayments, String> dateShowColumn, loanShowColumn;
    
    @FXML
    private TableColumn<PreviousPayments, Double> interestShowColumn, fineShowColumn, installmentShowColumn;
    
    //---------------------- Normal Attributes -------------------------------//
    private static int selectedLoan;
    private static int debtorId;
    private static ObservableList<NextPayment> allNextPayments;
    private static ObservableList<Payment> paymentList = FXCollections.observableArrayList();
    private static Member member;
    private static DecimalFormat numFormat = new DecimalFormat("0.00"); 
    private boolean isBalance = true;
    private boolean isFinalBalance = true;
    //---------------------- Initialize and Startup Actions ------------------//
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prepareNode();
        debtorId = SummaryController.selectedMemId;
        
        try {
            Callback<TableColumn<NextPayment,Double>, TableCell<NextPayment, Double>> cellFactory = (TableColumn<NextPayment, Double> p) -> new EditingCell();
            
            //-------------- Initialize Table Columns ------------------------//   
            loanColumn.setCellValueFactory(new PropertyValueFactory<>("loanName"));          
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
            balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
            interestColumn.setCellValueFactory(new PropertyValueFactory<>("interestAmount"));
            fineColumn.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));
            installmentColumn.setCellValueFactory(new PropertyValueFactory<>("installAmount"));
            
            paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payAmount"));
            
            dateShowColumn.setCellValueFactory(new PropertyValueFactory<>("payDate"));
            loanShowColumn.setCellValueFactory(new PropertyValueFactory<>("loanName"));
            interestShowColumn.setCellValueFactory(new PropertyValueFactory<>("interest"));
            fineShowColumn.setCellValueFactory(new PropertyValueFactory<>("fine"));
            installmentShowColumn.setCellValueFactory(new PropertyValueFactory<>("installAmount"));
            
            installmentColumn.setCellFactory(cellFactory);
       
            installmentColumn.setOnEditCommit((TableColumn.CellEditEvent<NextPayment, Double> event) -> {
                String total = numFormat.format(Double.parseDouble(totalText.getText()) - event.getOldValue() + event.getNewValue());
                totalText.setText(total);
                double balance = -1;
                if(!amountText.getText().trim().equals("")){
                    balance = Double.parseDouble(amountText.getText()) - Double.parseDouble(totalText.getText());
                }
                if (balance >= 0) {
                balanceText.setText(numFormat.format(balance));
                isBalance = false;
                } else {
                balanceText.setText("Insufficient");
                isBalance = true;
                }
                allNextPayments.get(event.getTableView().getSelectionModel().getSelectedIndex()).setInstallAmount(event.getNewValue());

                totalTable.getItems().setAll(allNextPayments);
            });
           
            //---------------Fill Customer Detail-----------------------------//
            member = MemberDBController.searchMemberFromId(debtorId);
            
            memberIdText.setText(member.getMemId()+"");
            nameText.setText(member.getName());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setDateTime();
        fillTables();
        prepareTotal();
        setloanTableMenu();
        showLastPayment();
        fillPaymentsShowTable();
        proceedBtnEnable();
        validateNodes();
    }    
    
    //---------------------- Nodes Events ------------------------------------//
    @FXML
    private void proceedBtnEvent(ActionEvent event) {
        try {
            setDateTime();
            //---------------------- create payment list ---------------------//
            int payId = PaymentDBController.getNextPaymentId();
            System.out.println(payId);
            for (NextPayment nextPayment : allNextPayments) {
                Payment payDetail = new Payment();
                payDetail.setPayId(payId);
                payDetail.setTakenId(nextPayment.getTakenId());
                payDetail.setPayDate(todayDateText.getText());
                payDetail.setPayTime(todayTimeText.getText());
                payDetail.setAmount(nextPayment.getPayAmount());
                payDetail.setFine(nextPayment.getFineAmount());
                payDetail.setInterest(nextPayment.getInterestAmount());
                
                paymentList.add(payDetail);
                double newLoanBalance = nextPayment.getBalance()-(nextPayment.getPayAmount()-nextPayment.getFineAmount()-nextPayment.getInterestAmount());
                LoanTakenDBController.updateMemberLoanBalance(nextPayment.getTakenId() , newLoanBalance);
                ++payId;
            }
            if(PaymentDBController.addNewPayments(paymentList)){               
                makeReport();
                MessageBox.showMessageBox("Make Payment","Sucsessfully Added Your Payment...!");
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void refreshBtnEvent(ActionEvent event) {
        fillTables();
        prepareTotal();
        prepareBalance();
        proceedBtnEnable();
    }
    
    @FXML
    private void cheakEvent(ActionEvent event) {        
        CheckBox check = (CheckBox) event.getSource();
        switch(check.getId()){
            case "comCheak":
                comText.setDisable(!check.isSelected());
                comText.setText("");
                break;
            case "norCheak":
                norText.setDisable(!check.isSelected());
                norText.setText("");
                break;
            case "sharesCheak":
                sharesText.setDisable(!check.isSelected());
                sharesText.setText("");
        }
        proceedBtnEnable();
    }
    
    @FXML
    private void calculateBtnEvent(ActionEvent event) {
        prepareBalance();
        proceedBtnEnable();
    }
    
    @FXML
    private void yesToggleEvent(ActionEvent event) {
        proceedBtnEnable();
        if(yesToggle.isSelected()){
            depositPane.setVisible(true);
        }else{
            depositPane.setVisible(false);
        }
        finalBalanceText.setText("");
        norText.setText("");
        comText.setText("");
        sharesText.setText("");    
    }
    
    //------------------------- Addtional Methods ----------------------------//
    
    private void setDateTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        todayDateText.setText(dateFormat.format(currentDate));
        todayTimeText.setText(timeFormat.format(currentDate));
    }

    private void fillTables() {
        try {
            allNextPayments = PaymentDBController.getNextPayments(member.getMemId());
            
            loanTable.getItems().setAll(allNextPayments);
            totalTable.getItems().setAll(allNextPayments);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    private void fillPaymentsShowTable() {
        try {
            paymentsShowTable.getItems().setAll(PaymentDBController.getPreviousPayments(member.getMemId()));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    private void setloanTableMenu() {
        loanTable.setRowFactory((TableView<NextPayment> param) -> {
            TableRow<NextPayment> row = new TableRow<>();
            ContextMenu menu = new ContextMenu();
            MenuItem item = new MenuItem("Delete");
            
            
            item.setOnAction((ActionEvent event) ->{
                allNextPayments.remove(row.getIndex());
                loanTable.getItems().setAll(allNextPayments);
                totalTable.getItems().setAll(allNextPayments);
                prepareTotal();
                prepareBalance();
                proceedBtnEnable();
            });
            
            menu.getItems().addAll(item);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(menu)
            );
            return row;
        });
        
    }

    private void prepareTotal() {
        double total = 0;
        for (NextPayment nextPayment : allNextPayments) {
            total += nextPayment.getInterestAmount();
            total += nextPayment.getFineAmount();
            total += nextPayment.getInstallAmount();
        }
        totalText.setText(numFormat.format(total));
    }

    private void prepareBalance() {
        double totalAmount = Double.parseDouble(totalText.getText());
        double userAmount = Double.parseDouble(amountText.getText());
        double balance = userAmount-totalAmount;
        if(balance>=0){
            balanceText.setText(numFormat.format(balance));
            isBalance = false;
        }else{
            balanceText.setText("Insufficient");
            isBalance = true;
        }    
    }
    
    private void prepareFinalBalance() {
        double balanceAmount = Double.parseDouble(balanceText.getText());
        double depositAmount = 0;
        double finalBalance = 0;
        boolean action1 = !norText.isDisable() && !norText.getText().trim().equals("");
        boolean action2 = !comText.isDisable() && !comText.getText().trim().equals("");
        boolean action3 = !sharesText.isDisable() && !sharesText.getText().trim().equals("");
        if(action1){
             depositAmount += Double.parseDouble(norText.getText());
        }
        if(action2){
             depositAmount += Double.parseDouble(comText.getText());
        }
        if(action3){
             depositAmount += Double.parseDouble(sharesText.getText());
        }
        finalBalance = balanceAmount-depositAmount;
        if(finalBalance>0){
            finalBalanceText.setText(numFormat.format(finalBalance));
            isFinalBalance = false;
        }else{
            finalBalanceText.setText("Insufficient");
            isFinalBalance = true;
        }   
    }
    
    private void showLastPayment() {
        try {
            PreviousPayments lastPayment = PaymentDBController.getLastPayment(member.getMemId());
            label1.setText(lastPayment.getPayDate());
            label2.setText(lastPayment.getPayTime());
            label3.setText(lastPayment.getNumOfLoans()+"");
            label4.setText(lastPayment.getInterest()+"");
            label5.setText(lastPayment.getFine()+"");
            label6.setText(lastPayment.getInstallAmount()+"");
            label7.setText(lastPayment.getPaymentAmount()+"");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MakePaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void prepareNode() {
        depositPane.setVisible(false);        
        calculateBtn.setDisable(true);
        amountText.setText("");
        norText.setDisable(true);
        comText.setDisable(true);
        sharesText.setDisable(true);
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
             if(amountText.getText().trim().equals("")){
                 calculateBtn.setDisable(true);                
             }else{
                 calculateBtn.setDisable(false);
                 prepareBalance();
                 proceedBtnEnable();
             }
        });
        //------------------------------Nor Text---------------------------------------//
        norText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[0-9]")) {
                event.consume();
            }
        });
        
        norText.setOnKeyReleased((KeyEvent event) -> {
             if(!norText.getText().trim().equals("") && Double.parseDouble(norText.getText()) == 0){
                 norText.setText("");
             }
             prepareFinalBalance();
             proceedBtnEnable();
        });
        //---------------------------------Com Text ------------------------------------//
        comText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[0-9]")) {
                event.consume();
            }
        });
        
        comText.setOnKeyReleased((KeyEvent event) -> {
             if(!comText.getText().trim().equals("") && Double.parseDouble(comText.getText()) == 0){
                 comText.setText("");
             }
             prepareFinalBalance();
             proceedBtnEnable();
        });
        //----------------------------------Share Text -----------------------------------//
        sharesText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[0-9]")) {
                event.consume();
            }
        });
        
        sharesText.setOnKeyReleased((KeyEvent event) -> {
             if(!sharesText.getText().trim().equals("") && Double.parseDouble(sharesText.getText()) == 0){
                 sharesText.setText("");
             }
             prepareFinalBalance();
             proceedBtnEnable();
        });
    }

    private void proceedBtnEnable() {
        boolean action1 = amountText.getText().trim().equals("");
        boolean action2 = isBalance;
        boolean action3 = yesToggle.isSelected();
        boolean action4 = !norText.isDisable() && norText.getText().trim().equals("");
        boolean action5 = !comText.isDisable() && comText.getText().trim().equals("");
        boolean action6 = !sharesText.isDisable() && sharesText.getText().trim().equals("");
        boolean action7 = isFinalBalance;
        
        if(action1 || action2){
            proceedBtn.setDisable(true);
        }else{
            if(action3){
                if(action4 || action5 || action6){
                    proceedBtn.setDisable(true);
                }else{
                    if(action7){
                        proceedBtn.setDisable(true);
                    }else{
                        proceedBtn.setDisable(false);
                    }                   
                }
            }else{
                proceedBtn.setDisable(false);
            }
        }
        
        if(isBalance){
            yesToggle.setSelected(false);
            yesToggle.setDisable(true);
            depositPane.setVisible(false);
            
            finalBalanceText.setText("");
            norText.setText("");
            comText.setText("");
            sharesText.setText("");                  
        }else{
            yesToggle.setDisable(false);
        }
    }

    private void makeReport() {
        HashMap<String,Object> map = new HashMap<>();        
        map.put("memberId", "Member Id : " + debtorId);
        map.put("name", "Member Name : " + member.getName());
        map.put("dateTime", "Date : " + todayDateText.getText()+" /  Time : "+todayTimeText.getText());
        map.put("total", "Total : " + totalText.getText());
        map.put("balance", "Balance : " + balanceText.getText());
        viewReport(getClass().getResourceAsStream("/reports/paymentSheet.jrxml"), map, allNextPayments);
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
    
    //---------------------- Edit cell Object -------------------------------//
    class EditingCell extends TableCell<NextPayment, Double> {
          
        private TextField textField;
          
        public EditingCell() {}
          
        @Override
        public void startEdit() {
              
            super.startEdit();
              
            if (textField == null) {
                createTextField();
            }
              
            setGraphic(textField);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.selectAll();
        }
          
        @Override
        public void cancelEdit() {
            super.cancelEdit();
            textField.setText(getString());
            setText(String.valueOf(getItem()));
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
          
         @Override
         public void updateItem(Double item, boolean empty) {
             super.updateItem(item, empty);

             if (empty) {
                 setText(null);
                 setGraphic(null);
             } else if (isEditing()) {
                 if (textField != null) {
                     textField.setText(getString());
                 }
                 setGraphic(textField);
                 setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
             } else {
                 setText(getString());
                 setContentDisplay(ContentDisplay.TEXT_ONLY);
             }
         }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap());
            textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                  
                @Override
                public void handle(KeyEvent t) {
                    try{
                    if (t.getCode() == KeyCode.ENTER) {
                        commitEdit(Double.parseDouble(textField.getText()));
                    } else if (t.getCode() == KeyCode.ESCAPE) {
                        cancelEdit();
                    }
                    }catch(NumberFormatException ex){
                        cancelEdit();
                        System.out.println("nfe come");
                    }
                }
            });
        }
          
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
     
     
}
