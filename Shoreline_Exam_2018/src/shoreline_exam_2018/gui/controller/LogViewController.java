/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.gui.model.LogModel;

/**
 * FXML Controller class
 *
 * @author alexl
 */
public class LogViewController implements Initializable {

    @FXML
    private ListView<Log> listviewLog;
    private LogModel model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         model = new LogModel();
         model.setListItemString(listviewLog);
         model.loadLogItems();
         listviewLog.setItems(model.getLogItems());
    }    
    
    @FXML
    public void updateList() {
        model.loadLogItems();
    }
}
