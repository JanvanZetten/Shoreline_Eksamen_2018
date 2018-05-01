/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import shoreline_exam_2018.bll.BLLExeption;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author Asbamz
 */
public class ProfilesModel
{
    private BLLManager bll;
    private GridPane gridPane;
    private int rowCount;

    public ProfilesModel(GridPane gridPane)
    {
        this.bll = new BLLManager();
        this.gridPane = gridPane;
        rowCount = 0;
    }

    public void getDataFromFile(Path path)
    {
        try
        {
            List<String> headers = bll.getHeadersFromFile(path);
            for (String header : headers)
            {
                addGridRow(header);
            }
        }
        catch (BLLExeption ex)
        {
            Logger.getLogger(ProfilesModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addGridRow(String header)
    {
        System.out.println(rowCount);
        Label lbl = new Label(header);
        TextField txtField = new TextField();
        GridPane.setConstraints(lbl, 0, rowCount);
        GridPane.setConstraints(txtField, 2, rowCount);
        gridPane.getChildren().addAll(lbl, txtField);
        rowCount++;
    }
}
