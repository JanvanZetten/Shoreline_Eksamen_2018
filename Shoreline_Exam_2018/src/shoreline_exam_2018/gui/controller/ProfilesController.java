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
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import shoreline_exam_2018.be.Profile;
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
    private AnchorPane anchorMain;
    @FXML
    private AnchorPane anchorProfiles;
    @FXML
    private AnchorPane anchorNewProfile;
    @FXML
    private Button btnNewProfile;
    @FXML
    private ListView<Profile> listviewProfiles;
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
    private Button btnBack;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Platform.runLater(() ->
        {
            pm = new ProfilesModel(anchorMain, anchorProfiles, anchorNewProfile, btnNewProfile, listviewProfiles, txtFieldProfileName, txtfieldSourcefile, btnSource, innerAnchor, splitPane, paneHeader, scrollMain, btnBack, btnDelete, btnUpdate);
        });
    }

    private ProfilesModel pm;

    @FXML
    private void handleNewProfile(ActionEvent event)
    {
        pm.handleNewProfile();
    }

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
    private void handleDelete(ActionEvent event)
    {
        pm.handleDelete();
    }

    @FXML
    private void handleUpdate(ActionEvent event)
    {
        pm.handleUpdate();
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
