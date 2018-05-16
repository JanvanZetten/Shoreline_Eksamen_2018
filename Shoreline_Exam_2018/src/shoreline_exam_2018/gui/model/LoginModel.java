/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.bll.BLLExeption;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.gui.model.AlertFactory;

/**
 *
 * @author alexl
 */
public class LoginModel {

    private BLLFacade bll;
    private User currentUser;

    public LoginModel() {
        bll = new BLLManager();
    }

    /**
     * Logs the user in.
     */
    public void attemptLogin(String username, String password, Parent root, Stage loginStage) {
        try {
            currentUser = bll.login(username, password);
            openMainView(root, loginStage);
        } catch (BLLExeption ex) {
            AlertFactory.showError("Wrong information", "The username and password combination doesn't exist. Please try again.");
        }
    }

    private void openMainView(Parent root, Stage loginStage) {
            Scene mainScene = new Scene(root);
            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.setTitle("Shoreline MappingTool");
            mainStage.getIcons().add(new Image("shoreline_exam_2018/logo.png"));
            mainStage.show();
            mainStage.setScene(mainScene);
            mainStage.centerOnScreen();
            
            loginStage.close();
    }
}
