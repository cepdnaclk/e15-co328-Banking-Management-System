package viewController;

import animation.FadeInLeft;
import animation.FadeInRight;
import dbController.UserDBController;
import dbModel.User;
import help.myHelp;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;



public class LoginController implements Initializable {
     @FXML
    private HBox userNameBox, passwordBox;

    @FXML
    private Text weedagamaLabel, sanasaLabel,welcomeLabel, userLabel;

    @FXML
    private AnchorPane loginPanel;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Label closeLabel;

    @FXML
    private TextField usernameText;

    @FXML
    private ImageView loadImage;
    
    Stage stage;
    public Main main;
    private boolean isStart=true;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Platform.runLater(() -> {
            new FadeInLeft(weedagamaLabel,20);
            new FadeInRight(sanasaLabel,20);
            closeLabel.setOnMouseClicked((MouseEvent event) -> {
                Platform.exit();
                System.exit(0);
            });
            
            loginPanel.setOnMouseClicked((MouseEvent event) -> {
                weedagamaLabel.setVisible(false);
                sanasaLabel.setVisible(false);
                loadImage.setVisible(false);
                if (isStart) {
                    new FadeInLeft(welcomeLabel, 20);
                    new FadeInRight(userLabel, 20);
                    new FadeInLeft(userNameBox, 20, 6);
                    new FadeInLeft(passwordBox, 20, 6);
                    new FadeInRight(loginBtn, 20, 6);
                    isStart = false;
                }else{
                    new FadeInLeft(userNameBox, 20, 6);
                    new FadeInLeft(passwordBox, 20, 6);
                }   
            });            
            loginBtnEnable();
            validationNodes();
        });      
    }    

    @FXML
    public void loginBtnEvent(ActionEvent event) {   
        try {
            User user = UserDBController.getUser(usernameText.getText(), passwordText.getText());
            if (user == null) {
                usernameText.setText("");
                passwordText.setText("");
                new FadeInLeft(userNameBox, 20, 6);
                new FadeInLeft(passwordBox, 20, 6);
                loginBtnEnable();
            } else {
                System.out.println(user.getUserName() + " - " + user.getUserType());
                new myHelp().makeStage(stage, closeLabel, "/view/sanasa.fxml", "Sanasa Bank", true, StageStyle.UNDECORATED, false, user.getUserType());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void validationNodes() {
        usernameText.setOnKeyTyped((KeyEvent event) -> {
            String str = event.getCharacter();
            if(!str.matches("[A-Za-z]")) {
                event.consume();
            }
        });
  
        usernameText.setOnKeyReleased((KeyEvent event) -> {
             loginBtnEnable(); 
        });
        
        passwordText.setOnKeyReleased((KeyEvent event) -> {
             loginBtnEnable(); 
        });
    }
    
    private void loginBtnEnable() {
        boolean action1 = usernameText.getText().trim().equals("");
        boolean action2 = passwordText.getText().trim().equals("");
        
        if(action1||action2){
            loginBtn.setDisable(true);
        }else{
            loginBtn.setDisable(false);
        }
    }   
}
