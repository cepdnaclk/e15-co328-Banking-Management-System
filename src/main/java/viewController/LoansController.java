
package viewController;

import animation.FadeUp;
import dbController.LoanDBController;
import dbModel.Loan;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoansController implements Initializable {
    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;
    
    @FXML
    private Label label4;

    @FXML
    private Label label5;

    @FXML
    private Label label6;

    @FXML
    private Label label7;

    @FXML
    private Label label8;

    @FXML
    private Label label9;
    
    @FXML
    private Label label10;

    @FXML
    private Label modelName;
    
    @FXML
    private Button leftBtn;

    @FXML
    private Button rightBtn;
    
    @FXML
    private AnchorPane contentPane;
    
    @FXML
    private Button viewBtn;
  
    @FXML
    private Button deleteModelBtn;

    @FXML
    private Button updateModelBtn;
    
    Pane pane;
    
    private static boolean viewBtnStatus;
    private static int modelCount;
    private static int position = 0;
    private static ObservableList<Loan> modelList;
    private static Loan loanModel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewBtnStatus = true;
        try {
            contentPane.getChildren().setAll(getContent("addNewLoanModel.fxml"));
            new FadeUp(contentPane, 20, 0);
            
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        viewBtn.setText("Table View");
        prepareModels();
        if(modelCount!= -1) viewModels();
    }    
    
    @FXML
    public void viewBtnEvent(ActionEvent event) {
         try{
            if(viewBtnStatus){
                contentPane.getChildren().setAll(getContent("viewAllLoansTable.fxml"));
                new FadeUp(contentPane, 20, 0);
                viewBtn.setText("Add Model");
                viewBtnStatus = false;
            }else{
                contentPane.getChildren().setAll(getContent("addNewLoanModel.fxml"));
                new FadeUp(contentPane, 20, 0);
                viewBtn.setText("Table View");
                viewBtnStatus = true;
            }
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void navigateEvent(ActionEvent event) {
        Button btn = (Button) event.getSource();
        if(modelCount != -1){
            if(btn.getId().equals("rightBtn")){
                position = (position == modelCount) ? 0 : ++position ;
                viewModels();
            }else{
                position = (position == 0) ? modelCount : --position ;
                viewModels();
            }
        }else{
            System.out.println("No Content Available....!");
        }
    }

    @FXML
    public void deleteModelBtnEvent(ActionEvent event) {
        if(modelCount!=-1){
            try {
                loanModel = modelList.get(position);
                int loanId = loanModel.getLoanId();
                if(LoanDBController.deleteLoanModel(loanId)){
                    System.out.println("Safely Deleted");
                    prepareModels();
                    position = (position == 0) ? 0 : --position ;
                    if(modelCount!= -1) viewModels();
                    if(!viewBtnStatus){
                        contentPane.getChildren().setAll(getContent("viewAllLoansTable.fxml"));
                    }
                }else{
                    System.out.println("Delete Unsucessfull");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LoansController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                System.out.println("Can't Delete this Loan Model");

            } catch (IOException ex) {
                Logger.getLogger(LoansController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("No content To delete");
        }
    }

    @FXML
    public void updateModelBtnEvent(ActionEvent event) {
        
    }

    
    private Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/subView/" + a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }
    
    private void viewModels(){
        loanModel = modelList.get(position);
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
        System.out.println(loanModel.getGuaranteeType());
    }
    
    public static void prepareModels(){
        try {
            modelCount = LoanDBController.countLoanModels() - 1;
            modelList = LoanDBController.getAllLoanModel();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoansController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoansController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
