
package dbController;

import dbConnection.DBConnection;
import dbModel.Member;
import dbSubModel.MemberAccount;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MemberDBController {
    
    public static int getNextMemberId() throws ClassNotFoundException, SQLException {
        String query = "Select memId from member order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1000;
    }
    
    public static boolean addNewClient(Member member) throws ClassNotFoundException, SQLException {
        String query = "Insert into member values(?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        
        state.setObject(1 , member.getMemId());
        state.setObject(2 , member.getName());
        state.setObject(3 , member.getAddress());
        state.setObject(4 , member.getDob());
        state.setObject(5 , member.getNic());
        state.setObject(6 , member.getContact());
        state.setObject(7 , member.isGender());
        state.setObject(8 , member.isIsMember());
        state.setObject(9 , member.isIsActive());
        state.setObject(10, member.isIsApplicant());
        state.setObject(11, member.getParentId());
        if (state.executeUpdate() > 0){
            return true;
        }
        return false;
    }

    public static Member searchMemberFromId(int memId) throws ClassNotFoundException, SQLException {
        String query = "Select * from member where memId = ?";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        state.setObject(1, memId);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            Member member = new Member();
            
            member.setMemId(result.getInt(1));
            member.setName(result.getString(2));
            member.setAddress(result.getString(3));
            member.setDob(result.getString(4));
            member.setNic(result.getString(5));
            member.setContact(result.getString(6));
            member.setGender(result.getBoolean(7));
            member.setIsMember(result.getBoolean(8));
            member.setIsActive(result.getBoolean(9));
            member.setIsApplicant(result.getBoolean(10));
            member.setParentId(result.getInt(11));
            return member;
        }
        return null;
    }
    
    public static ObservableList<String> likeSearchMember (String text) throws ClassNotFoundException, SQLException {
        String query = "Select memId, name from member where name like '%" + text + "%' and isMember = true and isActive = true;";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet set = state.executeQuery(query);
        ObservableList<String> results = FXCollections.observableArrayList();
        while(set.next()){
            results.add(set.getString(1)+" - "+set.getString(2));
        }
        return results;
    }
    
    public static ObservableList<String> likeSearchClient (String text) throws ClassNotFoundException, SQLException {
        String query = "Select memId, name from member where name like '%" + text + "%' and isActive = true;";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet set = state.executeQuery(query);
        ObservableList<String> results = FXCollections.observableArrayList();
        while(set.next()){
            results.add(set.getString(1)+" - "+set.getString(2));
        }
        return results;
    }
    
    public static ObservableList<MemberAccount> getAllClientWithAccount(String dataType) throws ClassNotFoundException, SQLException {
        String query = "select * from member natural join account where "+dataType;
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<MemberAccount> allMembers = FXCollections.observableArrayList();
        
        while (result.next()) {
           MemberAccount member = new MemberAccount();
            
           member.setMemId(result.getInt(1));
           member.setName(result.getString(2));
           member.setAddress(result.getString(3));
           member.setDob(result.getString(4));
           member.setNic(result.getString(5));
           member.setContact(result.getString(6));
           member.setGender(result.getBoolean(7));
           member.setIsMember(result.getBoolean(8));
           member.setIsActive(result.getBoolean(9));
           member.setIsApplicant(result.getBoolean(10));
           member.setParentId(result.getInt(11));
           member.setAccId(result.getInt(12));
           member.setNorBalance(result.getDouble(13));
           member.setComBalance(result.getDouble(14));
           
           allMembers.add(member);
        }
        return allMembers;       
    }

    public static void initializeImage(int memId) throws ClassNotFoundException, SQLException {
        String query = "Insert into memImage values(?,?)";
        Connection conn = DBConnection.createConnection().getConnection();        
        PreparedStatement state = conn.prepareStatement(query);    
        state.setObject(1, memId);
        state.setObject(2, null);
        state.executeUpdate();                     
    }

    public static void addImage (int memId, File file) throws ClassNotFoundException, SQLException, IOException {
        FileInputStream stream = null;
        try {           
            String query = "Update memImage set image = ? where memId = ?";
            Connection conn = DBConnection.createConnection().getConnection();
            conn.setAutoCommit(false);
            PreparedStatement state = conn.prepareStatement(query);            
            stream = new FileInputStream(file);                       
            state.setBinaryStream(1, stream, (int) file.length()); 
            state.setObject(2, memId);
            state.executeUpdate();           
            conn.commit();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MemberDBController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public static byte[] getImage (int memId) throws ClassNotFoundException, SQLException, IOException {         
        String query = "Select image from memImage where memId = "+memId;
        Connection conn = DBConnection.createConnection().getConnection();
        conn.setAutoCommit(false);
        PreparedStatement state = conn.prepareStatement(query);            
        ResultSet result = state.executeQuery();
        if(result.next()){
            return result.getBytes(1);
        }
        return null;
    }

    public static boolean updateMember(String text, int memId) throws ClassNotFoundException, SQLException {
        String query = "Update member set " + text + " where memId = " + memId;
        System.out.println(query);
        Connection conn = DBConnection.createConnection().getConnection();
        conn.setAutoCommit(false);
        try {
            PreparedStatement state = conn.prepareStatement(query);
            if (state.executeUpdate() > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
