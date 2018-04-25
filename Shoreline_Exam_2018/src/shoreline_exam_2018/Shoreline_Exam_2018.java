/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author janvanzetten
 */
public class Shoreline_Exam_2018 extends Application {
    Scene scene;
    
     /**
     * this does the slow stuff because of preloader
     * @throws Exception
     */
    @Override
    public void init() throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("gui/view/MainView.fxml"));
        scene = new Scene(root);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(scene);
        stage.setTitle("Shoreline MappingTool");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
