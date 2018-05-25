package shoreline_exam_2018.gui.model;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.LoggingHelper;

/**
 *
 * @author alexl
 */
public class SettingsModel {

    private BLLFacade bll;
    private TextField txtfieldInputDir;
    private TextField txtfieldOutputDir;
    private ObservableList<Profile> profiles;

    public SettingsModel() {
        bll = BLLManager.getInstance();
        profiles = FXCollections.observableArrayList();
    }

    /**
     * Retrieves the properties file and reads it on start-up. If no properties
     * exist, it will create the properties as default.
     */
    public void getProperties() {
        txtfieldInputDir.setText(bll.getDefaultDirectories()[1]);
        txtfieldOutputDir.setText(bll.getDefaultDirectories()[0]);
    }

    /**
     * Sets the TextField references from the controller.
     * @param txtfieldInputDir
     * @param txtfieldOutputDir 
     */
    public void setTextFields(TextField txtfieldInputDir, TextField txtfieldOutputDir) {
        this.txtfieldInputDir = txtfieldInputDir;
        this.txtfieldOutputDir = txtfieldOutputDir;
    }

    /**
     * Sets a new default directory for either Input or Output.
     * directory[0] = dirType. directory [1] = path to the directory.
     * @param dirType     = The type of the directory (dirInput OR dirOutput)
     * @param txtfieldDir = The directory TextField (txtfieldInputDir OR txtfieldOutputDir)
     */
    public void newDefaultDir(String dirType, TextField txtfieldDir) {
        DirectoryChooser dc = new DirectoryChooser();
        String[] directory = new String[2];
        String currentDir = System.getProperty("user.dir") + File.separator;
        File dir = new File(currentDir);
        
        directory[0] = dirType;
        dc.setInitialDirectory(dir);
        dc.setTitle("Choose a directory");

        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            try {
                directory[1] = selectedFile.toString();
                txtfieldDir.setText(directory[1]);
                bll.updateDefaultDirectory(directory, txtfieldInputDir.getText(), txtfieldOutputDir.getText());
            } catch (BLLException ex) {
                AlertFactory.showError("The properties file could not be updated", "Error: " + ex.getMessage());
            }
        }
    }

    /**
     *
     * @param defaultProfile
     * @param newDefProfile
     */
    public void newDefaultProfile(String defaultProfile, Profile newDefProfile) {
        try {
            String[] profile = new String[2];
            profile[0] = defaultProfile;
            profile[1] = newDefProfile.getId() + "";
            bll.updateDefaultProfile(profile);
        } catch (BLLException ex) {
            AlertFactory.showError("The properties file could not be updated", "Error: " + ex.getMessage());
        }
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
        catch (BLLException ex)
        {
            LoggingHelper.logException(ex);
            AlertFactory.showWarning("Could not load Profiles", "The program was unable to load Profiles into the Combo Box. Please restart the program if you want to convert.");
        }

        profileCombobox.setCellFactory((ListView<Profile> param) -> {
            return new profileListCell();
        });

        profileCombobox.setButtonCell(new profileListCell());
    }
}
