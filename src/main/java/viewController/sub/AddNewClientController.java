
package viewController.sub;

import animation.FadeUp;
import dbController.AccountDBController;
import dbController.MemberDBController;
import dbController.SharesDBController;
import dbModel.Account;
import dbModel.Member;
import dbModel.Shares;
import help.MessageBox;
import help.dateTime;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import viewController.DashboardController;
import viewController.SanasaController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;



public class AddNewClientController implements Initializable {
    
    //--------------------------- FXML Attributes ----------------------------//
    @FXML
    private DatePicker dateText;

    @FXML
    private TextField parentNameText, shareAmountText;

    @FXML
    private TextField firstNameText, lastNameText, street1Text, street2Text, street3Text, street4Text, nicNumText, contactText;

    @FXML
    private ToggleButton yesToggle;

    @FXML
    private Button saveBtn, cancelBtn;

    @FXML
    private RadioButton maleRadio, femaleRadio;

    @FXML
    private ComboBox<String> parentCombo;
    
    @FXML
    private CheckBox shareCheak;
    
    @FXML
    private AnchorPane parent, additionalPane;

    //---------------------- Normal Attributes -------------------------------//
    private ToggleGroup group;
    private RadioButton radio;
    private Pane pane;
    private static int selectedParentId;
    private static AnchorPane mainPain;
    private static ObservableList<String> result;
    private static String selectClient;
    
    //---------------------- Initialize and Startup Actions ------------------//
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainPain = SanasaController.mainPane;        
        group = new ToggleGroup();
        maleRadio.setToggleGroup(group);
        femaleRadio.setToggleGroup(group);
        maleRadio.setSelected(true);
        
