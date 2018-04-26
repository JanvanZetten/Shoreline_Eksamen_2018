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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import shoreline_exam_2018.bll.ConversionTask;

import shoreline_exam_2018.gui.model.MainModel;

/**
 *
 * @author janvanzetten
 */
public class MainController implements Initializable {
    
    private MainModel model;
    @FXML
    private Tab tabConvert;
    @FXML
    private Button btnLoadFile;
    @FXML
    private Tab tabProfiles;
    @FXML
    private Tab tabLog;
    @FXML
    private Tab tabSettings;
    @FXML
    private ListView<ConversionTask> tblTasks;
    
    /**
     * Initializes the MainController.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new MainModel();
        model.prepareTasks();
    }    

    
    @FXML
    private void handleLoadFile(ActionEvent event) {
        model.chooseFile(); 
        model.setTask(tblTasks);
    }
    
}
