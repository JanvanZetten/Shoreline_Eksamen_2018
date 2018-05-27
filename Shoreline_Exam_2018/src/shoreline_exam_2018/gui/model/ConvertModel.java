package shoreline_exam_2018.gui.model;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxSingle;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxMulti;
import shoreline_exam_2018.bll.LoggingHelper;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxInterface;
import shoreline_exam_2018.gui.model.conversion.BoxMaker;

/**
 *
 * @author alexl
 */
public class ConvertModel {

    private final BLLFacade bll;
    private final BoxMaker bMaker;

    private ObservableList<Profile> profiles;

    private File selectedFile = null;
    private File outputFile;

    // Is only used to check if the default output path has changed.
    private File defaultOutputFileChecker;

    private TextField outputField;

    public ConvertModel() {
        bll = BLLManager.getInstance();
        profiles = FXCollections.observableArrayList();
        bMaker = new BoxMaker();
    }

    /**
     * Opens a file chooser and sets a File object to be the selected file.
     *
     * @return
     */
    public String chooseFile() {

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Supported Files", "*.xlsx", "*.csv");
        FileChooser.ExtensionFilter xlsxfilter = new FileChooser.ExtensionFilter("XLSX Files", "*.xlsx");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().addAll(filter, xlsxfilter, csvFilter);

        String[] directory = bll.getDefaultDirectories();
        String currentDir = directory[1];

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
     * Opens a DirectoryChooser to select a folder to convert from.
     *
     * @return the input path as a string.
     */
    public String chooseDirectory() {
        DirectoryChooser direcChosser = new DirectoryChooser();

        direcChosser.setTitle("Chosse input directory");

        String[] directory = bll.getDefaultDirectories();
        String currentDir = directory[1];

        File dir = new File(currentDir);
        direcChosser.setInitialDirectory(dir);

        selectedFile = direcChosser.showDialog(new Stage());

        if (selectedFile != null) {
            return selectedFile.toString();
        }
        return "";
    }

    /**
     * Chooses a destination for the outputfile
     *
     * @return a string with the path
     */
    public String chooseDestination() {
        DirectoryChooser dc = new DirectoryChooser();

        dc.setTitle("Chosse output directory");

        String[] directory = bll.getDefaultDirectories();
        String currentDir = directory[0];

        File dir = new File(currentDir);
        dc.setInitialDirectory(dir);

        outputFile = dc.showDialog(null);

        if (outputFile != null) {
            return outputFile.toString();
        }

        return "";
    }

    /**
     * Populates the given combo box with profiles and sets proper naming for
     * these profiles so it looks nicely
     *
     * @param profileCombobox
     */
    public void loadProfilesInCombo(ComboBox<Profile> profileCombobox) {
            profileCombobox.setItems(profiles);
            try {
                profiles.addAll(bll.getAllProfiles());
            } catch (BLLException ex) {
                LoggingHelper.logException(ex);
                AlertFactory.showWarning("Could not load Profiles", "The program was unable to load Profiles into the Combo Box. Please restart the program if you want to convert.");
            }
            profileCombobox.setCellFactory((ListView<Profile> param) -> new profileListCell());
            profileCombobox.setButtonCell(new profileListCell());
            
            
            for (Profile profile : profileCombobox.getItems()) {
                if (bll.getDefaultProfile() == profile.getId()) {
                    profileCombobox.getSelectionModel().select(profile);
                    System.out.println(profile.getId());
                }
                else {
                    profileCombobox.getSelectionModel().selectFirst();
                }
            }
    }

    /**
     * Add profile to profiles list.
     *
     * @param profile
     */
    public void addProfile(Profile profile) {
        profiles.add(profile);
    }

    /**
     * Start a conversion with the given input and output files
     *
     * @param currentProfile the profile to use for conversion
     * @param listJobs
     * @return the conversion job that is started
     */
    public ConversionBoxSingle StartSingleConversion(Profile currentProfile, ListView<ConversionBoxInterface> listJobs) {
        return bMaker.createSingleBox(currentProfile, listJobs, selectedFile, outputFile);
    }

    /**
     * Creates a new ConversionBoxMulti and adds all files in a folder into this
     * object as ConversionBoxSingle. Also checks for file-types.
     *
     * @param currentProfile
     * @param listMain
     * @return
     */
    public ConversionBoxMulti StartMultiConversion(Profile currentProfile, ListView<ConversionBoxInterface> listMain) {
        return bMaker.createMultiBox(currentProfile, listMain, selectedFile, outputFile);
    }

    /**
     * Sets the default directory on start up.
     *
     * @param outputField
     */
    public void setDefaultOutputDir(TextField outputField) {
        this.outputField = outputField;
        this.outputField.setText(bll.getDefaultDirectories()[0]);
        outputFile = new File(this.outputField.getText());
        defaultOutputFileChecker = new File(this.outputField.getText());
    }

    /**
     * Checks if the default output has changed and sets it if it has.
     */
    public void haveDefaultsChanged() {
        if (!defaultOutputFileChecker.getPath().equals(bll.getDefaultDirectories()[0])) {
            outputField.setText(bll.getDefaultDirectories()[0]);
            outputFile = new File(outputField.getText());
            defaultOutputFileChecker = new File(outputField.getText());
        }
        
    }
}

//Class for showing the right name in the comboboxes list and button
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
