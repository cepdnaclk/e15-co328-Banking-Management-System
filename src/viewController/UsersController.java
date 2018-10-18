/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewController;

import dbController.NormalDBController;
import dbController.UserDBController;
import dbModel.User;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class UsersController implements Initializable {
    @FXML
    private ComboBox<String> userTypeCombo;

    @FXML
    private Button proceedBtn;

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField usernameText;
    
    @FXML
    private TableColumn<User, String> usernameColumn, userTypeColumn;
    
    @FXML
    private TableView<User> userTable;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userTypeCombo.getItems().setAll("Admin","Clerk","Audit");
        userTypeCombo.getSelectionModel().select(0);
        
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        
        fillTable();
    }    
    
    @FXML
    public void proceedBtnEvent(ActionEvent event) {
        try {
            User user = new User();
            user.setUserName(usernameText.getText());
            user.setPassword(passwordText.getText());
            user.setUserType(userTypeCombo.getSelectionModel().getSelectedIndex());
            
            if(UserDBController.addUser(user)){
                System.out.println("sucess");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillTable() {
        try {
            userTable.getItems().setAll(UserDBController.getAllUsers());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
