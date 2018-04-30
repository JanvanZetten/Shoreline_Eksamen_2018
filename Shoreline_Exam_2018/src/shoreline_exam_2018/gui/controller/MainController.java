/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import shoreline_exam_2018.bll.ConversionTask;

import shoreline_exam_2018.gui.model.MainModel;

/**
 *
 * @author janvanzetten
 */
public class MainController implements Initializable
{

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
    @FXML
    private Button btnConvert;
    @FXML
    private AnchorPane paneConvert;
    @FXML
    private AnchorPane paneProfiles;
    @FXML
    private AnchorPane paneLog;
    @FXML
    private AnchorPane paneSettings;

    /**
     * Initializes the MainController.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        model = new MainModel();
        model.prepareTasks();

        try
        {
            model.setupTabs(paneConvert, paneProfiles, paneLog, paneSettings);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads a file from a File Chooser.
     *
     * @param event
     */
    @FXML
    private void handleLoadFile(ActionEvent event)
    {
        model.chooseFile();
    }

    /**
     * Test button which creates a task and sets it into the view.
     *
     * @param event
     */
    @FXML
    private void handleConvert(ActionEvent event)
    {
        model.convertTest(tblTasks);
    }

}
