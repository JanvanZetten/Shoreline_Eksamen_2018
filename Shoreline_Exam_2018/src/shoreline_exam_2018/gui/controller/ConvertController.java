/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author alexl
 */
public class ConvertController implements Initializable {

    @FXML
    private Button btnLoadFile;
    @FXML
    private ListView<?> tblTasks;
    @FXML
    private Button btnConvert;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleLoadButton(ActionEvent event) {
//        model.chooseFile();
    }

    @FXML
    private void handleTaskButton(ActionEvent event) {
//        model.convertTest(tblTasks);
    }
    
}
