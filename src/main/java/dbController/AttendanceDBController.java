package dbController;


import dbConnection.DBConnection;
import dbSubModel.GraphModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AttendanceDBController {

    public static ObservableList<GraphModel> getMonthlyAttendance(int year) throws ClassNotFoundException, SQLException {
        String query = "select month(meetDate), sum(status) from attendance where year(meetDate)= "+year+" \n" +
                       "group by month(meetDate);";
        Connection conn = DBConnection.createConnection().getConnection();
        Statement state = conn.createStatement();
        ResultSet result = state.executeQuery(query);
        
        ObservableList<GraphModel> attendance = FXCollections.observableArrayList();
        String month = null;
        while (result.next()) {
            GraphModel attend = new GraphModel();     
            switch(result.getInt(1)){
                case 1: month="January"; break;
                case 2: month="February"; break;
                case 3: month="March"; break;
                case 4: month="April"; break;
                case 5: month="May"; break;
                case 6: month="June"; break;
                case 7: month="July"; break;
                case 8: month="August"; break;
                case 9: month="September"; break;
                case 10: month="October"; break;
                case 11: month="November"; break;
                case 12: month="December"; break;
            }
            attend.setCategory(month);
            attend.setNumber(result.getDouble(2));                      
            attendance.add(attend);
        }
        return attendance;
    }
    
}
