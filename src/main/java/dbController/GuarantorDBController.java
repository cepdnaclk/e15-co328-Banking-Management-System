
package dbController;


import dbConnection.DBConnection;
import dbModel.Guarantor;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuarantorDBController {
    
    public static int getNextGuarantorId() throws ClassNotFoundException, SQLException {
        String query = "Select guaId from guarantor order by 1 desc limit 1";
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1;
    }
    
    public static boolean addNewGuarantors(ObservableList<Guarantor> guarantorList) throws ClassNotFoundException, SQLException {
        String query = "Insert into guarantor values(?,?,?)";
        Connection conn = DBConnection.createConnection().getConnection();
        int count = 0;
        for (Guarantor guarantor : guarantorList) {
            PreparedStatement state = conn.prepareStatement(query);

            state.setObject(1, guarantor.getGuaId());
            state.setObject(2, guarantor.getTakenId());
            state.setObject(3, guarantor.getMemId());
            count += state.executeUpdate();
        }
        return count == guarantorList.size();
    }
}
