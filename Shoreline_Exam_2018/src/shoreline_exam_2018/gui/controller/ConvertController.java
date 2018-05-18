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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import shoreline_exam_2018.gui.model.ConvertModel;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.ConversionJob;

/**
 * FXML Controller class
 *
 * @author alexl
 */
public class ConvertController implements Initializable
{

    private ConvertModel model;
    
    @FXML
    private ComboBox<Profile> profileCombobox;
    @FXML
    private TextField inputField;
    @FXML
    private TextField outputField;
    @FXML
    private ListView<ConversionJob> listJobs;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        model = new ConvertModel();
        model.prepareTasks();
        model.loadProfilesInCombo(profileCombobox);
        inputField.setEditable(false);
        outputField.setEditable(false);
        model.loadProperties(inputField, outputField);
    }

    /**
     * Loads a file from a File Chooser.
     */
    @FXML
    private void handleLoadButton(ActionEvent event)
    {
        inputField.setText(model.chooseFile());
    }
    
    /**
     * Sets a destination folder and file name.
     */
    @FXML
    private void handleOutputButton(ActionEvent event)
    {
        outputField.setText(model.chooseDestination());
    }

    /**
     * Starts conversion and resets fields if the job is created.
     */
    @FXML
    private void handleTaskButton(ActionEvent event)
    {

        ConversionJob StartConversion = model.StartConversion(profileCombobox.getSelectionModel().getSelectedItem(), listJobs);
        if (StartConversion != null){
            listJobs.getItems().add(StartConversion);
            inputField.setText("");
            outputField.setText("");
        }
        
    }

    public ConvertModel getModel()
    {
        return model;
    }

}
