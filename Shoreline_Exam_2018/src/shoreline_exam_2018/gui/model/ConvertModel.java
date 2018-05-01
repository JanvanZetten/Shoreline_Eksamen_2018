/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.ConversionTask;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import javafx.stage.FileChooser;
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

    private BLLFacade bll;

    ObservableList<Profile> profiles;

    private List<ConversionTask> tblTasks;
    private ObservableList<ConversionTask> olTasks;
    
    private File selectedFile;
    private Path selectedFilePath;
    private Profile selectedProfile;
    private String taskName;
    
    public ConvertModel() {
        bll = new BLLManager();
        profiles = FXCollections.observableArrayList();
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

    /**
     * Opens a file chooser and sets a File object to be the selected file.
     */
    public void chooseFile() {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XLSX Files", "*.xlsx");
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(filter);
        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        fc.setInitialDirectory(dir);
        fc.setTitle("Attach a file");

        selectedFile = fc.showOpenDialog(null);
        //CODE BELOW NEEDS TO BE CHANGED AS IT CURRENTLY STARTS A TASK THE INSTANT 
        //YOU SELECT A FILE. THERE NEEDS TO BE A BUTTON THAT SAYS "START" INSTEAD
        if (selectedFile != null) {
            selectedFilePath = Paths.get(selectedFile.toURI());
        }
    }

    /**
     * Test method that handles conversion and setting of tasks.
     * @param tblTasks 
     */
    public void convertTest(ListView<ConversionTask> tblTasks) {
        this.tblTasks.add(bll.setConversionFilePath(taskName, selectedFilePath, selectedProfile));
        olTasks.addAll(this.tblTasks);
        tblTasks.setItems(olTasks);
    }

    public void loadProfilesInCombo(ComboBox<Profile> profileCombobox) {
        profileCombobox.setItems(profiles);
        try {
            profiles.addAll(bll.getAllProfiles());
        } catch (BLLExeption ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.showAndWait();
        }

        profileCombobox.setCellFactory((ListView<Profile> l) -> new ListCell<Profile>() {
            @Override
            protected void updateItem(Profile item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    setText(item.getName());
                }
            }
        });

    }

}
