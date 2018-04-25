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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import shoreline_exam_2018.gui.model.LoginModel;

/**
 * FXML Controller class
 *
 * @author alexl
 */
public class LoginViewController implements Initializable {

    @FXML
    private TextField textfieldUsername;
    @FXML
    private TextField textviewPassword;
    
    private LoginModel model;
    @FXML
    private Button buttonLogin;

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
        model.attemptLogin(textfieldUsername.getText(), textviewPassword.getText(), buttonLogin);
    }
    
}
