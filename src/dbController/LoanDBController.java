
package dbController;

import dbConnection.DBConnection;
import dbModel.Loan;
import dbSubModel.MemberAccount;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LoanDBController {
    
    public static int getNextLoanModelId() throws ClassNotFoundException, SQLException {
        String query = "Select loanId from loan order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1;
    }
    
    public static boolean addNewLoanModel(Loan loan) throws ClassNotFoundException, SQLException {
       String query = "Insert into loan values(?,?,?,?,?,?,?,?,?,?,?)";
       Connection conn = DBConnection.createConnection().getConnection();
       conn.setAutoCommit(false);
       try{
            PreparedStatement state = conn.prepareStatement(query);
            
            state.setObject(1, loan.getLoanId());
            state.setObject(2, loan.getLoanName());
            state.setObject(3, loan.getRate());
            state.setObject(4, loan.getMaxAmount());
            state.setObject(5, loan.isForMember());
            state.setObject(6, loan.isMethod());
            state.setObject(7, loan.getRateType());
            state.setObject(8, loan.getFineType());
            state.setObject(9, loan.getGuaranteeType());
            state.setObject(10, loan.getMinPeriod());
            state.setObject(11, loan.getMaxPeriod());
            
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

    public static ObservableList<Loan> getAllLoanModel() throws ClassNotFoundException, SQLException {
        String query = "select * from loan";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<Loan> allLoanModels = FXCollections.observableArrayList();
        
        while (result.next()) {
           Loan loanModel = new Loan();
           loanModel.setLoanId(result.getInt(1));
           loanModel.setLoanName(result.getString(2));
           loanModel.setRate(result.getDouble(3));
           loanModel.setMaxAmount(result.getDouble(4));
           loanModel.setForMember(result.getBoolean(5));
           loanModel.setMethod(result.getBoolean(6));
           loanModel.setRateType(result.getInt(7));
           loanModel.setFineType(result.getInt(8));
           loanModel.setGuaranteeType(result.getInt(9));
           loanModel.setMinPeriod(result.getInt(10));
           loanModel.setMaxPeriod(result.getInt(11));
           
           loanModel.setMethodName((result.getBoolean(6))?"RB Method":"CI Method");
           
           allLoanModels.add(loanModel);
        }
        return allLoanModels;
    }
    
    public static Integer countLoanModels() throws ClassNotFoundException, SQLException {
        String query = "select count(loanId) from loan";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1);
        }
        return null;
    }

    public static boolean deleteLoanModel(int loanId) throws ClassNotFoundException, SQLException {
        String query = "Delete from loan where loanId = ?";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        state.setObject(1, loanId);
        return state.executeUpdate() > 0;
    }
    
}
