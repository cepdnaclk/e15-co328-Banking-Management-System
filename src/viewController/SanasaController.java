
package viewController;

import animation.FadeUp;
import help.ResizeHelper;
import help.myHelp;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;

public class SanasaController {
    @FXML
    private AnchorPane paneBar;
    
    @FXML
    private Button fullscreenBtn;

    @FXML
    private Button logout;
    
    @FXML
    private Label temp;
    
    @FXML
    private AnchorPane daddy;

    @FXML
    private AnchorPane contentPane;
    
    @FXML
    private ListView<String> listView;
    
    @FXML
    private Button minimizeBtn;
    
    @FXML
    private Button closeBtn;
    
    private Stage stage;
    private Pane pane;
    public static int userType;
    private static Stage primaryStage;
    public static AnchorPane mainPane;
    public SanasaController() {}
    
    public void initialize() {  
        mainPane = contentPane;                
        Platform.runLater(()->{
            switch(userType){
                case 0:
                    listView.getItems().addAll("Dashboard","Clients","Loans","Deposits","Shares","Attendance","Graphs","Reports","Users","Settings");
                    break;
                case 1:
                    listView.getItems().addAll("Dashboard","Clients","Loans","Deposits","Shares","Attendance","Graphs","Reports","Settings");
                    break;
                case 2:
                    listView.getItems().addAll("Graphs","Reports");
                    break;                    
            }
            listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            try {
                primaryStage = (Stage) temp.getScene().getWindow();
                if(userType==2){
                    contentPane.getChildren().setAll(getContent("graphs.fxml"));
                }else{
                    contentPane.getChildren().setAll(getContent("dashboard.fxml"));
                }                
                ResizeHelper.addResizeListener(primaryStage);
            }catch (IOException ex) {
                Logger.getLogger(SanasaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @FXML
    public void listViewEvent(MouseEvent event) {
        try{
            switch(listView.getSelectionModel().getSelectedItem()){
                case "Dashboard" : 
                    contentPane.getChildren().setAll(getContent("dashboard.fxml"));  
                    break;
                case "Clients" : 
                    contentPane.getChildren().setAll(getContent("member.fxml"));     
                    break;
                case "Loans" : 
                    contentPane.getChildren().setAll(getContent("loans.fxml"));      
                    break;
                case "Deposits" : 
                    DepositsController.selectedMemId = -1; 
                    contentPane.getChildren().setAll(getContent("deposits.fxml")); 
                    break;
                case "Shares" : 
                    SharesController.selectedMemId = -1;
                    contentPane.getChildren().setAll(getContent("shares.fxml"));     
                    break;
                case "Attendance" : 
                    contentPane.getChildren().setAll(getContent("attendance.fxml")); 
                    break;
                case "Graphs" : 
                    contentPane.getChildren().setAll(getContent("graphs.fxml"));     
                    break;
                case "Reports":
                    contentPane.getChildren().setAll(getContent("reports.fxml"));
                    new FadeUp(contentPane, 20, 0);
                    break;
                case "Users":
                    contentPane.getChildren().setAll(getContent("users.fxml"));
                    new FadeUp(contentPane, 20, 0);
                    break;
                case "Settings":
                    contentPane.getChildren().setAll(getContent("settings.fxml"));               
            }
        } catch (IOException ex) {
            Logger.getLogger(SanasaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void logoutEvent(){
        myHelp help2 = new myHelp();
        help2.makeStage(stage, temp, "/view/login.fxml", "Sanasa Bank", false, StageStyle.UNDECORATED, false,0);
    }
    
    @FXML
    public void fullscreenBtnEvent(ActionEvent event) {
         if (primaryStage.isFullScreen()) {
            primaryStage.setFullScreen(false);
        }else{
            primaryStage.setFullScreen(true);
        }
    }
    
    @FXML
    public void minimizeBtnEvent(ActionEvent event) {
        primaryStage.setIconified(true);
    }
    
    @FXML
    public void closeBtnEvent(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }

    public Pane getContent(String a) throws IOException{
        pane = FXMLLoader.load(getClass().getResource("/view/"+ a));
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        return pane;
    }
    
    public static class Distance{
        static double deltaX,deltaY;
    }

    public void getDelta(MouseEvent event){
        Distance.deltaX = - event.getX()+(-200);
        Distance.deltaY = - event.getY();
    }
    
    public void setStage(MouseEvent event){
        primaryStage.setX(event.getScreenX()+ Distance.deltaX);
        primaryStage.setY(event.getScreenY()+ Distance.deltaY);
    }
}
