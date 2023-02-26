package help;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author RISITH
 */
public class MessageBox implements Initializable {
    
    //--------------------------- FXML Attributes ----------------------------//
    @FXML
    private Label titleLabel, messageLabel;

    //---------------------- Normal Attributes -------------------------------//
    private static Parent root;
    private static Stage primaryStage;
    private static String title,message;
    
//---------------------- Initialize and Startup Actions ----------------------//
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        titleLabel.setText(title);
        messageLabel.setText(message);
    }    
  
    //---------------------- Nodes Events ------------------------------------//    
    @FXML
    public void setStage(MouseEvent event) {
        primaryStage.setX(event.getScreenX()+ Distance.deltaX);
        primaryStage.setY(event.getScreenY()+ Distance.deltaY);
    }

    @FXML
    public void getDelta(MouseEvent event) {
        Distance.deltaX = - event.getX();
        Distance.deltaY = - event.getY();
    }

    @FXML
    public void closeBtnEvent(ActionEvent event) {
        primaryStage.close();
        System.gc();
    }

    @FXML
    public void okMessageBtnEvent(ActionEvent event) {
        primaryStage.close();
        System.gc();
    }

    
    //------------------------- Addtional Methods ----------------------------//
    public static void showMessageBox(String t, String m){   
        title = t; message = m;
        new MessageBox().setRoot();       
        Scene scene = new Scene(root);
        primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
 
    private void setRoot(){
        try {
            root = FXMLLoader.load(getClass().getResource("/view/messageBox.fxml"));            
        } catch (IOException ex) {
            Logger.getLogger(MessageBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static class Distance{
        static double deltaX,deltaY;
    }
}
