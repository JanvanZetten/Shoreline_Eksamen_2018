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
import shoreline_exam_2018.gui.controller.LoginController;

/**
 *
 * @author janvanzetten
 */
public class Shoreline_Exam_2018 extends Application {

    Scene scene;
    Stage loginStage;
    Parent preloadMainView;
    Image logo;
    LoginController loginController;

    /**
     * this does the slow startup stuff because of preloader
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/view/LoginView.fxml"));
        Parent root = loader.load();

        scene = new Scene(root);
        logo = new Image("shoreline_exam_2018/logo.png");
        preloadMainView = FXMLLoader.load(getClass().getResource("/shoreline_exam_2018/gui/view/MainView.fxml"));

        loginController = (LoginController) loader.getController();
    }

    /**
     * This does the startup stuff wich need the JavaFX thread
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        loginStage = stage;
        stage.setScene(scene);
        stage.setTitle("Shoreline MappingTool Login");
        stage.getIcons().add(logo);
        stage.show();
        
        loginController.setPreLoad(preloadMainView, loginStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
