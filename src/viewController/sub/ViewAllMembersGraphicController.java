/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewController.sub;

import dbController.MemberDBController;
import dbSubModel.MemberAccount;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author RISITH-PC
 */
public class ViewAllMembersGraphicController implements Initializable {
    
    @FXML
    private FlowPane flowPane;
    
    private static String dataType;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        try {
            dataType = "isMember = true  AND isActive = true";
            ObservableList<MemberAccount> members = MemberDBController.getAllClientWithAccount(dataType);
            for (MemberAccount member : members) {
                Image image = convertToJavaFXImage(MemberDBController.getImage(member.getMemId()),50,50);
                String text = member.getMemId()+" - "+member.getName();
                if(image == null){
                    flowPane.getChildren().add(new imagePane(new Image("/images/emptyUser.jpg"),text));
                }else{
                    flowPane.getChildren().add(new imagePane(image, text));
                }                
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ViewAllMembersGraphicController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ViewAllMembersGraphicController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ViewAllMembersGraphicController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private static Image convertToJavaFXImage(byte[] raw, final int width, final int height) {
        if(raw!=null){
            WritableImage image = new WritableImage(width, height);
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(raw);
                BufferedImage read = ImageIO.read(bis);
                image = SwingFXUtils.toFXImage(read, null);
            } catch (IOException ex) {
                Logger.getLogger(ViewAllMembersTableController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return image;
        }
        return null;
    }

    //-------------------- Create Image Pane Object -------------------------//
    public class imagePane extends Pane {
        ImageView image;
        Pane myPane;
        Label label;
        public imagePane(Image photo, String name) {
            image = new ImageView(photo);
            image.setFitHeight(157.8);
            image.setFitWidth(129);
            image.setPreserveRatio(false);
            image.setLayoutX(3);
            image.setLayoutY(3);
            //---------------------------------------//
            myPane = new Pane();
            myPane.setPrefSize(135, 35);
            myPane.setLayoutX(0);
            myPane.setLayoutY(129);
            myPane.setOpacity(0.9);
            myPane.setStyle("-fx-background-color: rgb(43,87,154)");
            //---------------------------------------//
            label = new Label();
            label.setText(name);
            label.setLayoutX(5);
            label.setLayoutY(135);
            label.setFont(new Font("Segoe UI Semibold", 12));
            label.setStyle("-fx-text-fill: rgb(255,255,255)");
            //---------------------------------------//
            getChildren().addAll(image,myPane,label);
            setPrefSize(135, 163.8);
            setStyle("-fx-background-color: rgb(43,87,154)");
        }
    }

}

