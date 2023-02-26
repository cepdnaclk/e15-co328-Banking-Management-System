
package dbController;

import dbConnection.DBConnection;
import dbModel.Account;
import dbSubModel.GraphModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.DecimalFormat;

public class AccountDBController {
    
    private static DecimalFormat numFormat = new DecimalFormat("0.00"); 
    
    public static int getNextAccountId() throws ClassNotFoundException, SQLException {
        String query = "Select accId from account order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1;
    }
    
    public static boolean addNewAccount(Account account) throws ClassNotFoundException, SQLException {
       String query = "Insert into account values(?,?,?,?)";
       Connection conn = DBConnection.createConnection().getConnection();
       conn.setAutoCommit(false);
       try{
            PreparedStatement state = conn.prepareStatement(query);
            state.setObject(1,account.getAccId());
            state.setObject(2,account.getMemId());
            state.setObject(3,account.getNorBalance());
            state.setObject(4,null);
            if(state.executeUpdate()>0){
               conn.commit();
               return true;
            }
            conn.rollback();
            return false;
       }finally{
            conn.setAutoCommit(true);
       }

    }

    public static boolean updateNormalBalance(Account account) throws ClassNotFoundException, SQLException {
        String query = "Update account set norBalance = ? where accId = ?";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        state.setObject(1, account.getNorBalance());
        state.setObject(2, account.getAccId());
        
        return state.executeUpdate() > 0;
     
    }
    
    public static boolean updateCompulsoryBalance(Account account) throws ClassNotFoundException, SQLException {
        String query = "Update account set comBalance = ? where accId = ?";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        state.setObject(1, account.getComBalance());
        state.setObject(2, account.getAccId());
        
        return state.executeUpdate() > 0;     
    }

    public static Double getNormalBalance(int memberId) throws ClassNotFoundException, SQLException {
        String query = "Select norBalance from account where memId = "+memberId;
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return Double.parseDouble(numFormat.format(result.getDouble(1)));
        }
        return null;
    }

    public static Double getCompulsoryBalance(int memberId) throws ClassNotFoundException, SQLException {
        String query = "Select comBalance from account where memId = "+memberId;
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return Double.parseDouble(numFormat.format(result.getDouble(1)));
        }
        return null;
    }   

    public static ObservableList<GraphModel> getAccountSum() throws ClassNotFoundException, SQLException {
        String query = "select sum(norBalance), sum(comBalance) from account;";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<GraphModel> accSums = FXCollections.observableArrayList();      
        while (result.next()) {
            GraphModel accSum1 = new GraphModel();               
            accSum1.setCategory("Normal");
            accSum1.setNumber(result.getDouble(1));                      
            
            GraphModel accSum2 = new GraphModel();               
            accSum2.setCategory("Compulsory");
            accSum2.setNumber(result.getDouble(2));   
            
            accSums.addAll(accSum1,accSum2);
        }
        query = "select sum(amount) from fixed";
        result = state.executeQuery(query);
        if(result.next()){
            GraphModel accSum3 = new GraphModel();  
            accSum3.setCategory("Fixed");
            accSum3.setNumber(result.getDouble(1));   
            accSums.add(accSum3);
        }
        return accSums;
    }
}
