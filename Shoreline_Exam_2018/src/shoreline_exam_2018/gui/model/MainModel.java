/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import shoreline_exam_2018.gui.controller.ConvertController;
import shoreline_exam_2018.gui.controller.ProfilesController;

/**
 *
 * @author janvanzetten
 */
public class MainModel
{

    private Tab tabConvert;
    private ConvertModel cm;

    /**
     * Sets the tabs in all the tabs of the MainView.
     *
     * @param paneConvert = The convertion view.
     * @param paneProfiles = The profiles view.
     * @param paneLog = The log view.
     * @param paneSettings = The settings view.
     */
    public void setupTabs(AnchorPane paneConvert, AnchorPane paneProfiles, AnchorPane paneLog, AnchorPane paneSettings, Tab tabConvert)
    {
        setPane("Convert", paneConvert);
        setPane("Profiles", paneProfiles);
        setPane("Log", paneLog);
        setPane("Settings", paneSettings);
        this.tabConvert = tabConvert;
    }

    /**
     * Rescales the views to have the same anchors as the tab panes they are
     * located in. Also sets a node of the view into the tab pane.
     * @param PANE_NAME = Name of the tab.
     * @param PANE = Name of the pane.
     */
    private void setPane(String PANE_NAME, AnchorPane PANE)
    {
        try
        {
            URL url = new File(System.getProperty("user.dir") + "/src/shoreline_exam_2018/gui/view/" + PANE_NAME + "View.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);

            Node node = (Node) loader.load();

            if (PANE_NAME.equalsIgnoreCase("Convert"))
            {
                ConvertController cc = (ConvertController) loader.getController();
                Platform.runLater(() ->
                {
                    cm = cc.getModel();
                });
            }
            else if (PANE_NAME.equalsIgnoreCase("Profiles"))
            {
                ProfilesController pc = (ProfilesController) loader.getController();
                Platform.runLater(() ->
                {
                    pc.addSharedInfo(cm, tabConvert);
                });
            }

            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            PANE.getChildren().setAll(node);
        }
        catch (MalformedURLException ex)
        {
            Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sets tab for Convert View.
     * @param tabConvert
     */
    public void setTabConvert(Tab tabConvert)
    {
        this.tabConvert = tabConvert;
    }
}
