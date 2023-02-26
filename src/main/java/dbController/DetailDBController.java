package dbController;

import dbConnection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author RISITH-PC
 */
public class DetailDBController {
    
    public static Double getDetail(int detailId) throws ClassNotFoundException, SQLException {
        String query = "Select detail from detail where detailId = " + detailId;
        Connection conn = DBConnection.createConnection().getConnection();
        PreparedStatement state = conn.prepareStatement(query);
        ResultSet result = state.executeQuery();
        if (result.next()) {
            return result.getDouble(1);
        }
        return null;
    }
    
}
