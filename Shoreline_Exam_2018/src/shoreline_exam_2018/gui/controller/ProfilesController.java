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
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    private TextField txtFieldProfileName;
    @FXML
    private TextField txtfieldSourcefile;
    @FXML
    private Button btnSource;
    @FXML
    private AnchorPane innerAnchor;
    @FXML
    private SplitPane splitPane;
    @FXML
    private ScrollPane scrollHeader;
    @FXML
    private GridPane gridDrag;
    @FXML
    private ScrollPane scrollMain;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnBack;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Platform.runLater(() ->
        {
            pm = new ProfilesModel(txtFieldProfileName, txtfieldSourcefile, btnSource, innerAnchor, splitPane, scrollHeader, gridDrag, scrollMain, btnBack, btnSave);
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

    @FXML
    private void handleBack(ActionEvent event)
    {
        pm.handleBack();
    }

    @FXML
    private void handleSaveStructure(ActionEvent event)
    {
        pm.handleSaveStructure();
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
