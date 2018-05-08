/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

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
    private Tab tabProfiles;
    @FXML
    private AnchorPane paneConvert;
    @FXML
    private AnchorPane paneProfiles;
    private AnchorPane paneLog = new AnchorPane();
    private AnchorPane paneSettings = new AnchorPane();

    /**
     * Initializes the MainController.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        model = new MainModel();
        Platform.runLater(() ->
        {
            model.setupTabs(paneConvert, paneProfiles, paneLog, paneSettings, tabConvert);
        });
    }

}
