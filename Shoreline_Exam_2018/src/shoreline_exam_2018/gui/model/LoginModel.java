/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import shoreline_exam_2018.be.LogType;
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

    public LoginModel() {
        bll = BLLManager.getInstance();
    }

    /**
     * Logs the user in. Displays an error if a wrong login atempt is made.
     */
    public void attemptLogin(String username, String password, Parent root, Stage loginStage) {
        try {
            bll.login(username, password);
            openMainView(root, loginStage);
            
        } catch (BLLExeption ex) {
            AlertFactory.showError("Wrong information", "The username and password combination doesn't exist. Please try again.");
        }
    }

    /**
     * Opens the MainView on login. Closes the LoginView. Sets the user that has logged in.
     * @param root
     * @param loginStage 
     */
    private void openMainView(Parent root, Stage loginStage) {
        try {
            Scene mainScene = new Scene(root);
            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.setTitle("Shoreline MappingTool");
            mainStage.getIcons().add(new Image("shoreline_exam_2018/logo.png"));
            mainStage.show();
            mainStage.setScene(mainScene);
            mainStage.centerOnScreen();
            
            bll.addLog(LogType.LOGIN, "User " + bll.getcurrentUser().getName() + " has logged in", bll.getcurrentUser());
            
            loginStage.close();
        } catch (BLLExeption ex) {
            Logger.getLogger(LoginModel.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
