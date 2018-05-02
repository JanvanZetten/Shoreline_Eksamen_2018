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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import shoreline_exam_2018.gui.model.ConvertModel;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.ConversionTask;

/**
 * FXML Controller class
 *
 * @author alexl
 */
public class ConvertController implements Initializable {

    @FXML
    private Button btnLoadFile;
    @FXML
    private Button btnConvert;
    @FXML
    private ScrollPane paneScroll;
    @FXML
    private ComboBox<Profile> profileCombobox;
    @FXML
    private FlowPane paneTasks;
    
    private ConvertModel model;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new ConvertModel();
        model.prepareTasks();
        model.loadProfilesInCombo(profileCombobox);
        paneTasks.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
    }    

    /**
     * Loads a file from a File Chooser.
     *
     * @param event
     */
    @FXML
    private void handleLoadButton(ActionEvent event) {
        model.chooseFile();
    }

    /**
     * Test button which creates a task and sets it into the view.
     *
     * @param event
     */
    @FXML
    private void handleTaskButton(ActionEvent event) {
        model.convertTest(paneTasks);
    }
    
}
