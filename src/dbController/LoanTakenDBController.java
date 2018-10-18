
package dbController;

import dbConnection.DBConnection;
import dbModel.Fixed;
import dbModel.Guarantor;
import dbModel.LoanTaken;
import dbModel.Payment;
import dbSubModel.GraphModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reportBeans.LoanSchedule;

public class LoanTakenDBController {
    
    public static int getNextLoanTakenId() throws ClassNotFoundException, SQLException {
        String query = "Select takenId from loanTaken order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1000;
    }
    
    public static boolean addNewLoanTaken (LoanTaken loanTaken, ObservableList<Guarantor> guaList, ObservableList<Payment> paymentList) throws ClassNotFoundException, SQLException {
       String query = "Insert into loanTaken values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
       Connection conn = DBConnection.createConnection().getConnection();
       conn.setAutoCommit(false);
       try{
            PreparedStatement state = conn.prepareStatement(query);
            
            state.setObject(1,  loanTaken.getTakenId());
            state.setObject(2,  loanTaken.getMemId());
            state.setObject(3,  loanTaken.getLoanName());
            state.setObject(4,  loanTaken.isMethod());
            state.setObject(5,  loanTaken.getRate());
            state.setObject(6,  loanTaken.getRateType());
            state.setObject(7,  loanTaken.getFineType());
            state.setObject(8,  loanTaken.getGuaranteeType());
            state.setObject(9,  loanTaken.getTakenDate());
            state.setObject(10, loanTaken.getTakenTime());
            state.setObject(11, loanTaken.getPeriod());
            state.setObject(12, loanTaken.getAmount());
            state.setObject(13, loanTaken.getBalance());
            
            if(guaList == null){
                if(state.executeUpdate()>0){
                    if(PaymentDBController.addNewPayments(paymentList)){
                        conn.commit();
                        return true;
                    }
                    conn.rollback();
                    return false;
                }
                conn.rollback();
                return false;
            }
            
            if(state.executeUpdate()>0){
                if(GuarantorDBController.addNewGuarantors(guaList)){
                    if(PaymentDBController.addNewPayments(paymentList)){
                        conn.commit();
                        return true;
                    }
                    conn.rollback();
                    return false;
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
    
    /*public static ObservableList<Fixed>  getMemberCurrentLoanTakens(int memberId) throws ClassNotFoundException, SQLException {
    String query = "select * from loanTaken natural join account where accId = " + memberId + " and isWithdraw = false and isBond = false";
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
    }*/
    
    public static ObservableList<LoanTaken> getclientLoans() throws ClassNotFoundException, SQLException {
        String query = "select loanId, t.loanName \n" +
                       "from loanTaken t, loan l\n" +
                       "where t.loanName = l.loanName\n" +
                       "group by loanId";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<LoanTaken> clientLoans = FXCollections.observableArrayList();
        
        while (result.next()) {
            LoanTaken loanTaken = new LoanTaken();
           
            loanTaken.setTakenId(result.getInt(1));
            loanTaken.setLoanName(result.getString(2));
            
            clientLoans.add(loanTaken);
        }
        return clientLoans;
    }

    public static ObservableList<Integer> getDebtors() throws ClassNotFoundException, SQLException {
        String query = "select memId from loanTaken group by memId";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        ObservableList<Integer>  debtors = FXCollections.observableArrayList();
        while (result.next()) {
            debtors.add(result.getInt(1));
        }
        return debtors;
    }
   
    public static ObservableList<LoanTaken> getClientLoans(int memId) throws ClassNotFoundException, SQLException {
        String query = "select loanId, balance \n" +
                       "from loanTaken t, loan l\n" +
                       "where t.loanName = l.loanName and memId = "+memId;
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<LoanTaken> clientLoans = FXCollections.observableArrayList();
        
        while (result.next()) {
            LoanTaken loanTaken = new LoanTaken();
           
            loanTaken.setTakenId(result.getInt(1));
            loanTaken.setBalance(result.getDouble(2));
            
            clientLoans.add(loanTaken);
        }
        return clientLoans;
    }

    public static ObservableList<LoanSchedule> getLoanModelSchedule(String selectedItem) throws ClassNotFoundException, SQLException {
        String query = "select m.memId, name, takenDate, period, amount, balance\n" +
                       "from member m, loanTaken t\n" +
                       "where m.memId = t.memId and loanName = \""+selectedItem+"\"";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<LoanSchedule> loanSchedules = FXCollections.observableArrayList();
        
        while (result.next()) {
            LoanSchedule loanSchedule = new LoanSchedule();
           
            loanSchedule.setMemId(result.getInt(1));
            loanSchedule.setName(result.getString(2));
            loanSchedule.setTakenDate(result.getString(3));
            loanSchedule.setPeriod(result.getInt(4));
            loanSchedule.setAmount(result.getDouble(5));
            loanSchedule.setBalance(result.getDouble(6));
                       
            loanSchedules.add(loanSchedule);
        }
        return loanSchedules;
    }  

    public static void updateMemberLoanBalance(int takenId, Double balance) throws ClassNotFoundException, SQLException{
        String query = "Update loanTaken set balance = ? where takenId = ?";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        state.setObject(1, balance);
        state.setObject(2, takenId);       
        state.executeUpdate();
        
    }

    public static ObservableList<GraphModel> getLoanModelSum() throws ClassNotFoundException, SQLException {
        String query = "select loanName, sum(amount) from loanTaken group by loanName";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<GraphModel> modelSums = FXCollections.observableArrayList();
        String name = null;
        while (result.next()) {
            GraphModel modelSum = new GraphModel();     
            name = result.getString(1).split(" ")[0];
            modelSum.setCategory(name);
            modelSum.setNumber(result.getDouble(2));                      
            modelSums.add(modelSum);
        }
        return modelSums;
    }

}
