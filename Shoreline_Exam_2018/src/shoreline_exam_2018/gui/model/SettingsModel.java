package shoreline_exam_2018.gui.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author alexl
 */
public class SettingsModel {

    private BLLFacade bll;
    private TextField txtfieldInputDir;
    private TextField txtfieldOutputDir;

    public SettingsModel() {
        bll = BLLManager.getInstance();
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
        directory[0] = dirType;

        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        dc.setInitialDirectory(dir);
        dc.setTitle("Choose a directory");

        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            try {
                directory[1] = selectedFile.toString();
                txtfieldDir.setText(directory[1]);
                bll.updateDefaultDirectory(directory, txtfieldInputDir.getText(), txtfieldOutputDir.getText());
            } catch (BLLException ex) {
                AlertFactory.showError("The config could not be updated", "Error: " + ex.getMessage());
            }
        }
    }
}
