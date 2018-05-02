/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import shoreline_exam_2018.gui.model.ConvertModel;
import shoreline_exam_2018.gui.model.ProfilesModel;

/**
 * FXML Controller class
 *
 * @author Asbamz
 */
public class ProfilesController implements Initializable
{
    @FXML
    private GridPane grid;
    @FXML
    private TextField txtfieldSourcefile;
    @FXML
    private TextField txtFieldProfileName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Platform.runLater(() ->
        {
            pm = new ProfilesModel(grid, txtfieldSourcefile, txtFieldProfileName);
        });
    }

    private ProfilesModel pm;

    /**
     * Handle press on Source button.
     * @param event
     */
    @FXML
    private void handleSource(ActionEvent event)
    {
        pm.handleSource();
    }

    /**
     * Handle press on Save button.
     * @param event
     */
    @FXML
    private void handleSave(ActionEvent event)
    {
        pm.handleSave();
    }

    /**
     * Adds model and tab.
     * @param cm
     * @param tabConvert
     */
    public void addSharedInfo(ConvertModel cm, Tab tabConvert)
    {
        pm.addSharedInfo(cm, tabConvert);
    }
}
