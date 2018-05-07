/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_exam_2018.bll.ConversionJob;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.BLLExeption;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author alexl
 */
public class ConvertModel {

    private BLLFacade bll;

    ObservableList<Profile> profiles;

    private List<ConversionJob> tblTasks;
    private ObservableList<ConversionJob> olTasks;

    private File selectedFile = null;
    private File outputFile = null;
    private Profile selectedProfile;
    private String taskName;

    public ConvertModel() {
        bll = new BLLManager();
        profiles = FXCollections.observableArrayList();
        olTasks = FXCollections.observableArrayList();
    }

    /**
     * Sets array and observable lists for future use to place tasks into view.
     *
     * @param tblTasks
     */
    public void prepareTasks() {
        tblTasks = new ArrayList<>();
    }

    /**
     * Opens a file chooser and sets a File object to be the selected file.
     */
    public String chooseFile() {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XLSX Files", "*.xlsx");
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(filter);
        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        fc.setInitialDirectory(dir);
        fc.setTitle("Load a file");

        selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {

            return selectedFile.toString();
        }
        return "";
    }

    /**
     * Populates the given combo box with profiles and sets proper naming for
     * these profiles so it looks nicely
     */
    public void loadProfilesInCombo(ComboBox<Profile> profileCombobox) {
        profileCombobox.setItems(profiles);
        try {
            profiles.addAll(bll.getAllProfiles());
        } catch (BLLExeption ex) {
            AlertFactory.showWarning("Could not load Profiles", "The program was unable to load Profiles into the Combo Box. Please restart the program if you want to convert.");
        }

        profileCombobox.setCellFactory((ListView<Profile> param) -> new profileListCell());

        profileCombobox.setButtonCell(new profileListCell());
    }

    /**
     * Add profile to profiles list.
     */
    public void addProfile(Profile profile) {
        profiles.add(profile);
    }

    /**
     * Chooses a destination for the outputfile
     *
     * @return a string with the path
     */
    public String chooseDestination() {
        DirectoryChooser direcChosser = new DirectoryChooser();

        direcChosser.setTitle("Chosse output directory");

        File selectedDirectory = direcChosser.showDialog(new Stage());

        TextInputDialog namedialog = new TextInputDialog();
        namedialog.setTitle("Outputfile name");
        namedialog.setHeaderText("Please write the wanted name for the output file:");

        String fileName;

        Optional<String> result = namedialog.showAndWait();
        if (result.isPresent()) {
            fileName = result.get();
            if (!fileName.trim().isEmpty()) {

                String filePath = selectedDirectory.getAbsolutePath() + File.separator + fileName + ".json";

                outputFile = new File(filePath);
                return filePath;
            }
        }
        return null;
    }


    /**
     * Start a conversion with the given input and output files
     * @param currentProfile the profile to use for conversion
     * @return the conversion job that is started
     */
    public ConversionJob StartConversion(Profile currentProfile, ListView<ConversionJob> listJobs) {
        ConversionJob startConversion = null;
        
        if (selectedFile != null && outputFile != null) {
            String name;
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] split = selectedFile.getAbsolutePath().split(pattern);

            name = split[split.length - 1];

            try {
                startConversion = bll.startConversion(name, selectedFile.toPath(), outputFile.toPath(), currentProfile, listJobs);
            } catch (BLLExeption ex) {
                AlertFactory.showError("Could not convert.", "Error: " + ex.getMessage());
            }
            
            selectedFile = null;
            outputFile = null;
            return startConversion;
        }
        else{
            AlertFactory.showError("No input file", "Please select an input and output file");
            return null;
        }
        
    }
}

//Class for showing the right name in the comboboxs list and button
class profileListCell extends ListCell<Profile> {

    @Override
    protected void updateItem(Profile item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
        } else {
            setText(item.getName());
        }
    }
}
