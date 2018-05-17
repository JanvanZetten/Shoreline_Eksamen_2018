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
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import shoreline_exam_2018.be.User;

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
    private Tab tabLog;
    @FXML
    private Tab tabSettings;
    
    @FXML
    private AnchorPane paneConvert;
    @FXML
    private AnchorPane paneProfiles;
    @FXML
    private AnchorPane paneLog;
    @FXML
    private AnchorPane paneSettings;
    
    private int GRAPHIC_SIZE = 36;

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
        tabLog.setGraphic(new FlowPane(new ImageView(new Image("shoreline_exam_2018/resources/log.png", GRAPHIC_SIZE, GRAPHIC_SIZE, true, true))));
        tabSettings.setGraphic(new FlowPane(new ImageView(new Image("shoreline_exam_2018/resources/settings.png", GRAPHIC_SIZE, GRAPHIC_SIZE, true, true))));
    }

    public void setCurrentUser(User currentUser) {
        model.setCurrentUser(currentUser);
    }

}
