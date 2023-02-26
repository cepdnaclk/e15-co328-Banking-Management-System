
package viewController.sub;

import dbController.MemberDBController;
import dbSubModel.MemberAccount;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewAllMembersTableController implements Initializable {
   
    @FXML
    private TableView<MemberAccount> clientTable;
    
    @FXML
    private TableColumn<MemberAccount, Double> compulsoryColumn;

    @FXML
    private TableColumn<MemberAccount, String> addressColumn;

    @FXML
    private TableColumn<MemberAccount, String> contactColumn;

    @FXML
    private TableColumn<MemberAccount, Double> normalColumn;

    @FXML
    private TableColumn<MemberAccount, String> nameColumn;

    @FXML
    private TableColumn<MemberAccount, Integer> idColumn;

    @FXML
    private Button uploadBtn, saveBtnAM, cancelBtnNA, saveBtnNA, cancelBtnAM;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane parentPane, nonAppPane, appMemPane;

    @FXML
    private ToggleButton yesToggle;

    @FXML
    private TextField parentNameText;

    @FXML
    private TextField urlText, sizeText;

    @FXML
    private TextField applicantIdText;

    @FXML
    private TextField applicantNameText;

    @FXML
    private TextField shareAmountText;

    @FXML
    private ComboBox<?> parentCombo;

    @FXML
    private CheckBox shareCheak;

    private static String dataType;
    private static int selectedIndex;
    private static MenuItem item1,item2,item3,item4,item5,item6,item7,item8;
    private static TableView<MemberAccount> staticTable = new TableView<>();
    private FileChooser fileChooser;
    private File file;
    @Override
    public void initialize(URL url, ResourceBundle rb) {         
        idColumn.setCellValueFactory(new PropertyValueFactory<>("memId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        normalColumn.setCellValueFactory(new PropertyValueFactory<>("norBalance"));
        compulsoryColumn.setCellValueFactory(new PropertyValueFactory<>("comBalance"));
        
        //clientTable.getColumns().addAll(idColumn,nameColumn,addressColumn,contactColumn,normalColumn,compulsoryColumn);
        staticTable = clientTable;
        prepareAppMemPane();
        prepareFace(1);
        setTableData(selectedIndex);    
        createMenuItems();
        setTableMenu();
    }    
  
    @FXML
    void yesToggleEvent(ActionEvent event) {

    }

    @FXML
    void shareCheakEvent(ActionEvent event) {

    }

    @FXML
    void saveBtnNAEvent(ActionEvent event) {
        System.out.println("Non - Applicant");
    }

    @FXML
    void cancelBtnNAEvent(ActionEvent event) {
        prepareFace(1);
    }

    @FXML
    void saveBtnAMEvent(ActionEvent event) {
        try {
            MemberDBController.updateMember("isMember = true, isApplicant = false",Integer.parseInt(applicantIdText.getText()));
            MemberDBController.addImage(Integer.parseInt(applicantIdText.getText()), file);
            setTableData(selectedIndex);
            prepareFace(1);
            prepareAppMemPane();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewAllMembersTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewAllMembersTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViewAllMembersTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void cancelBtnAMEvent(ActionEvent event) {
        prepareFace(1);
        prepareAppMemPane();
    }

    @FXML
    void uploadBtnEvent(ActionEvent event) {     
        configureFileChooser(fileChooser);
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            urlText.setText(file.getAbsolutePath());
            sizeText.setText(file.length()/1024 + " KB");
            if(file.length()/1024<500){
                saveBtnAM.setDisable(false);
            }
            //MemberDBController.addImage(Integer.parseInt(applicantIdText.getText()), file);
            try {
                System.out.println("1");
                BufferedImage bufferedImage = ImageIO.read(file);
                System.out.println("2");
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                System.out.println(image == null);
                imageView.setImage(image);
                System.out.println("4");
            } catch (IOException ex) {
                Logger.getLogger(ViewAllMembersTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
  
    
    public static void setTableData(int selectedIndex) {
        ViewAllMembersTableController.selectedIndex = selectedIndex;
        try {
            switch(selectedIndex){
                case 0:
                    dataType = "isMember = true  AND isActive = true";                  
                    break;
                case 1:
                    dataType = "isMember = true  AND isActive = false";                
                    break;
                case 2:
                    dataType = "isMember = false  AND isActive = true AND isApplicant = false";               
                    break;
                case 3:
                    dataType = "isMember = false  AND isActive = false ";               
                    break;
                case 4:
                    dataType = "isActive = true AND isApplicant = true";                
            }
            staticTable.setItems(MemberDBController.getAllClientWithAccount(dataType));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewAllMembersTableController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewAllMembersTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setTableMenu() {  
        staticTable.setRowFactory((TableView<MemberAccount> table) -> {
            TableRow<MemberAccount> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();           
            
            contextMenu.getItems().addAll(item1,item2);
           
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                    .then((ContextMenu) null)
                    .otherwise(contextMenu)
            );

            row.setOnContextMenuRequested(evt -> {
                // update menu when requested
                switch (selectedIndex) {
                    case 0:
                        contextMenu.getItems().setAll(item1, item2);
                        break;
                    case 1:
                        contextMenu.getItems().setAll(item3);
                        break;
                    case 2:
                        contextMenu.getItems().setAll(item4, item5);
                        break;
                    case 3:
                        contextMenu.getItems().setAll(item3);
                        break;
                    case 4:
                        contextMenu.getItems().setAll(item6,item7,item8);
                        break;
                }
            });
            return row ;  
        });     
    }   

    private void createMenuItems() {
        //------------ Create Menu 1 -----------------//        
        item1 = new MenuItem("Inactive");
        item2 = new MenuItem("Set as Non Member");
        
        item1.setOnAction((ActionEvent event) ->{
                prepareFace(1);
        });
        item2.setOnAction((ActionEvent event) ->{
                prepareFace(1);
        });
        
        
        //------------ Create Menu 2 -----------------//       
        item3 = new MenuItem("Active");
       
        item3.setOnAction((ActionEvent event) ->{
                prepareFace(1);
        });      
        
        
        //------------ Create Menu 3 -----------------//
        
        item4 = new MenuItem("Inactive");
        item5 = new MenuItem("Set as Applicant");
        
        item4.setOnAction((ActionEvent event) ->{
                prepareFace(1);
        });
        item5.setOnAction((ActionEvent event) ->{
                prepareFace(2);
        });
              
        //------------ Create Menu 4 -----------------//
       
        item6 = new MenuItem("Inactive");
        item7 = new MenuItem("Set as Member");
        item8 = new MenuItem("Remove Applicant");
        
        item6.setOnAction((ActionEvent event) ->{
                prepareFace(1);
        });
        item7.setOnAction((ActionEvent event) ->{
            prepareAppMemPane();
            prepareFace(3);
            int rowCount = clientTable.getSelectionModel().getSelectedIndex();
            System.out.println(rowCount);
            applicantNameText.setText(clientTable.getItems().get(rowCount).getName());
            applicantIdText.setText(clientTable.getItems().get(rowCount).getMemId()+"");            
        });
        item8.setOnAction((ActionEvent event) ->{
                prepareFace(1);
        });
           
    } 

    private void prepareFace(int type) {
        switch (type) {
            case 1:
                nonAppPane.setVisible(false);
                appMemPane.setVisible(false);
                AnchorPane.setRightAnchor(clientTable, 0.0);
                break;
            case 2:
                nonAppPane.setVisible(true);
                appMemPane.setVisible(false);
                AnchorPane.setRightAnchor(clientTable, 292.0);
                break;
            case 3:
                nonAppPane.setVisible(false);
                appMemPane.setVisible(true);
                AnchorPane.setRightAnchor(clientTable, 292.0);
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Upload Member Image");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png")
        );
    }
    
    private void prepareAppMemPane() {
        fileChooser = new FileChooser();
        imageView.setImage(new Image("/images/emptyUser.jpg"));
        applicantNameText.setText("");
        applicantIdText.setText("");
        urlText.setText("");
        sizeText.setText("");
        saveBtnAM.setDisable(true);
    }

}
