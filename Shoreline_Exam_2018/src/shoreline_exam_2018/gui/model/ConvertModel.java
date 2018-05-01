/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.BLLExeption;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.gui.model.ConvertModel;

/**
 *
 * @author alexl
 */
public class ConvertModel {
    
    ObservableList<Profile> profiles = FXCollections.observableArrayList();
    BLLFacade bll = new BLLManager();

    public void chooseFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void convertTest(ListView<?> tblTasks) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadProfilesInCombo(ComboBox<Profile> profileCombobox) {
        profileCombobox.setItems(profiles);
        try {
            profiles.addAll(bll.getAllProfiles());
        } catch (BLLExeption ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.showAndWait();
        }
        
    }
    
}
