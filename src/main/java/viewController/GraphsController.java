
package viewController;

import animation.FadeUp;
import dbController.MemberDBController;
import dbController.SharesDBController;
import dbModel.Shares;
import dbSubModel.MemberAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class GraphsController implements Initializable {
    @FXML
    private ComboBox<String> graphTypeCombo, dataTypeCombo, clientTypeCombo;

    @FXML
    private VBox clientTypeBox;
    
    @FXML
    private AnchorPane parentPane;
    
    private static ObservableList<ChartModel> modelList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        modelList = FXCollections.observableArrayList();
        new FadeUp(parentPane, 20, 0);
        prepareNode();
        gernerateGraph();
    }    
    
    @FXML
    public void clientTypeComboEvent(ActionEvent event) {
        new FadeUp(parentPane, 20, 0);
        gernerateGraph();
    }

    @FXML
    public void dataTypeComboEvent(ActionEvent event) {
        switch(dataTypeCombo.getSelectionModel().getSelectedIndex()){
            case 0: clientTypeBox.setVisible(true);break;
            default: clientTypeBox.setVisible(false);
        }
        new FadeUp(parentPane, 20, 0);
        gernerateGraph();
    }

    @FXML
    public void graphTypeComboEvent(ActionEvent event) {
        new FadeUp(parentPane, 20, 0);
        gernerateGraph();
    }

    
    private void prepareNode() {
         dataTypeCombo.getItems().setAll("Client-Normal",
                                        "Member-Compulsory",
                                        "Member-Shares",
                                        "Member-Attendance",
                                        "LoanType-Count",
                                        "LoanType-Profit");
         
         graphTypeCombo.getItems().setAll("Bar Chart",
                                          "Line Chart",
                                          "Area Chart",
                                          "Scatter Chart");
         
         clientTypeCombo.getItems().setAll("Member",
                                           "Non-Member");
         
         dataTypeCombo.getSelectionModel().select(0);
         graphTypeCombo.getSelectionModel().select(0);
         clientTypeCombo.getSelectionModel().select(0);
    }
     
    private BarChart<String,Number> getBarChart(String title, String xLabel, String yLabel, ObservableList<ChartModel> list){
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis,yAxis);
        
        barChart.setTitle(title);
        barChart.setAnimated(true);
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        
        XYChart.Series dataSeries = new XYChart.Series();
        
        for (ChartModel chartModel : list) {
            dataSeries.getData().add(new XYChart.Data(chartModel.getCategory(),chartModel.getNumber()));
        }
        barChart.getData().add(dataSeries);
        for (Node n : barChart.lookupAll(".default-color0.chart-bar")) {
            n.setStyle("-fx-bar-fill: rgb(43,87,154)");
        }
        return barChart;
    }

    private LineChart<String,Number> getLineChart(String title, String xLabel, String yLabel, ObservableList<ChartModel> list){
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
        
        lineChart.setTitle(title);
        lineChart.setAnimated(true);
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        
        XYChart.Series dataSeries = new XYChart.Series();
        
        for (ChartModel chartModel : list) {
            dataSeries.getData().add(new XYChart.Data(chartModel.getCategory(),chartModel.getNumber()));
        }
        lineChart.getData().add(dataSeries);
        for (Node n : lineChart.lookupAll(".default-color0.chart-series-line")) {
            n.setStyle("-fx-stroke: rgb(1,205,39)");
        }
        return lineChart;
    }
   
    private AreaChart<String,Number> getAreaChart(String title, String xLabel, String yLabel, ObservableList<ChartModel> list){
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final AreaChart<String,Number> areaChart = new AreaChart<String,Number>(xAxis,yAxis);
        
        areaChart.setTitle(title);
        areaChart.setAnimated(true);
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        
        XYChart.Series dataSeries = new XYChart.Series();
        
        for (ChartModel chartModel : list) {
            dataSeries.getData().add(new XYChart.Data(chartModel.getCategory(),chartModel.getNumber()));
        }
        areaChart.getData().add(dataSeries);
        
        return areaChart;
    }
    
    private ScatterChart<String,Number> getScatterChart(String title, String xLabel, String yLabel, ObservableList<ChartModel> list){
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final ScatterChart<String,Number> scatterChart = new ScatterChart<String,Number>(xAxis,yAxis);
        
        scatterChart.setTitle(title);
        scatterChart.setAnimated(true);
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        
        XYChart.Series dataSeries = new XYChart.Series();
        
        for (ChartModel chartModel : list) {
            dataSeries.getData().add(new XYChart.Data(chartModel.getCategory(),chartModel.getNumber()));
        }
        scatterChart.getData().add(dataSeries);
        return scatterChart;
    }

    private void gernerateGraph() {
        Node node = null;
        String title=null, xLabel=null, yLabel=null;
        try {
            switch (dataTypeCombo.getSelectionModel().getSelectedIndex()) {
                case 0:
                    if (clientTypeCombo.getSelectionModel().getSelectedIndex() == 0) {
                        title = "Member's Normal Deposit Balance";
                        xLabel = "Member Name";
                        yLabel = "Normal Balance";
                        for (MemberAccount memberAccount : MemberDBController.getAllClientWithAccount("isMember = true  AND isActive = true")) {
                            modelList.add(new ChartModel(memberAccount.getName(), memberAccount.getNorBalance()));
                        }
                    }else{
                        title = "Non-Member's Normal Deposit Balance";
                        xLabel = "Non-Member Name";
                        yLabel = "Normal Balance";
                        for (MemberAccount memberAccount : MemberDBController.getAllClientWithAccount("isMember = false  AND isActive = true")) {
                            modelList.add(new ChartModel(memberAccount.getName(), memberAccount.getNorBalance()));
                        }
                    }
                    break;
                case 1:
                    title = "Member's Compulsory Deposit Balance";
                    xLabel = "Member Name";
                    yLabel = "Compulsory Balance";
                    for (MemberAccount memberAccount : MemberDBController.getAllClientWithAccount("isMember = true  AND isActive = true")) {
                        modelList.add(new ChartModel(memberAccount.getName(), memberAccount.getComBalance()));
                    }
                    break;                
                case 2:
                    title = "Member's Shares Balance";
                    xLabel = "Member Name";
                    yLabel = "Shares Balance";
                    for (Shares shares : SharesDBController.getAllShareBalance()) {
                        modelList.add(new ChartModel(shares.getName(), shares.getBalance()));
                    }
                    break;
                case 3:
                case 4:
                case 5:
            }
            switch(graphTypeCombo.getSelectionModel().getSelectedIndex()){
                case 0:
                    node = getBarChart(title, xLabel, yLabel, modelList);
                    break;
                case 1:
                    node = getLineChart(title, xLabel, yLabel, modelList);
                    break;
                case 2:
                    node = getAreaChart(title, xLabel, yLabel, modelList);
                    break;
                case 3:
                    node = getScatterChart(title, xLabel, yLabel, modelList);
            }
            modelList.clear();
            AnchorPane.setRightAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setTopAnchor(node, 0.0);
            parentPane.getChildren().setAll(node);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GraphsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GraphsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    //------------------- graph model object ---------------------//
    public class ChartModel{
        private String category;
        private Double number;

        public ChartModel() {
        }

        public ChartModel(String category, Double number) {
            this.category = category;
            this.number = number;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Double getNumber() {
            return number;
        }

        public void setNumber(Double number) {
            this.number = number;
        }
        
    }
   
}
