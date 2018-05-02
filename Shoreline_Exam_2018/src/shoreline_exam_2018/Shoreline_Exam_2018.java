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
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author janvanzetten
 */
public class Shoreline_Exam_2018 extends Application {

    Scene scene;
    Image logo;

    /**
     * this does the slow startup stuff because of preloader
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("gui/view/MainView.fxml"));
        scene = new Scene(root);
        logo = new Image("shoreline_exam_2018/logo.png");

    }

    /**
     * This does the startup stuff wich need the JavaFX thread
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(scene);
        stage.setTitle("Shoreline MappingTool");
        stage.getIcons().add(logo);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
