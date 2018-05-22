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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import shoreline_exam_2018.gui.model.ConvertModel;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.ConversionJobMulti;
import shoreline_exam_2018.bll.ConversionJobSingle;
import shoreline_exam_2018.bll.ConversionJobs;

/**
 * FXML Controller class
 *
 * @author alexl
 */
public class ConvertController implements Initializable {

    private ConvertModel model;

    @FXML
    private ComboBox<Profile> profileCombobox;
    @FXML
    private TextField inputField;
    @FXML
    private TextField outputField;
    @FXML
    private ListView<ConversionJobs> listJobs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = new ConvertModel();
        model.prepareTasks();
        model.loadProfilesInCombo(profileCombobox);
        inputField.setEditable(false);
        outputField.setEditable(false);
    }

    /**
     * Loads a file from a File Chooser.
     */
    @FXML
    private void handleLoadButton(ActionEvent event) {
        inputField.setText(model.chooseFile());
    }

    /**
     * Sets a destination folder and file name.
     */
    @FXML
    private void handleOutputButton(ActionEvent event) {
        outputField.setText(model.chooseDestination());
    }

    /**
     * Starts conversion and resets fields if the job is created.
     */
    @FXML
    private void handleTaskButton(ActionEvent event) {
        File input = new File(inputField.getText());

        if (input.isFile()) {
            ConversionJobSingle StartConversion = model.StartSingleConversion(profileCombobox.getSelectionModel().getSelectedItem(), listJobs, inputField.getText());
            if (StartConversion != null) {
                listJobs.getItems().add(StartConversion);
                inputField.setText("");
            }
        }
        else if (input.isDirectory()) {
            ConversionJobMulti StartConversion = model.StartMultiConversion(profileCombobox.getSelectionModel().getSelectedItem(), listJobs, inputField.getText());
            if (StartConversion != null) {
                model.addFolderListener(StartConversion, inputField.getText());
                listJobs.getItems().add(StartConversion);
                inputField.setText("");
            }
        }

    }

    public ConvertModel getModel() {
        return model;
    }

    @FXML
    private void handleLoadDirectoryButton(ActionEvent event) {
        String directory = model.chooseDirectory();
        if (directory != null) {
            inputField.setText(directory);
        }
    }

}
