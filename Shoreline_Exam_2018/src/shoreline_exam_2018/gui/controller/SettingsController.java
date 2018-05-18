/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import shoreline_exam_2018.gui.model.SettingsModel;

/**
 * FXML Controller class
 *
 * @author alexl
 */
public class SettingsController implements Initializable {

    @FXML
    private TextField txtfieldInputDir;
    @FXML
    private TextField txtfieldOutputDir;
    
    private SettingsModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new SettingsModel();
    }

    @FXML
    private void handleInputDir(ActionEvent event) {
        model.newDefaultDir("inputDir");
    }

    @FXML
    private void handleOutputDir(ActionEvent event) {
        model.newDefaultDir("outputDir");
    }

}
