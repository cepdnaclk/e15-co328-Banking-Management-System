
package dbController;


import dbConnection.DBConnection;
import dbModel.Account;
import dbModel.Compulsory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reportBeans.ScheduleDeposit;

import java.sql.*;

public class CompulsoryDBController {
    
    public static int getNextComId() throws ClassNotFoundException, SQLException {
        String query = "Select comId from compulsory order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1000;
    }
    
    public static ObservableList<Compulsory> getCompulsoryDeposits(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select comDate, comTime, comType, amount, c.balance \n" +
                       "from compulsory c, account a \n" +
                       "where c.accId = a.accId and memId = " + memberId + " \n" +
                       "order by comDate desc, comTime desc;";
   
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<Compulsory> comDeposits = FXCollections.observableArrayList();
        
        while (result.next()) {
            Compulsory compulsory = new Compulsory();
           
            compulsory.setComDate(result.getString(1));
            compulsory.setComTime(result.getString(2));
            compulsory.setComType(result.getInt(3));
            compulsory.setAmount(result.getDouble(4));
            compulsory.setBalance(result.getDouble(5));
            
            comDeposits.add(compulsory);
        }
        return comDeposits;
    }

    public static int getLastInterestDayCount(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select min(datediff(curdate(), comDate))\n" +
                       "from compulsory c, account a \n" +
                       "where c.accId = a.accId and comType = 1 and memId = "+memberId;
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        if (result.next()) {
            if (result.getObject(1) != null) {
                return result.getInt(1);
            }
        }
        //--- if this member never calculate interest then run this commad for count days ---//
        query = "select max(datediff(curdate(), comDate))\n" +
                "from compulsory c, account a \n" +
                "where c.accId = a.accId and memId = "+memberId;
        result = state.executeQuery(query);
        if (result.next()) {
            if (result.getObject(1) != null) {
                return result.getInt(1);
            }
        }
        return -1;
    } 
    
    public static Compulsory getNextInterestDetail(int memberId) throws ClassNotFoundException, SQLException{
         String query = "select a.accId, min(datediff(curdate(), comDate)), sum(interest), comBalance\n" +
                        "from compulsory c, account a \n" +
                        "where c.accId = a.accId and memId = " + memberId + " \n" +
                        "order by comDate asc";

         Connection conn = DBConnection.createConnection().getConnection();
         Statement state = conn.createStatement();
         ResultSet result = state.executeQuery(query);

         if (result.next()) {
             if (result.getObject(1) != null) {
                 Compulsory compulsory = new Compulsory();

                 compulsory.setAccId(result.getInt(1));
                 compulsory.setNumOfDates(result.getInt(2));
                 compulsory.setInterest(result.getDouble(3));
                 compulsory.setBalance(result.getDouble(4));

                 return compulsory;
             }
         }
         return null;
     }

    public static boolean addNewCompulsoryDeposit(Compulsory compulsory, Account account) throws ClassNotFoundException, SQLException {
        String query = "Insert into compulsory values(?,?,?,?,?,?,?,?)";
        Connection conn = DBConnection.createConnection().getConnection();
        conn.setAutoCommit(false);
        try {
            PreparedStatement state = conn.prepareStatement(query);
            
            state.setObject(1, compulsory.getComId());
            state.setObject(2, compulsory.getAccId());
            state.setObject(3, compulsory.getComDate());
            state.setObject(4, compulsory.getComTime());
            state.setObject(5, compulsory.getComType());
            state.setObject(6, compulsory.getAmount());
            state.setObject(7, compulsory.getBalance());
            state.setObject(8, compulsory.getInterest());
            if (state.executeUpdate() > 0) {
                if (AccountDBController.updateCompulsoryBalance(account)) {
                    conn.commit();
                    return true;
                }
                conn.rollback();
                return false;
            }
            conn.rollback();
            return false;
            
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public static ObservableList<ScheduleDeposit> getComSchedule(String date) throws ClassNotFoundException, SQLException {
        String query = "select a.accId, name, balance\n"
                     + "from compulsory c, account a, member m\n"
                     + "where c.accId = a.accId and a.memId = m.memId  and comId in ( \n"
                     + "    select max(comId)\n"
                     + "    from compulsory\n"
                     + "    where comDate <= '" +date+ "'\n"
                     + "    group by accId\n"
                     + ")order by accId;";
        System.out.println(query);
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);

        ObservableList<ScheduleDeposit> comSchedules = FXCollections.observableArrayList();
        while (result.next()) {
            ScheduleDeposit comSchedule = new ScheduleDeposit();

            comSchedule.setMemberId(result.getInt(1));
            comSchedule.setName(result.getString(2));
            comSchedule.setBalance(result.getDouble(3));

            comSchedules.add(comSchedule);
        }
        return comSchedules;
    }
}


