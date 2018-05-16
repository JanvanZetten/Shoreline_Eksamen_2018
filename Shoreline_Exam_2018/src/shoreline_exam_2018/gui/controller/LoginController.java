/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import shoreline_exam_2018.gui.model.LoginModel;

/**
 * FXML Controller class
 *
 * @author alexl
 */
public class LoginController implements Initializable {

    @FXML
    private TextField textfieldUsername;
    @FXML
    private TextField textviewPassword;
    @FXML
    private Button buttonLogin;
    
    private LoginModel model;
    private Parent preloadMainView;
    private Stage loginStage;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new LoginModel();
    }    

    /**
     * Button to log in. Sends information to LoginModel.
     * @param event 
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        model.attemptLogin(textfieldUsername.getText(), textviewPassword.getText(), buttonLogin, preloadMainView, loginStage);
    }
    
    public void setPreLoad(Parent preloadMainView, Stage loginStage) {
        this.preloadMainView = preloadMainView;
        this.loginStage = loginStage;
    }
}
