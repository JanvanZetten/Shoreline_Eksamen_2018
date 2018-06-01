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
import shoreline_exam_2018.gui.model.ConvertModel;
import shoreline_exam_2018.gui.model.NewProfileModel;

/**
 * FXML Controller class
 *
 * @author Asbamz
 */
public class NewProfileController implements Initializable
{
    @FXML
    private AnchorPane anchorMain;
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
    private AnchorPane paneHeader;
    @FXML
    private ScrollPane scrollMain;
    @FXML
    private Button btnBackToProfiles;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnSaveStructure;
    @FXML
    private Button btnSave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Platform.runLater(() ->
        {
            npm = new NewProfileModel(anchorMain, txtFieldProfileName, txtfieldSourcefile, btnSource, innerAnchor, splitPane, paneHeader, scrollMain, btnBack, btnSaveStructure, btnSave);
        });
    }

    private NewProfileModel npm;

    /**
     * Handle press on Source button.
     * @param event
     */
    @FXML
    private void handleSource(ActionEvent event)
    {
        npm.handleSource();
    }

    @FXML
    private void handleBackToProfiles(ActionEvent event)
    {
        npm.handleBackToProfiles();
    }

    @FXML
    private void handleBack(ActionEvent event)
    {
        npm.handleBack();
    }

    @FXML
    private void handleSaveStructure(ActionEvent event)
    {
        npm.handleSaveStructure();
    }

    /**
     * Handle press on Save button.
     * @param event
     */
    @FXML
    private void handleSave(ActionEvent event)
    {
        npm.handleSave();
    }

    /**
     * Adds model and tab.
     * @param cm
     * @param tabConvert
     */
    void addSharedInfo(ConvertModel cm, Tab tabConvert)
    {
        npm.addSharedInfo(cm, tabConvert);
    }

    /**
     * Get Model.
     * @return
     */
    public NewProfileModel getModel()
    {
        return npm;
    }
}
