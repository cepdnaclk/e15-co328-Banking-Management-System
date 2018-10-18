
package dbController;

import dbConnection.DBConnection;
import dbModel.Normal;
import dbModel.Shares;
import dbSubModel.GraphModel;
import dbSubModel.MemberAccount;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SharesDBController {
    public static int getNextSharesId() throws ClassNotFoundException, SQLException {
        String query = "Select shaId from shares order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1000;
    }

    public static void addShare(Shares share) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static double getBalance(int memId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static ObservableList<Shares> getAllShareBalance() throws ClassNotFoundException, SQLException {
        String query = "select m.memId, name, sum(amount) \n" +
                       "from shares s, member m\n" +
                       "where s.memId = m.memId AND isMember = true \n" +
                       "group by m.memId";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<Shares> memberShares = FXCollections.observableArrayList();
        
        while (result.next()) {
           Shares shares = new Shares();
            
           shares.setMemId(result.getInt(1));
           shares.setName(result.getString(2));
           shares.setBalance(result.getDouble(3));
           
           memberShares.add(shares);
        }
        return memberShares;       
    }

    public static ObservableList<GraphModel> getAccountSum() throws ClassNotFoundException, SQLException {
        String query = "select year(shaDate), sum(amount) from shares\n" +
                       "group by year(shaDate)";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<GraphModel> modelSums = FXCollections.observableArrayList();
        
        while (result.next()) {
            GraphModel modelSum = new GraphModel();     
            modelSum.setCategory(result.getInt(1)+"");
            modelSum.setNumber(result.getDouble(2));                      
            modelSums.add(modelSum);
        }
        return modelSums;
    }

    public static ObservableList<Shares> getMemberShares(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select * from shares where memId= "+memberId;
   
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<Shares> shares = FXCollections.observableArrayList();
        
        while (result.next()) {
            Shares share = new Shares();
           
            share.setShaId(result.getInt(1));
            share.setMemId(result.getInt(2));
            share.setShaDate(result.getString(3));
            share.setShaTime(result.getString(4));
            share.setAmount(result.getDouble(5));
            share.setBalance(result.getDouble(6));
            
            shares.add(share);
        }
        return shares;
    }
        
}
