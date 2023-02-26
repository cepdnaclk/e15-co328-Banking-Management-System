
package viewController.sub;

import dbController.LoanDBController;
import dbModel.Loan;
import help.MessageBox;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import viewController.LoansController;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddNewLoanModelController implements Initializable {
    
    @FXML
    private RadioButton methodRadio1, methodRadio2;

    @FXML
    private Button saveBtn, cancelBtn;

    @FXML
    private ToggleButton yesToggleBtn;

    @FXML
    private ComboBox<String> rateTypeCombo, fineTypeCombo, guaranteeTypeCombo;

    @FXML
    private TextField loanNameText, maxAmountText, rateText, minPeriodText, maxPeriodText;

    @FXML
    private Slider rateSlider, maxPeriodSlider, minPeriodSlider;

    @FXML
    private CheckBox maxAmountCheak,minPeriodCheak,maxPeriodCheak;
    
    ToggleGroup group;
    RadioButton radio;
    
    DecimalFormat df = new DecimalFormat("#.##");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        group = new ToggleGroup();
        methodRadio1.setToggleGroup(group);
        methodRadio2.setToggleGroup(group);
        methodRadio1.setSelected(true);
        rateTypeCombo.getItems().addAll("Daily","Monthly","Annually");
        fineTypeCombo.getItems().addAll("None","Cost Type","Increse Rate");
        guaranteeTypeCombo.getItems().addAll("None","Personal","Property","Fix Deposit");
        
        maxPeriodText.textProperty().bind(
            Bindings.format(
                "%.0f"+"months",
                maxPeriodSlider.valueProperty()
            )
        );
      
        minPeriodText.textProperty().bind(
            Bindings.format(
                "%.0f"+"months",
                minPeriodSlider.valueProperty()
            )
        );
        
        rateText.textProperty().bind(
            Bindings.format(
                "%.2f",
                rateSlider.valueProperty()
            )
        );
        
    }
    
    @FXML
    public void saveBtnEvent(ActionEvent event) {
        try {
            //------- Geting Member Data To Variables---------//
            radio = (RadioButton)group.getSelectedToggle(); 
            int loanId = LoanDBController.getNextLoanModelId();
            String loanName = loanNameText.getText();
            double rate = Double.parseDouble(rateText.getText());
            boolean forMember = yesToggleBtn.isSelected();;
            boolean method = radio.getText().equals("Redusing balance method"); 
            int rateType = lookRateType();
            int fineType = lookFineType();
            int guaranteeType = lookGuaranteeType();
            Double maxAmount = (maxAmountCheak.isSelected()) ? Double.parseDouble(maxAmountText.getText()) : null;
            Integer minPeriod = (minPeriodCheak.isSelected()) ? (int) minPeriodSlider.getValue(): null;
            Integer maxPeriod = (maxPeriodCheak.isSelected()) ? (int) maxPeriodSlider.getValue() : null;
            
            
            //------- Creating Loan Object---------//
            Loan loan = new Loan();
            loan.setLoanId(loanId);
            loan.setLoanName(loanName);
            loan.setRate(rate);
            loan.setMaxAmount(maxAmount);
            loan.setForMember(forMember);
            loan.setMethod(method);
            loan.setRateType(rateType);
            loan.setFineType(fineType);
            loan.setGuaranteeType(guaranteeType);
            loan.setMinPeriod(minPeriod);
            loan.setMaxPeriod(maxPeriod);
            
            if (LoanDBController.addNewLoanModel(loan)) {
                clearFields();
                MessageBox.showMessageBox("Add New Loan Model","Sucsessfully Added Your New Loan Model...!");
            } else {
                MessageBox.showMessageBox("Add New Loan Model","Unsucsessfulled To Do Your Task...!");
            }
            LoansController.prepareModels();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AddNewLoanModelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AddNewLoanModelController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex){
            MessageBox.showMessageBox("Add New Loan Model","Please Fill All Data");
        } catch (NumberFormatException ex){
            System.err.println("Please insert correct data");
        }
       
    }

    @FXML
    public void cancelBtnEvent(ActionEvent event) {
        clearFields();
    }

    @FXML
    public void cheakEvent(ActionEvent event) {
        CheckBox cheak = (CheckBox) event.getSource();
        if(cheak.isSelected()){
            switch(cheak.getId()){
                case "maxAmountCheak": 
                    maxAmountText.setDisable(false); 
                    break;
                default :
                    maxPeriodCheak.setSelected(true);
                    minPeriodCheak.setSelected(true);
                    minPeriodSlider.setDisable(false); 
                    maxPeriodSlider.setDisable(false); 
            }
        }else{
            switch(cheak.getId()){
                case "maxAmountCheak": 
                    maxAmountText.setDisable(true); 
                    break;
                default :
                    maxPeriodCheak.setSelected(false);
                    minPeriodCheak.setSelected(false);
                    minPeriodSlider.setDisable(true); 
                    maxPeriodSlider.setDisable(true); 
            }
        }
    }
    
    private int lookRateType() {
        int i=0;
        switch(rateTypeCombo.getValue()){
            case "Monthly" : i=1; break;
            case "Annually" : i=2; 
        }
        return i;
    }

    private int lookGuaranteeType() {
        int i=0;
        switch(guaranteeTypeCombo.getValue()){
            case "Personal" : i=1; break;
            case "Property" : i=2; break;
            case "Fix Deposit" : i=3; 
        }
        return i;
    }
    
    private int lookFineType() {
        int i=0;
        switch(fineTypeCombo.getValue()){
            case "Cost Type" : i=1; break;
            case "Increse Rate" : i=2; 
        }
        return i;
    }

    private void clearFields(){
        loanNameText.setText("");
        maxAmountText.setText("");
       
        
        maxAmountCheak.setSelected(false);
        minPeriodCheak.setSelected(false);
        maxPeriodCheak.setSelected(false);
        
        minPeriodSlider.setValue(minPeriodSlider.getMin());
        maxPeriodSlider.setValue(maxPeriodSlider.getMin());
        
        maxAmountText.setDisable(true);
        minPeriodSlider.setDisable(true);
        maxPeriodSlider.setDisable(true);
        yesToggleBtn.setSelected(false);
        
        fineTypeCombo.getSelectionModel().select(null);
        rateTypeCombo.getSelectionModel().select(null);
    }
}
