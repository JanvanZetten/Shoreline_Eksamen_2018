package shoreline_exam_2018.gui.model;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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
import shoreline_exam_2018.gui.model.conversion.ConversionBoxManager;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxInterface;

/**
 *
 * @author alexl
 */
public class ConvertModel
{

    private BLLFacade bll;
    private ConversionBoxManager cManager;

    ObservableList<Profile> profiles;

    private List<ConversionBoxSingle> tblTasks;
    private ObservableList<ConversionBoxSingle> olTasks;

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
        cManager = new ConversionBoxManager();
    }

    /**
     * Sets array and observable lists for future use to place tasks into view.
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
    public ConversionBoxSingle StartSingleConversion(Profile currentProfile, ListView<ConversionBoxInterface> listJobs, String inputPath)
    {
        ConversionBoxSingle conversionBoxSingle = null;
        selectedFile = new File(inputPath);
        if (selectedFile != null && outputFile != null && currentProfile != null)
        {
            String name;
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] split = selectedFile.getAbsolutePath().split(pattern);

            name = split[split.length - 1];
            
            try
            {
                String output = outputFile.toPath() + File.separator + removeExtension(name) + ".json";
                File outputFile = new File(output);
                conversionBoxSingle = cManager.newConversion(name, selectedFile.toPath(), outputFile.toPath(), currentProfile, listJobs, null);
            }
            catch (BLLException ex)
            {
                LoggingHelper.logException(ex);
                AlertFactory.showError("Could not convert.", "Error: " + ex.getMessage());
            }

            selectedFile = null;
            return conversionBoxSingle;
        }
        else
        {
            AlertFactory.showError("Missing fields", "Please select a profile and an input and output file.");
            return null;
        }
    }

    /**
     * Creates a new ConversionBoxMulti and adds all files in a folder into this
     * object as ConversionBoxSingle. Also checks for file-types.
     * @param currentProfile
     * @param listJobs
     * @param inputPath
     * @return 
     */
    public ConversionBoxMulti StartMultiConversion(Profile currentProfile, ListView<ConversionBoxInterface> listJobs, String inputPath)
    {
        ConversionBoxMulti conversionBoxMulti = null;
        ArrayList<ConversionBoxSingle> listConversions = new ArrayList<>();
        ListView<ConversionBoxInterface> list = new ListView<>();
        selectedFile = new File(inputPath);

        if (selectedFile != null && outputFile != null && currentProfile != null)
        {
            File[] directory = selectedFile.listFiles();

            conversionBoxMulti = cManager.newMultiConversion(currentProfile, list);

            for (File file : directory)
            {

                String extension = "";

                int i = file.toString().lastIndexOf('.');

                extension = file.toString().substring(i + 1);

                if (extension.equals("xlsx") || extension.equals("csv"))
                {
                    try
                    {
                        String output = outputFile.toPath() + File.separator + removeExtension(file.getName()) + ".json";
                        File outputFile = new File(output);
                        listConversions.add(cManager.newConversion(file.getName(), file.toPath(), outputFile.toPath(), currentProfile, list, conversionBoxMulti));
                    }
                    catch (BLLException ex)
                    {
                        LoggingHelper.logException(ex);
                        AlertFactory.showError("Could not convert.", "Error: " + ex.getMessage());
                    }
                }
            }
            conversionBoxMulti.setupPane(listConversions);
            addFolderListener(conversionBoxMulti, inputPath, cManager);
            selectedFile = null;
            return conversionBoxMulti;
        }
        else
        {
            AlertFactory.showError("Missing fields", "Please select a profile and an input and output file.");
            return null;
        }
    }

    /**
     * Opens a DirectoryChooser to select a folder to convert from.
     * @return the input path as a string.
     */
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

    /**
     * Creates a listener to a created ConversionBoxMulti. 
     * @param conversionBoxMulti = The ConversionBoxMulti to have the listener.
     * @param path               = The input path of the folder.
     * @param cManager           = The ConversionBoxManager that connects task and box creation.
     */
    public void addFolderListener(ConversionBoxMulti conversionBoxMulti, String path, ConversionBoxManager cManager)
    {
        try
        {
            bll.addDirectoryListener(conversionBoxMulti, Paths.get(path), outputFile.toPath(), cManager);
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
    
    /**
     * Removes the last . and the text after that
     * @param name
     * @return 
     */
    private String removeExtension(String name){
            String[] outputSplit = name.split("\\.");
            String outputName = outputSplit[0];
            for (int i = 1; i < (outputSplit.length - 1); i++) {
                outputName = outputName + "." +  outputSplit[i];
            }
            System.out.println(outputName);
            return outputName;
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
