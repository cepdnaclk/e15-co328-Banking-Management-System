
package dbController;


import dbConnection.DBConnection;
import dbModel.Account;
import dbModel.Normal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reportBeans.ScheduleDeposit;

import java.sql.*;

/**
 *
 * @author RISITH-PC
 */
public class NormalDBController {
    
     public static int getNextNormalId() throws ClassNotFoundException, SQLException {
        String query = "Select norId from normal order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1000;
    }

     public static ObservableList<Normal> getNormalDeposits(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select norDate, norTime, norType, amount, n.balance \n" +
                       "from normal n, account a \n" +
                       "where n.accId = a.accId and memId = " + memberId + " \n" +
                       "order by norDate desc, norTime desc";
   
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<Normal> norDeposits = FXCollections.observableArrayList();
        
        while (result.next()) {
            Normal normal = new Normal();
           
            normal.setNorDate(result.getString(1));
            normal.setNorTime(result.getString(2));
            normal.setNorType(result.getInt(3));
            normal.setAmount(result.getDouble(4));
            normal.setBalance(result.getDouble(5));
            
            norDeposits.add(normal);
        }
        return norDeposits;
    }
     
     public static int getLastInterestDayCount(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select min(datediff(curdate(), norDate)) \n" +
                       "from normal n, account a \n" +
                       "where n.accId = a.accId and norType = 1 and  memId = "+memberId;
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        if (result.next()) {
            if (result.getObject(1) != null) {
                return result.getInt(1);
            }
        }
        //--- if this member never calculate interest then run this commad for count days ---//
        query = "select max(datediff(curdate(), norDate))\n" +
                "from normal n, account a \n" +
                "where n.accId = a.accId and  memId = "+memberId;
        result = state.executeQuery(query);
        if (result.next()) {
            if (result.getObject(1) != null) {
                return result.getInt(1);
            }
        }
        return -1;
    } 
    
     public static Normal getNextInterestDetail(int memberId) throws ClassNotFoundException, SQLException{
         String query = "select a.accId, min(datediff(curdate(), norDate)), sum(interest), norBalance\n" +
                        "from normal n, account a \n" +
                        "where n.accId = a.accId and memId = " + memberId + " \n" +
                        "order by norDate asc";

         Connection conn = DBConnection.createConnection().getConnection();
         Statement state = conn.createStatement();
         ResultSet result = state.executeQuery(query);

         if (result.next()) {
             if (result.getObject(1) != null) {
                 Normal normal = new Normal();

                 normal.setAccId(result.getInt(1));
                 normal.setNumOfDates(result.getInt(2));
                 normal.setInterest(result.getDouble(3));
                 normal.setBalance(result.getDouble(4));

                 return normal;
             }
         }
         return null;
     }
     
     public static boolean addNewNormalDeposit (Normal normal, Account account) throws ClassNotFoundException, SQLException {
       String query = "Insert into normal values(?,?,?,?,?,?,?,?)";
       Connection conn = DBConnection.createConnection().getConnection();
       conn.setAutoCommit(false);
       try{
            PreparedStatement state = conn.prepareStatement(query);
            
            state.setObject(1,  normal.getNorId());
            state.setObject(2,  normal.getAccId());
            state.setObject(3,  normal.getNorDate());
            state.setObject(4,  normal.getNorTime());
            state.setObject(5,  normal.getNorType());
            state.setObject(6,  normal.getAmount());
            state.setObject(7,  normal.getBalance());
            state.setObject(8,  normal.getInterest());          
            if (state.executeUpdate() > 0) {
               if (AccountDBController.updateNormalBalance(account)) {
                   conn.commit();
                   return true;
               }
               conn.rollback();
               return false;
           }
           conn.rollback();
           return false;

       }finally{
            conn.setAutoCommit(true);       
       }
    }   

     public static ObservableList<ScheduleDeposit> getNormalSchedule(String date, String clientType) throws ClassNotFoundException, SQLException {
        String query = "select a.accId, name, balance\n" +
                       "from normal n, account a, member m\n" +
                       "where n.accId = a.accId and a.memId = m.memId "+clientType+" and norId in (\n" +
                       "    select max(norId)\n" +
                       "    from normal\n" +
                       "    where norDate <= '" + date + "'\n" +
                       "    group by accId\n" +
                       ")order by accId";
         System.out.println(query);
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<ScheduleDeposit> norSchedules = FXCollections.observableArrayList();        
        while (result.next()) {
            ScheduleDeposit norSchedule = new ScheduleDeposit();
           
            norSchedule.setMemberId(result.getInt(1));
            norSchedule.setName(result.getString(2));
            norSchedule.setBalance(result.getDouble(3));
            
            norSchedules.add(norSchedule);
        }
        return norSchedules;
    }
}
