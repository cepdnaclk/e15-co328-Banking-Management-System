/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbController;

import dbConnection.DBConnection;
import dbModel.Normal;
import dbModel.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author RISITH-PC
 */
public class UserDBController {

    public static User getUser(String username, String password) throws ClassNotFoundException, SQLException {
        String query = "select * from users where userName= ? and userPassword= ?";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        state.setObject(1, username);
        state.setObject(2, password);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            User user = new User();
            user.setUserName(result.getString(1));
            user.setPassword(result.getString(2));
            user.setUserType(result.getInt(3));
            return user;
        }
        return null;
    }
    
    public static boolean addUser(User user) throws ClassNotFoundException, SQLException {
        String query = "Insert into users values(?,?,?)";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);

        state.setObject(1, user.getUserName());
        state.setObject(2, user.getPassword());
        state.setObject(3, user.getUserType());
                
        return state.executeUpdate()>0;
    }

    public static ObservableList<User> getAllUsers() throws ClassNotFoundException, SQLException {
        String query = "select * from users";
   
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<User> users = FXCollections.observableArrayList();
        
        while (result.next()) {
            User user = new User();
           
            user.setUserName(result.getString(1));
            user.setPassword(result.getString(2));
            user.setUserType(result.getInt(3));
            
            users.add(user);
        }
        return users;
    }
}
