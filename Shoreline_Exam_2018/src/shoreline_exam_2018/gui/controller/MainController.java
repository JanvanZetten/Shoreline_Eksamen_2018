/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import shoreline_exam_2018.gui.model.MainModel;

/**
 *
 * @author janvanzetten
 */
public class MainController implements Initializable {
    
    @FXML
    private Label label;
    private MainModel model;
    
    /**
     * Initializes the MainController.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new MainModel();
    }    
    
}
