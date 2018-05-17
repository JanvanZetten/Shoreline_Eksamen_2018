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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private String handleInputDir(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();

        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        dc.setInitialDirectory(dir);
        dc.setTitle("Load a file");
        
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            return selectedFile.toString();
        }
        return "";
    }

    @FXML
    private String handleOutputDir(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();

        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        dc.setInitialDirectory(dir);
        dc.setTitle("Load a file");
        
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            return selectedFile.toString();
        }
        return "";
    }

}
