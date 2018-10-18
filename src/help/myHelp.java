
package help;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import viewController.SanasaController;

public class myHelp {
    
    public void makeStage(Stage oldStage, Label sourse, String loaderUrl, String title, boolean resizable, StageStyle style, boolean maximized,int type){
       try {
            Stage newStage = new Stage();
            oldStage = (Stage) sourse.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(loaderUrl));       
            Scene scene = new Scene(root);
            newStage.initStyle(style);
            newStage.setResizable(resizable);
            newStage.setMaximized(maximized);
            newStage.setTitle(title);
            newStage.setScene(scene);
            newStage.show();
            SanasaController.userType=type;
            oldStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    public Stage getStage(Stage stage, Label sourse){
        stage = (Stage) sourse.getScene().getWindow();
        return stage;
    }
}
