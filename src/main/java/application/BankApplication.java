package application;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BankApplication extends Application {
    public Stage stage;
    String url = "/view/login.fxml";
    
    @Override
    public void start(Stage stage) {
        try{
            this.stage=stage;            
            Parent root = FXMLLoader.load(getClass().getResource(url));           
            stage.initStyle(StageStyle.UNDECORATED);
           
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(BankApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }   
}
