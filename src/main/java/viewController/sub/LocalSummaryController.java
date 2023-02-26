
package viewController.sub;

import dbController.AccountDBController;
import dbController.AttendanceDBController;
import dbController.LoanTakenDBController;
import dbController.SharesDBController;
import dbSubModel.GraphModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalSummaryController implements Initializable {
    @FXML
    private CategoryAxis xLAxis, xDAxis, xSAxis, xAAxis;

    @FXML
    private NumberAxis yLAxis, yDAxis, ySAxis, yAAxis;

    @FXML
    private BarChart<String, Number> loanChart, depositChart;
    
    @FXML
    private LineChart<String, Number> shareChart, attendanceChart;
    
    private static ObservableList<GraphModel> modelList;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modelList = FXCollections.observableArrayList();
        setLoanChart();
        setDepositChart();
        setShareChart();
        setAttendanceChart();
    }    

    private void setLoanChart() {
        try {
            XYChart.Series dataSeries = new XYChart.Series();
            modelList = LoanTakenDBController.getLoanModelSum();
            for (GraphModel graphModel : modelList) {
                dataSeries.getData().add(new XYChart.Data(graphModel.getCategory(),graphModel.getNumber()));
            }            
            loanChart.getData().add(dataSeries);
            for (Node n : loanChart.lookupAll(".default-color0.chart-bar")) {
                n.setStyle("-fx-bar-fill: #13418b");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocalSummaryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LocalSummaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setDepositChart() {
        try {
            XYChart.Series dataSeries = new XYChart.Series();
            modelList = AccountDBController.getAccountSum();
            for (GraphModel graphModel : modelList) {
                dataSeries.getData().add(new XYChart.Data(graphModel.getCategory(),graphModel.getNumber()));
            }           
            depositChart.getData().add(dataSeries);
            for (Node n : depositChart.lookupAll(".default-color0.chart-bar")) {
                n.setStyle("-fx-bar-fill: #13418b");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocalSummaryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LocalSummaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setShareChart() {
        try {
            XYChart.Series dataSeries = new XYChart.Series();
            modelList = SharesDBController.getAccountSum();
            for (GraphModel graphModel : modelList) {
                dataSeries.getData().add(new XYChart.Data(graphModel.getCategory(),graphModel.getNumber()));
            }           
            shareChart.getData().add(dataSeries);
            for (Node n : shareChart.lookupAll(".default-color0.chart-series-line")) {
                n.setStyle("-fx-stroke: rgb(43,87,154)");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocalSummaryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LocalSummaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setAttendanceChart() {
        try {
            XYChart.Series dataSeries = new XYChart.Series();
            modelList = AttendanceDBController.getMonthlyAttendance(2016);
            for (GraphModel graphModel : modelList) {
                dataSeries.getData().add(new XYChart.Data(graphModel.getCategory(),graphModel.getNumber()));
            }           
            attendanceChart.getData().add(dataSeries);
            for (Node n : attendanceChart.lookupAll(".default-color0.chart-series-line")) {
                n.setStyle("-fx-stroke: rgb(43,87,154)");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocalSummaryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LocalSummaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
