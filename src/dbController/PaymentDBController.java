
package dbController;

import dbConnection.DBConnection;
import dbModel.Payment;
import dbSubModel.NextPayment;
import dbSubModel.PreviousPayments;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PaymentDBController {
    public static int getNextPaymentId() throws ClassNotFoundException, SQLException {
        String query = "Select payId from payment order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1000;
    }

    public static boolean addNewPayments(ObservableList<Payment> paymentList) throws ClassNotFoundException, SQLException {
        String query = "Insert into payment values(?,?,?,?,?,?,?)";
        Connection conn = DBConnection.createConnection().getConnection();
        int count = 0;
        for (Payment payment : paymentList) {
            PreparedStatement state = conn.prepareStatement(query);
            state.setObject(1, payment.getPayId());
            state.setObject(2, payment.getTakenId());
            state.setObject(3, payment.getPayDate());
            state.setObject(4, payment.getPayTime());
            state.setObject(5, payment.getAmount());
            state.setObject(6, payment.getFine());
            state.setObject(7, payment.getInterest());         
            count += state.executeUpdate();
        }
        return count == paymentList.size();
    }
    
    public static ObservableList<NextPayment> getNextPayments(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select t.takenID, loanName, rate, method, rateType, fineType, period, t.amount, balance, \n" +
                       "max(datediff(curdate(), payDate)), \n" +
                       "min(datediff(curdate(), payDate))\n" +
                       "from payment p, loanTaken t \n" +
                       "where p.takenId = t.takenID and balance != -1 and memId = " + memberId + " \n" +
                       "group by t.takenID";      
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<NextPayment> nextPayments = FXCollections.observableArrayList();
        
        while (result.next()) {
            NextPayment nextPayment = new NextPayment();
            nextPayment.setTakenId(result.getInt(1));
            nextPayment.setLoanName(result.getString(2));
            nextPayment.setRate(result.getInt(3));
            nextPayment.setMethod(result.getBoolean(4));
            nextPayment.setRateType(result.getInt(5));
            nextPayment.setFineType(result.getInt(6));
            nextPayment.setPeriod(result.getInt(7)); 
            nextPayment.setAmount(result.getDouble(8));
            nextPayment.setBalance(result.getDouble(9));            
            nextPayment.setDates(result.getInt(10));
            nextPayment.setNumOfDates(result.getInt(11));
            nextPayments.add(nextPayment);
        }
        return nextPayments;
    }
    
    public static ObservableList<PreviousPayments> getPreviousPayments(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select payDate, loanName, interest, fine, p.amount-(interest+fine)\n" +
                       "from payment p, loanTaken t \n" +
                       "where p.takenId = t.takenID and balance != -1 and memId = " + memberId + " \n" +
                       "order by payDate desc"; 
        
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<PreviousPayments> prePayments = FXCollections.observableArrayList();
        
        while (result.next()) {
            PreviousPayments prePayment = new PreviousPayments();
           
            prePayment.setPayDate(result.getString(1));
            prePayment.setLoanName(result.getString(2));
            prePayment.setInterest(result.getDouble(3));
            prePayment.setFine(result.getDouble(4));
            prePayment.setInstallAmount(result.getDouble(5));
            
            prePayments.add(prePayment);
        }
        return prePayments;
    }
    
    public static PreviousPayments getLastPayment(int memberId) throws ClassNotFoundException, SQLException {
        String query = "select payDate, payTime, count(loanName), sum(interest), sum(fine), sum(p.amount-(interest+fine)), sum(p.amount)\n" +
                       "from payment p, loanTaken t \n" +
                       "where p.takenId = t.takenID and balance != -1 and memId = " + memberId + " \n" +
                       "group by payDate\n" +
                       "order by payDate desc limit 1;";
   
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        if (result.next()) {
            PreviousPayments lastPayment = new PreviousPayments();
            
            lastPayment.setPayDate(result.getString(1));
            lastPayment.setPayTime(result.getString(2));
            lastPayment.setNumOfLoans(result.getInt(3));
            lastPayment.setInterest(result.getDouble(4));
            lastPayment.setFine(result.getDouble(5));
            lastPayment.setInstallAmount(result.getDouble(6));
            lastPayment.setPaymentAmount(result.getDouble(7));
            
            return lastPayment;
        }
        return null;       
    }    
}