        //----------- Calling Additional Methods -----------------------------//
        clearFields();
        validationNodes();
        searchableCombo();
    }    
    
    //---------------------- Nodes Events ------------------------------------//
    
    @FXML
    public void saveBtnEvent(ActionEvent event) {
        try{
            //------- Geting Member Data To Variables---------//
            radio = (RadioButton)group.getSelectedToggle(); 
            int memId = MemberDBController.getNextMemberId();
            String fName = firstNameText.getText();
            String lName = lastNameText.getText();
            String street1 = street1Text.getText();
            String street2 = street2Text.getText();
            String street3 = street3Text.getText();
            String street4 = street4Text.getText();
            String dob = dateText.getValue().toString();
            String nic = nicNumText.getText();
            String contact = contactText.getText();
            boolean gender = radio.getText().equals("Male");
            boolean isApplicant = yesToggle.isSelected();
            int parentId = yesToggle.isSelected() ? selectedParentId : -1;
            
            //------- Creating Member Object---------//
            Member member = new Member();
            
            member.setMemId(memId);
            member.setName(fName+" "+lName);
            member.setAddress(street1+","+street2+","+street3+","+street4);
            member.setDob(dob);
            member.setNic(nic);
            member.setContact(contact);
            member.setGender(gender);
            member.setIsMember(false);
            member.setIsActive(true);
            member.setIsApplicant(isApplicant);
            member.setParentId(parentId);
            
            //--------getting account data to variables------//
            int accId = AccountDBController.getNextAccountId();
            
            //--------create account object-----------------//
            Account account = new Account();
            
            account.setAccId(accId);
            account.setMemId(memId);
            account.setNorBalance(0.0);
            account.setComBalance(0.0);
           
            if (MemberDBController.addNewClient(member) && AccountDBController.addNewAccount(account)) {
                MemberDBController.initializeImage(memId);
                if(shareCheak.isSelected()){
                    Shares share = new Shares();
                    dateTime myDate = new dateTime();
                    share.setShaId(SharesDBController.getNextSharesId());
                    share.setMemId(memId);
                    share.setShaDate(myDate.getNowDate());
                    share.setShaTime(myDate.getNowTime());
                    share.setAmount(Double.parseDouble(shareAmountText.getText()));
                    share.setBalance(Double.parseDouble(shareAmountText.getText())+SharesDBController.getBalance(memId));
                    
                    SharesDBController.addShare(share);
                }
                clearFields();
                MessageBox.showMessageBox("Add New Client","Sucsessfully Added Your New Client...!");
            } else {
                MessageBox.showMessageBox("Add New Client","Unsucsessfulled to do your task...!");
            }
            
        }catch(NullPointerException e){
            System.out.println("Please Fill All Data..");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddNewClientController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddNewClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void yesToggleEvent(ActionEvent event) {
        if(yesToggle.isSelected()){
            additionalPane.setVisible(true);
            parentNameText.setText("");
            shareAmountText.setText("");
            setSaveBtnEnable();
        }else{
            additionalPane.setVisible(false);
            setSaveBtnEnable();
        }
    }
    
    @FXML
    public void dateTextEvent(ActionEvent event) {
        setSaveBtnEnable();
    }

    @FXML
    public void shareCheakEvent(ActionEvent event) {
        if(shareCheak.isSelected()){
            shareAmountText.setVisible(true);
            shareAmountText.setText("");
            setSaveBtnEnable();
        }else{
            shareAmountText.setVisible(false);
            shareAmountText.setText("");
            setSaveBtnEnable();
        }
        System.out.println("d");
    }
    
    @FXML
    public void cancelBtnEvent(ActionEvent event) {        
         try {
            mainPain.getChildren().setAll(getContent("dashboard.fxml"));
            new FadeUp(mainPain, 20, 0);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    //------------------------- Addtional Methods ----------------------------//
    
    public Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/" + a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }
    
    private void clearFields() {
        firstNameText.setText("");
        lastNameText.setText("");
        street1Text.setText("");
        street2Text.setText("");
        street3Text.setText("");
        street4Text.setText("");
        dateText.getEditor().setText("");
        nicNumText.setText("");
        contactText.setText("");
        parentNameText.setText("");
        shareAmountText.setText("");
        dateText.setValue(null);
        yesToggle.setSelected(false);
        additionalPane.setVisible(false);
        shareAmountText.setVisible(false);
        shareCheak.setSelected(false);
        saveBtn.setDisable(true);
    }

    private void searchableCombo() {
        parentCombo.getEditor().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.DOWN || event.getCode()== KeyCode.UP) {
                    event.consume();
                }else{
                    String text = parentCombo.getEditor().getText();
                    try {
                        result = MemberDBController.likeSearchMember(text);
                        
                        parentCombo.hide();                       
                        if (result.isEmpty()) {
                            parentCombo.hide();
                        } else {
                            parentCombo.getItems().setAll(result);
                            parentCombo.show();
                        }

                    } catch (ClassNotFoundException | SQLException ex) {
                    }
                    event.consume();               
                }
            }
        });
        
        parentCombo.setOnKeyReleased((KeyEvent event) -> {
            try {
                if (event.getCode() == KeyCode.ENTER) {
                    selectClient = parentCombo.getSelectionModel().getSelectedItem();
                    String[] breaks = selectClient.split(" ");
                    selectedParentId = Integer.parseInt(breaks[0]);
                    parentNameText.setText(selectClient);
                    setSaveBtnEnable();
                    parentCombo.getSelectionModel().clearSelection();
                    parentCombo.getEditor().setText("");
                    event.consume();
                }
            } catch (NumberFormatException ex) {
                parentCombo.getEditor().setText("");
            }catch (NullPointerException ex){
                parentCombo.getEditor().setText("");
            }
        });       
    }    
    
    private void validationNodes() {
        firstNameText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[A-Za-z]")) {
                event.consume();
            }
        });
  
        firstNameText.setOnKeyReleased((KeyEvent event) -> {
             setSaveBtnEnable(); 
        });
        //----------------------------------------------------//
        lastNameText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[A-Za-z]")) {
                event.consume();
            }
        });
  
        lastNameText.setOnKeyReleased((KeyEvent event) -> {
             setSaveBtnEnable(); 
        });
        //---------------------------------------------------//
        street1Text.setOnKeyReleased((KeyEvent event) -> {
             setSaveBtnEnable(); 
        });
        street2Text.setOnKeyReleased((KeyEvent event) -> {
             setSaveBtnEnable(); 
        });
        //---------------------------------------------------//
        nicNumText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[0-9V|X]")) {
                event.consume();
            }
        });
  
        nicNumText.setOnKeyReleased((KeyEvent event) -> {
             setSaveBtnEnable(); 
        });
        //---------------------------------------------------//
        contactText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[0-9-]")) {
                event.consume();
            }
        });
  
        contactText.setOnKeyReleased((KeyEvent event) -> {
             setSaveBtnEnable(); 
        });
        //---------------------------------------------------//
        shareAmountText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[0-9]")) {
                event.consume();
            }
        });
        
        shareAmountText.setOnKeyReleased((KeyEvent event) -> {
             setSaveBtnEnable(); 
        });
    }
    
    private void setSaveBtnEnable(){
        boolean action1 = firstNameText.getText().trim().equals("") || lastNameText.getText().equals("");
        boolean action2 = street1Text.getText().trim().equals("") || street2Text.getText().trim().equals("");
        boolean action3 = dateText.getEditor().getText().equals("");
        boolean action4 = nicNumText.getText().equals("");
        boolean action5 = contactText.getText().equals("");
        boolean action6 = parentNameText.getText().equals("");
        boolean action7 = shareAmountText.getText().equals("");
        boolean end = false;
        if(yesToggle.isSelected()){
            if(shareCheak.isSelected()){
                end = action1 || action2 || action3 || action4 || action5 || action6 || action7;
            }else{
                end = action1 || action2 || action3 || action4 || action5 || action6;
            }
        }else{
            end = action1 || action2 || action3 || action4 || action5;
        }
        
        if(end){
            saveBtn.setDisable(true);
        }else{
            saveBtn.setDisable(false);
        }
    }
    
}
