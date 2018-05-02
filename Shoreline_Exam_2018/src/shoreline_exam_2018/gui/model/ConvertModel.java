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
import shoreline_exam_2018.bll.ConversionTask;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.BLLExeption;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author alexl
 */
public class ConvertModel
{

    private BLLFacade bll;

    ObservableList<Profile> profiles;

    private List<ConversionTask> tblTasks;
    private ObservableList<ConversionTask> olTasks;

    private File selectedFile;
    private Path selectedFilePath;
    private Profile selectedProfile;
    private String taskName;

    public ConvertModel()
    {
        bll = new BLLManager();
        profiles = FXCollections.observableArrayList();
    }

    /**
     * Sets array and observable lists for future use to place tasks into view.
     *
     * @param tblTasks
     */
    public void prepareTasks()
    {
        tblTasks = new ArrayList<>();
        olTasks = FXCollections.observableArrayList();
    }

    /**
     * Opens a file chooser and sets a File object to be the selected file.
     */
    public String chooseFile()
    {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XLSX Files", "*.xlsx");
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(filter);
        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        fc.setInitialDirectory(dir);
        fc.setTitle("Load a file");

        selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null)
        {
            selectedFilePath = Paths.get(selectedFile.toURI());
            return selectedFilePath.toString();
        }
        return "";
    }

    /**
     * Test method that handles conversion and setting of tasks.
     *
     * @param tblTasks
     */
    public void convertTest(FlowPane list)
    {
        tblTasks.add(bll.setConversionFilePath(taskName, selectedFilePath, selectedProfile));
        olTasks.addAll(this.tblTasks);
        list.getChildren().addAll(olTasks);

        // Clears the list so no duplicates are added
        tblTasks.clear();
        olTasks.clear();
    }

    /**
     * Populates the given combo box with profiles and sets proper naming for
     * these profiles so it looks nicely
     * @param profileCombobox
     */
    public void loadProfilesInCombo(ComboBox<Profile> profileCombobox)
    {
        profileCombobox.setItems(profiles);
        try
        {
            profiles.addAll(bll.getAllProfiles());
        }
        catch (BLLExeption ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
            alert.showAndWait();
        }

        profileCombobox.setCellFactory((ListView<Profile> param) -> new profileListCell());

        profileCombobox.setButtonCell(new profileListCell());
    }

    /**
     * Add profile to profiles list.
     * @param profile
     */
    public void addProfile(Profile profile)
    {
        profiles.add(profile);
    }
}

//Class for showing the right name in the comboboxs list and button
class profileListCell extends ListCell<Profile>
{
    @Override
    protected void updateItem(Profile item, boolean empty)
    {
        super.updateItem(item, empty);
        if (item == null || empty)
        {
            setGraphic(null);
        }
        else
        {
            setText(item.getName());
        }
    }
}
