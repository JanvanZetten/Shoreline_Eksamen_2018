/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.controller;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Platform.runLater(() ->
        {
            pm = new ProfilesModel(grid);
            pm.getDataFromFile(Paths.get(System.getProperty("user.dir") + "/test/shoreline_exam_2018/MockTilJunitTest.xlsx"));
        });
    }

    private ProfilesModel pm;

}
