package shoreline_exam_2018.gui.model;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_exam_2018.bll.ConversionJobSingle;
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
import shoreline_exam_2018.bll.ConversionJobMulti;
import shoreline_exam_2018.bll.ConversionJobs;
import shoreline_exam_2018.bll.LoggingHelper;

/**
 *
 * @author alexl
 */
public class ConvertModel
{

    private BLLFacade bll;

    ObservableList<Profile> profiles;

    private List<ConversionJobSingle> tblTasks;
    private ObservableList<ConversionJobSingle> olTasks;

    private File selectedFile = null;
    private File outputFile;
    
    // Is only used to check if the default output path has changed.
    private File defaultOutputFileChecker;
    
    private TextField outputField;

    public ConvertModel()
    {
        bll = BLLManager.getInstance();
        profiles = FXCollections.observableArrayList();
        olTasks = FXCollections.observableArrayList();
    }

    /**
     * Sets array and observable lists for future use to place tasks into view.
     *
     * @param tblTasks
     */
    public void prepareTasks()
    {
        tblTasks = new ArrayList<>();
    }

    /**
     * Opens a file chooser and sets a File object to be the selected file.
     */
    public String chooseFile()
    {

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

        if (selectedFile != null)
        {

            return selectedFile.toString();
        }
        return "";
    }

    /**
     * Chooses a destination for the outputfile
     *
     * @return a string with the path
     */
    public String chooseDestination()
    {
        DirectoryChooser dc = new DirectoryChooser();

        dc.setTitle("Chosse output directory");

        String[] directory = bll.getDefaultDirectories();
        String currentDir = directory[0];

        File dir = new File(currentDir);
        dc.setInitialDirectory(dir);

        outputFile = dc.showDialog(null);

        if (outputFile != null)
        {
            return outputFile.toString();
        }

        return "";
    }

    /**
     * Populates the given combo box with profiles and sets proper naming for
     * these profiles so it looks nicely
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

        profileCombobox.setCellFactory((ListView<Profile> param) -> new profileListCell());

        profileCombobox.setButtonCell(new profileListCell());
    }

    /**
     * Add profile to profiles list.
     */
    public void addProfile(Profile profile)
    {
        profiles.add(profile);
    }

    /**
     * Start a conversion with the given input and output files
     *
     * @param currentProfile the profile to use for conversion
     * @return the conversion job that is started
     */
    public ConversionJobSingle StartSingleConversion(Profile currentProfile, ListView<ConversionJobs> listJobs, String inputPath)
    {
        ConversionJobSingle startConversion = null;
        selectedFile = new File(inputPath);
        if (selectedFile != null && outputFile != null && currentProfile != null)
        {
            String name;
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] split = selectedFile.getAbsolutePath().split(pattern);

            name = split[split.length - 1];

            try
            {
                String output = outputFile.toPath() + File.separator + selectedFile.getName() + ".json";
                File outputFile = new File(output);
                startConversion = bll.startSingleConversion(name, selectedFile.toPath(), outputFile.toPath(), currentProfile, listJobs, null);
            }
            catch (BLLException ex)
            {
                LoggingHelper.logException(ex);
                AlertFactory.showError("Could not convert.", "Error: " + ex.getMessage());
            }

            selectedFile = null;
            return startConversion;
        }
        else
        {
            AlertFactory.showError("Missing fields", "Please select a profile and an input and output file.");
            return null;
        }
    }

    public ConversionJobMulti StartMultiConversion(Profile currentProfile, ListView<ConversionJobs> listJobs, String inputPath)
    {
        ConversionJobMulti startConversion = null;
        ArrayList<ConversionJobSingle> listConversions = new ArrayList<>();
        ListView<ConversionJobs> list = new ListView<>();
        selectedFile = new File(inputPath);

        if (selectedFile != null && outputFile != null && currentProfile != null)
        {
            File[] directory = selectedFile.listFiles();

            try
            {
                startConversion = bll.startMultiConversion(currentProfile, list);
            }
            catch (BLLException ex)
            {
                LoggingHelper.logException(ex);
                AlertFactory.showError("Could not convert.", "Error: " + ex.getMessage());
            }

            for (File file : directory)
            {

                String extension = "";

                int i = file.toString().lastIndexOf('.');

                extension = file.toString().substring(i + 1);
                extension.trim();

                if (extension.equals("xlsx") || extension.equals("csv"))
                {
                    try
                    {
                        String output = outputFile.toPath() + File.separator + file.getName() + ".json";
                        File outputFile = new File(output);
                        listConversions.add(bll.startSingleConversion(file.getName(), file.toPath(), outputFile.toPath(), currentProfile, list, startConversion));
                    }
                    catch (BLLException ex)
                    {
                        LoggingHelper.logException(ex);
                        AlertFactory.showError("Could not convert.", "Error: " + ex.getMessage());
                    }
                }
            }
            startConversion.setupPane(listConversions);
            selectedFile = null;
            return startConversion;
        }
        else
        {
            AlertFactory.showError("Missing fields", "Please select a profile and an input and output file.");
            return null;
        }
    }

    public String chooseDirectory()
    {
        DirectoryChooser direcChosser = new DirectoryChooser();

        direcChosser.setTitle("Chosse input directory");

        String[] directory = bll.getDefaultDirectories();
        String currentDir = directory[1];

        File dir = new File(currentDir);
        direcChosser.setInitialDirectory(dir);

        File selectedDirectory = direcChosser.showDialog(new Stage());

        if (selectedDirectory != null)
        {
            return selectedDirectory.getAbsolutePath();
        }
        return null;
    }

    public void addFolderListener(ConversionJobMulti StartConversion, String path)
    {
        try
        {
            bll.addDirectoryListener(StartConversion, Paths.get(path), outputFile.toPath());
        }
        catch (BLLException ex)
        {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Could not listen on directory", ex.getMessage());
        }

    }
    
    /**
     * Sets the default directory on start up.
     * @param outputField
     */
    public void setDefaultOutputDir(TextField outputField)
    {
        this.outputField = outputField;
        this.outputField.setText(bll.getDefaultDirectories()[0]);
        outputFile = new File(this.outputField.getText());
        defaultOutputFileChecker = new File(this.outputField.getText());
    }

    /**
     * Checks if the default output has changed and sets it if it has.
     */
    public void hasDefaultDirChanged() {
        if (!defaultOutputFileChecker.getPath().equals(bll.getDefaultDirectories()[0])) {
            outputField.setText(bll.getDefaultDirectories()[0]);
            outputFile = new File(outputField.getText());
            defaultOutputFileChecker = new File(outputField.getText());
        }
    }
}

//Class for showing the right name in the comboboxes list and button
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
