
package dbController;

import dbConnection.DBConnection;
import dbModel.Fixed;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reportBeans.FixedScheduleDeposit;

import java.sql.*;

public class FixedDBController {
    
    public static int getNextFixedDepositId() throws ClassNotFoundException, SQLException {
        String query = "Select fixId from fixed order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1000;
    }
    
    public static boolean addNewFixedDeposit (Fixed fixed) throws ClassNotFoundException, SQLException {
       String query = "Insert into fixed values(?,?,?,?,?,?,?,?,?,?)";
       Connection conn = DBConnection.createConnection().getConnection();
       conn.setAutoCommit(false);
       try{
            PreparedStatement state = conn.prepareStatement(query);
            
            state.setObject(1,  fixed.getFixId());
            state.setObject(2,  fixed.getAccId());
            state.setObject(3,  fixed.getFixDate());
            state.setObject(4,  fixed.getFixTime());
            state.setObject(5,  fixed.getPeriod());
            state.setObject(6,  fixed.getAmount());
            state.setObject(7,  fixed.isIsMaturity());
            state.setObject(8,  fixed.isIsWithdraw());
            state.setObject(9,  fixed.isIsBond());
            state.setObject(10, fixed.getInterest());
           
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
    //------------------- This Control make specific fixed deposit record's isBond = true -----------------------//
    public static boolean makeFixedDepositBond (int fixId) throws ClassNotFoundException, SQLException {
        String query = "Update fixed set isBond = true where fixId = ?";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        state.setObject(1, fixId);
        
        return state.executeUpdate()>0;
    }
    
    //------------------- This Control gives specific member's fixed deposits they are not withdrawed-----------------------//
    
    public static ObservableList<Fixed>  getMemberCurrentFixedDeposits(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select * from fixed natural join account where accId = " + memberId + " and isWithdraw = false and isBond = false";        
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<Fixed> allFixedDeposits = FXCollections.observableArrayList();
        
        while (result.next()) {
            Fixed fixed = new Fixed();
           
            fixed.setAccId(result.getInt(1));
            fixed.setFixId(result.getInt(2));
            fixed.setFixDate(result.getString(3));
            fixed.setFixTime(result.getString(4));
            fixed.setPeriod(result.getInt(5));
            fixed.setAmount(result.getDouble(6));
            fixed.setIsMaturity(result.getBoolean(7));
            fixed.setIsWithdraw(result.getBoolean(8));
            fixed.setIsBond(result.getBoolean(9));
            fixed.setInterest(result.getDouble(10));
            
            allFixedDeposits.add(fixed);
        }
        return allFixedDeposits;
    }  

    public static int getActiveFixedDepositCount(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select count(fixId)\n" +
                       "from fixed f, account a\n" +
                       "where f.accId = a.accId and isWithdraw = false and memId = "+ memberId;
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1);
        }
        return -1;
    }   

    public static ObservableList<FixedScheduleDeposit> getFixSchedule(String clientType, String fixedType) throws ClassNotFoundException, SQLException {
        String query = "select a.memId, name, fixDate, amount, period\n" +
                       "from fixed f, account a, member m\n" +
                       "where f.accId = a.accId and a.memId = m.memId " + fixedType + clientType +"\n" +
                       "order by a.memId;";
        System.out.println(query);
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<FixedScheduleDeposit> fixSchedules = FXCollections.observableArrayList();        
        while (result.next()) {
            FixedScheduleDeposit fixSchedule = new FixedScheduleDeposit();
           
            fixSchedule.setId(result.getInt(1));
            fixSchedule.setName(result.getString(2));
            fixSchedule.setTakenDate(result.getString(3));
            fixSchedule.setAmount(result.getDouble(4));
            fixSchedule.setPeriod(result.getInt(5));
            
            fixSchedules.add(fixSchedule);
        }
        return fixSchedules;
    }
}
