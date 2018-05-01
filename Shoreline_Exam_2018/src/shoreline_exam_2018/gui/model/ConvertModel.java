/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.ConversionTask;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.gui.model.ConvertModel;

/**
 *
 * @author alexl
 */
public class ConvertModel {
    
    private BLLFacade bll;
    
    private List<ConversionTask> tblTasks;
    private ObservableList<ConversionTask> olTasks;
    
    public ConvertModel() {
        bll = new BLLManager();
    }
    
    /**
     * Sets array and observable lists for future use to place tasks into view.
     *
     * @param tblTasks
     */
    public void prepareTasks() {
        tblTasks = new ArrayList<>();
        olTasks = FXCollections.observableArrayList();
    }

    public void chooseFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void convertTest(ListView<?> tblTasks) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadProfilesInCombo(ComboBox<Profile> profileCombobox) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
