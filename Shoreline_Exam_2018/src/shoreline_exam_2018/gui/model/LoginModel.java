/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
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
    public void attemptLogin(String username, String password, Button button) {
        try {
            currentUser = bll.login(username, password);
            System.out.println(currentUser.toString());
        } catch (BLLExeption ex) {
            AlertFactory.showError("Wrong information", "The username and password combination doesn't exist. Please try again.");
        }
    }

}
