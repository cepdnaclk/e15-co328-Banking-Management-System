
package viewController.sub;

import dbController.FixedDBController;
import dbController.GuarantorDBController;
import dbController.LoanDBController;
import dbController.LoanTakenDBController;
import dbController.MemberDBController;
import dbController.PaymentDBController;
import dbModel.Fixed;
import dbModel.Guarantor;
import dbModel.Loan;
import dbModel.LoanTaken;
import dbModel.Member;
import dbModel.Payment;
import help.MessageBox;
import help.dateTime;
import viewController.LoansController;
import viewController.SanasaController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TakeLoanController implements Initializable {
    
    //--------------------------- FXML Attributes ----------------------------//
    
    @FXML
    private TextField todayDateText, todayTimeText, amountText, periodText, fixedDepositText;

    @FXML
    private TextField memberIdText, typeText, statusText, nameText, dobText, nicNumText, contactText, genderText;

    @FXML
    private TextArea addressField;

    @FXML
    private Label label1, label2, label3, label4, label5, label6, label7, label8, label9, label10;

    @FXML
    private ComboBox<String> selectLoanCombo, guaranteeCombo;

    @FXML
    private Button cancelBtn, proceedBtn;

    @FXML
    private Label modelName;

    @FXML
    private TableView<Member> guaranteeTable;
    
    @FXML
    private TableView<Fixed> fixedDepositTable;
          
    @FXML
    private TableColumn<Member, String> nameColumn, idColumn;
    
    @FXML
    private TableColumn<Fixed, String> dateColumn;
    
    @FXML
    private TableColumn<Fixed, Double> amountColumn;
    
    @FXML
    private TableColumn<Fixed, Integer> periodColumn;
    
    @FXML
    private CheckBox cheak1, cheak2, cheak3, cheak4;
    
    @FXML
    private Slider periodSlider;
    
    @FXML
    private HBox periodHBox;
     
    @FXML
    private VBox addFinalDetailPane, fixDepositBox, guaranteeBox;
   
    //---------------------- Normal Attributes -------------------------------//
    
    private static int debtorId;
    private static int selectedFixedDeposit;
    private Pane pane;
    private static ObservableList<Loan> modelList;
    private static ObservableList<Member> guarantorList;
    private static ObservableList<Fixed> allFixedDeposits;
    private static ObservableList<String> result;
    private static String selectGuarantee;
    private static Loan loanModel;
    private static AnchorPane mainPain;
    
    //---------------------- Initialize and Startup Actions ------------------//
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        debtorId = SummaryController.selectedMemId;
        prepareLists();
        
        //----- Add Items To selectLoanCombo -----// 
        setLoanModelsCombo();
        
        try {
            //---------------Make some arrangements---------------------------//
            guarantorList = FXCollections.observableArrayList();
            selectLoanCombo.getSelectionModel().select(0);
            arrangeModel(selectLoanCombo.getSelectionModel().getSelectedIndex());

            //-------------- Initialize Table Columns ------------------------//
            idColumn.setCellValueFactory(new PropertyValueFactory<>("memId"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            dateColumn.setCellValueFactory(new PropertyValueFactory<>("fixDate"));
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
            periodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));
            
            //---------------Fill Customer Detail-----------------------------//
            
            Member member = MemberDBController.searchMemberFromId(debtorId);
            
            
            String type = (String) null;
            
            if(member.isIsMember()){
                type= "Member";
            }else if(member.isIsApplicant()){
                type = "Applicant";
            }else{
                type = "Non Member";
            }
            
            String[] split = member.getAddress().split(",");
            String address = split[0] + "\n";
            for (int i = 1; i < split.length; i++) {
                address = address + split[i]+ "\n";
            }
            address = address + "\b";
            
            memberIdText.setText(member.getMemId()+"");
            typeText.setText(type);
            statusText.setText((member.isIsActive()) ? "Active" : "Inactive" );
            nameText.setText(member.getName());
            addressField.setText(address);
            dobText.setText(member.getDob());
            nicNumText.setText(member.getNic());
            genderText.setText((member.isGender())?"Male":"Female");
            contactText.setText(member.getContact());
            
            //------------------- fill fixedDepositTable ---------------------//
            fixedDepositTable.getItems().setAll(allFixedDeposits);
            
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TakeLoanController.class.getName()).log(Level.SEVERE, null, ex);
        }  
        mainPain = SanasaController.mainPane;
        
        //----------- Calling Additional Methods -----------------------------//
        setDateTime();
        validationNodes();
        searchableCombo();
        setGuaranteeTableMenu();
        setFixedDepositTableMenu();
        setProceedBtnEnable();
    }    
    
    //---------------------- Nodes Events -------------------------------------//
    
    @FXML
    public void proceedBtnEvent(ActionEvent event) {
        try {
            //----------------Assign Variables-------------------------//
            int takenId = LoanTakenDBController.getNextLoanTakenId();
            int memberId = SummaryController.selectedMemId;
            String loanName = loanModel.getLoanName();
            boolean method = loanModel.isMethod();
            double rate = loanModel.getRate();
            int rateType = loanModel.getRateType();
            int fineType = loanModel.getFineType();
            int guaranteeType = loanModel.getGuaranteeType();
            String takenDate = todayDateText.getText();
            String takenTime = todayTimeText.getText();
            int period = Integer.parseInt(periodText.getText());
            double amount = Double.parseDouble(amountText.getText());
                       
            //-----------------create loan taken model--------------------------//
            LoanTaken loanTaken = new LoanTaken();
            
            loanTaken.setTakenId(takenId);
            loanTaken.setMemId(memberId);
            loanTaken.setLoanName(loanName);
            loanTaken.setMethod(method);
            loanTaken.setRate(rate);
            loanTaken.setRateType(rateType);
            loanTaken.setFineType(fineType);
            loanTaken.setGuaranteeType(guaranteeType);
            loanTaken.setTakenDate(takenDate);
            loanTaken.setTakenTime(takenTime);
            loanTaken.setPeriod(period);
            loanTaken.setAmount(amount);
            loanTaken.setBalance(amount);
            
            //---------------------- create payment list model ---------------------//
            ObservableList<Payment> paymentList = FXCollections.observableArrayList();
            Payment payDetail = new Payment();
            payDetail.setPayId(PaymentDBController.getNextPaymentId());
            payDetail.setTakenId(takenId);
            payDetail.setPayDate(takenDate);
            payDetail.setPayTime(takenTime);
            payDetail.setAmount(0);
            payDetail.setFine(0);
            payDetail.setInterest(0);
            paymentList.add(payDetail);
            
            //---------------------- create guarantee list model ---------------------//
            int guaId = GuarantorDBController.getNextGuarantorId();  
            ObservableList<Guarantor> guaList = FXCollections.observableArrayList();
            
            if (guaranteeType == 0) {
                if (LoanTakenDBController.addNewLoanTaken(loanTaken, guaList, paymentList)) {
                    System.out.println("Sucess");       //messagebox//
                } else {
                    System.out.println("Unsucess");     //messagebox//
                }
            } else if (guaranteeType == 3) {
                int selectedFixId = allFixedDeposits.get(selectedFixedDeposit).getFixId();
                //---- create fixed deposit guarantee detail ----//
                Guarantor guarantor = new Guarantor();
                guarantor.setGuaId(guaId);
                guarantor.setTakenId(takenId);
                guarantor.setMemId(selectedFixId);
                guaList.add(guarantor);
                if (LoanTakenDBController.addNewLoanTaken(loanTaken, guaList, paymentList) && FixedDBController.makeFixedDepositBond(selectedFixId)) {
                    MessageBox.showMessageBox("Take A New Loan","Sucsessfully Added Your New Loan...!");
                } else {
                    MessageBox.showMessageBox("Take A New Loan","Unsucsessfulled To Do Your Task...!");
                }
            } else {
                //---- create members guarantee detail ----//
                for (Member member : guarantorList) {
                    Guarantor guarantor = new Guarantor();
                    guarantor.setGuaId(guaId);
                    guarantor.setTakenId(takenId);
                    guarantor.setMemId(member.getMemId());
                    guaList.add(guarantor);
                    guaId++;
                }
                if (LoanTakenDBController.addNewLoanTaken(loanTaken, guaList, paymentList)) {
                    MessageBox.showMessageBox("Take A New Loan","Sucsessfully Added Your New Loan...!");
                } else {
                    MessageBox.showMessageBox("Take A New Loan","Unsucsessfulled To Do Your Task...!");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TakeLoanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        proceedBtn.setDisable(true);
    }

    @FXML
    public void cancelBtnEvent(ActionEvent event) {
        try {
            mainPain.getChildren().setAll(getContent("dashboard.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(TakeLoanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void selectLoanComboEvent(ActionEvent event) {
        arrangeModel(selectLoanCombo.getSelectionModel().getSelectedIndex());
    }
  
    @FXML
    public void guaranteeComboEvent(ActionEvent event) {
         String selectText = guaranteeCombo.getSelectionModel().getSelectedItem();
         if(selectText == null){
             addGuarantee();
             event.consume();
         }
         selectGuarantee = selectText;
    }
    
    @FXML
    public void checkBoxEvent(ActionEvent event) {
        if (loanModel.getGuaranteeType() == 2) {
            if (cheak1.isSelected() && cheak2.isSelected() && cheak3.isSelected() && cheak4.isSelected()) { 
                addFinalDetailPane.setDisable(false);
            }else{
                addFinalDetailPane.setDisable(true);
            }
        } else {
            if (cheak1.isSelected() && cheak2.isSelected() && cheak3.isSelected()) {   
                addFinalDetailPane.setDisable(false);
            }else{ 
                addFinalDetailPane.setDisable(true);
            }
        }
    }
    
    
    //------------------------- Addtional Methods ----------------------------//
    
    private void arrangeModel(int index){
        loanModel = modelList.get(index);
        
        modelName.setText(loanModel.getLoanName());
        label1.setText(loanModel.getRate()+"%");
        label2.setText("Rs."+loanModel.getMaxAmount());
        label3.setText((loanModel.isMethod())?"Redusing Balance Method":"Compound Interest Method");
        label4.setText((loanModel.getMinPeriod() == 0) ? "No define a min period" : loanModel.getMinPeriod()+" months");
        label5.setText((loanModel.getMaxPeriod() == 0) ? "No define a max period" : loanModel.getMaxPeriod()+" months");
        
        switch(loanModel.getRateType()){
            case 0 : label6.setText("Daily"); break;
            case 1 : label6.setText("Monthly"); break;
            case 2 : label6.setText("Annually");
        }
        
        switch(loanModel.getFineType()){
            case 0 : label7.setText("No Fine For This"); break;
            case 1 : label7.setText("Charge A Cost"); break;
            case 2 : label7.setText("Increse Interest Rate");
        }
        
        switch(loanModel.getGuaranteeType()){
            case 0 : label10.setText("None"); break;
            case 1 : label10.setText("Persons"); break;
            case 2 : label10.setText("Property"); break;
            case 3 : label10.setText("Fix Deposit");
        }
        
        //-------------------- Arrange Period Slider -------------------------//
        if(loanModel.getMinPeriod() == 0 || loanModel.getMaxPeriod() == 0){
            if(periodHBox.getChildren().contains(periodSlider)){
                periodHBox.getChildren().remove(periodSlider);
            }
        }else{
            if(!periodHBox.getChildren().contains(periodSlider)){
                periodHBox.getChildren().remove(periodText);
                periodHBox.getChildren().add(periodSlider);
                periodHBox.getChildren().add(periodText);
            }
            periodSlider.setMin(loanModel.getMinPeriod());
            periodSlider.setMax(loanModel.getMaxPeriod());
            periodSlider.setValue(periodSlider.getMin());
            setPeriodSliderAction();
        }
        
        //------- Make some arrangements -------//
        addFinalDetailPane.setDisable(true);
        proceedBtn.setDisable(true);
        amountText.setText("");
        periodText.setText(periodSlider.getMin()+"");
        cheak1.setSelected(false);
        cheak2.setSelected(false);
        cheak3.setSelected(false);
        cheak4.setSelected(false);
       
        if (!guarantorList.isEmpty()) {
        guarantorList.clear();
        guaranteeTable.getItems().clear();
        }
        
        if(loanModel.getGuaranteeType() == 0){
            fixDepositBox.setDisable(true);
            guaranteeBox.setDisable(true);
        }else{
            fixDepositBox.setDisable(false);
            guaranteeBox.setDisable(false);
        }
        
        if(loanModel.getGuaranteeType() == 2) {
            cheak4.setVisible(true);
        }else{
            cheak4.setVisible(false);
        }
        
        if(loanModel.getGuaranteeType() == 3){
            fixDepositBox.setVisible(true);
            guaranteeBox.setVisible(false);
        }else{
            fixDepositBox.setVisible(false);
            guaranteeBox.setVisible(true);
        }
    }
    
    public static void prepareLists(){
        try {
            modelList = LoanDBController.getAllLoanModel();
            allFixedDeposits = FixedDBController.getMemberCurrentFixedDeposits(debtorId);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LoansController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setDateTime() {
        dateTime dateTime = new dateTime();
        todayDateText.setText(dateTime.getNowDate());
        todayTimeText.setText(dateTime.getNowTime());
    }
    
    private Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/"+ a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }

    private void validationNodes() {
        amountText.setOnKeyTyped((KeyEvent event) -> {
            char c = event.getCharacter().charAt(0);
            if(!Character.isDigit(c)) {
                event.consume();
            }
        });
  
        amountText.setOnKeyReleased((KeyEvent event) -> {
            setProceedBtnEnable();
        });

        periodText.setOnKeyTyped((KeyEvent event) -> {
            char c = event.getCharacter().charAt(0);
            if(!Character.isDigit(c)) {
                event.consume();
            }else{
                if(Integer.parseInt(periodText.getText()+ event.getCharacter())> 120){    // CONSTANT //
                    event.consume();
                }
            }
        });
        
        periodText.setOnKeyReleased((KeyEvent event) -> {
            setProceedBtnEnable();
        });
    }
    
    private void searchableCombo() {
        guaranteeCombo.getEditor().setOnKeyTyped((KeyEvent event) -> {
            String text = guaranteeCombo.getEditor().getText() + event.getCharacter();
            try {
                result = MemberDBController.likeSearchMember(text);

                guaranteeCombo.hide();
                guaranteeCombo.getItems().setAll(result);
                if (!result.isEmpty()) {
                    guaranteeCombo.show();
                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TakeLoanController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullPointerException ex) {
                System.out.println("null pointer exception in search combo");
            }
        });
    }

    private void addGuarantee() {
        try {
            String[] breaks = selectGuarantee.split(" ");
            int selectGuaranteeId = Integer.parseInt(breaks[0]);
            if (debtorId == selectGuaranteeId) {
                System.out.println("Debtor And Guarantor is equal"); //meassage box//
            } else {
                Member member = MemberDBController.searchMemberFromId(selectGuaranteeId);
                
                if (isNotDuplicate(member)) {
                    guarantorList.add(member);
                    guaranteeTable.getItems().setAll(guarantorList);
                    setProceedBtnEnable();
                }
                
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TakeLoanController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isNotDuplicate(Member member) {
        if(guarantorList != null){
            for (Member preMember : guarantorList) {
                if(preMember.getMemId() == member.getMemId()){
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private void setGuaranteeTableMenu() {
        guaranteeTable.setRowFactory((TableView<Member> table) -> {
            TableRow<Member> row = new TableRow<>();
            ContextMenu menu = new ContextMenu();
            MenuItem item = new MenuItem("Delete");
            
            item.setOnAction((ActionEvent event) ->{
                guarantorList.remove(row.getIndex());
                guaranteeTable.getItems().setAll(guarantorList);
                setProceedBtnEnable();
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

    private void setFixedDepositTableMenu() {
        fixedDepositTable.setRowFactory((TableView<Fixed> param) -> {
            TableRow<Fixed> row = new TableRow<>();
            ContextMenu menu = new ContextMenu();
            MenuItem item = new MenuItem("Add Deposit");
            
            item.setOnAction((ActionEvent event)->{
                selectedFixedDeposit = row.getIndex();
                String text = allFixedDeposits.get(selectedFixedDeposit).getFixDate()+" - Rs. "+allFixedDeposits.get(selectedFixedDeposit).getAmount()+" Fixed Deposit";
                fixedDepositText.setText(text);
                setProceedBtnEnable();
            });
            
            menu.getItems().add(item);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(menu)
            );
            
            return row;
        });
        
    }
   
    private void setPeriodSliderAction() {
        periodText.setText(periodSlider.getMin()+"");
        periodSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (newValue == null) {
                    periodText.setText(periodSlider.getMin()+"");
                    return;
                }
                periodText.setText(new DecimalFormat("#").format(newValue));
                setProceedBtnEnable();
            }
        });
    }

    private void setLoanModelsCombo() {
        
        for (Loan loan : modelList) {
            selectLoanCombo.getItems().add(loan.getLoanName());
        }
    }

    private void setProceedBtnEnable() {
        boolean action1 = amountText.getText().equals("");
        boolean action2 = periodText.getText().equals("");
        boolean action3 = guarantorList.isEmpty();
        boolean action4 = fixedDepositText.getText().equals("");
        switch(loanModel.getGuaranteeType()){
            case 0 :
                if(action1 || action2){
                    proceedBtn.setDisable(true);
                }else{
                    proceedBtn.setDisable(false);
                }
                break;
            case 3 :
                if(action1 || action2 || action4){
                    proceedBtn.setDisable(true);
                }else{
                    proceedBtn.setDisable(false);
                }
                break;
            default:
                if(action1 || action2 || action3){
                    proceedBtn.setDisable(true);
                }else{
                    proceedBtn.setDisable(false);
                }
        }
    }
    
}
