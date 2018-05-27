/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import shoreline_exam_2018.gui.model.profile.StructurePane;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.entity.StructEntityObject;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.LoggingHelper;
import shoreline_exam_2018.bll.Utilities.StructEntityUtils;
import shoreline_exam_2018.gui.model.profile.HeaderPane;

/**
 *
 * @author Asbamz
 */
public class ProfilesModel
{
    private BLLFacade bll; // BLL Manager to contact database.
    private TextField txtFieldProfileName; // Profile Name TextField.
    private TextField txtfieldSourcefile; // SourceFile TextField.
    private Button btnSource; // Source Button.
    private AnchorPane innerAnchor; // AnchorPane showing SplitPane.
    private SplitPane splitPane; // SplitPane with ScrollHeader and ScrollMain.
    private AnchorPane paneHeader; // Pane to show headers.
    private ScrollPane scrollMain; // ScrollPane for StructurePane.
    private Button btnBack; // Back Button.
    private Button btnSave; // Save Button.
    private ConvertModel cm; // Convert Model to add Profile to ComboBox.
    private Tab tabConvert; // Tab for convert view for switching tab on success.
    private StructurePane pg; // The Master Grid.
    private boolean isNext; // is rule view.

    /**
     * Takes Profile Edit GridPane as parameter. Uses BLLManager.
     */
    public ProfilesModel(TextField txtFieldProfileName, TextField txtfieldSourcefile, Button btnSource, AnchorPane innerAnchor, SplitPane splitPane, AnchorPane paneHeader, ScrollPane scrollMain, Button btnBack, Button btnSave)
    {
        this.bll = BLLManager.getInstance();

        this.txtFieldProfileName = txtFieldProfileName;
        this.txtfieldSourcefile = txtfieldSourcefile;
        this.btnSource = btnSource;
        this.innerAnchor = innerAnchor;
        this.splitPane = splitPane;
        this.paneHeader = paneHeader;
        this.scrollMain = scrollMain;
        this.btnBack = btnBack;
        this.btnBack.setVisible(false);
        this.btnSave = btnSave;
        this.btnSave.setText("Next");

        this.pg = new StructurePane(true);

        clearView();
    }

    /**
     * Gets file path with a FileChooser and update TextField. to show the path.
     */
    public void handleSource()
    {
        Path sourceFile = chooseFile();
        if (sourceFile != null)
        {
            txtfieldSourcefile.setText(sourceFile.toString());
            getDataFromFile(sourceFile);
        }
    }

    /**
     * Gets headers from file and creates the header Grid. Resets Master Grid.
     * @param path
     */
    private void getDataFromFile(Path path)
    {
        try
        {
            clearData();

            HashMap<String, Entry<Integer, String>> headersIndexAndExamples = bll.getHeadersAndExamplesFromFile(path);
            ObservableMap<String, Entry<Integer, String>> obsHeader = FXCollections.observableHashMap();
            obsHeader.putAll(headersIndexAndExamples);
            pg.addHashMap(obsHeader);
            HeaderPane hp = new HeaderPane(pg);

            AnchorPane.setTopAnchor(hp, 0.0);
            AnchorPane.setRightAnchor(hp, 0.0);
            AnchorPane.setBottomAnchor(hp, 0.0);
            AnchorPane.setLeftAnchor(hp, 0.0);

            paneHeader.getChildren().add(hp);
        }
        catch (BLLException ex)
        {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Could not get data from file", "The program was unable to get any data from " + path.toString() + ", Try another file");
        }
    }

    /**
     * Shows structurePane
     */
    public void handleBack()
    {
        isNext = false;
        splitPane.setVisible(true);
        btnSource.setVisible(true);
        btnBack.setVisible(false);
        btnSave.setText("Next");
        innerAnchor.getChildren().clear();
        innerAnchor.getChildren().add(splitPane);
    }

    /**
     * Shows ruleView and last saves the profile.
     */
    public void handleSaveStructure()
    {
        // Gets structure from Master Grid.
        List<StructEntityInterface> result = pg.getStructure();

        // Checks if empty.
        if (result == null || result.isEmpty())
        {
            AlertFactory.showInformation("No Structure", "There was not found any structure.");
            return;
        }

        // Checks if any entity is null/not filled out.
        if (StructEntityUtils.isAnyEntryNull(result))
        {
            AlertFactory.showInformation("Empty Collection", "Some of the collection in output structure is empty.");
            return;
        }

        // If name is not null nor empty.
        if (!txtFieldProfileName.getText().isEmpty() && txtFieldProfileName.getText() != null)
        {
            String profileName = txtFieldProfileName.getText();
            // Create Object with ProfileName and Structure.
            StructEntityObject seo = new StructEntityObject(profileName, result);

            try
            {
                // Add Structure to database.
                StructEntityObject structure = bll.addStructure(profileName, seo, bll.getcurrentUser().getId());

                bll.addLog(LogType.PROFILE, "Structure " + profileName + " was successfully added to the system.", bll.getcurrentUser());
                AlertFactory.showInformation("Success", "Structure has successfully been added to the system.");

                clearView();
            }
            catch (BLLException ex)
            {
                LoggingHelper.logException(ex);
                AlertFactory.showError("Data error", "An error happened trying to save the structure.\nERROR: " + ex.getMessage());
            }
        }
        else
        {
            AlertFactory.showInformation("Name missing", "The structure has to be named.");
        }
    }

    /**
     * Handles saves.
     */
    public void handleSave()
    {
        // Gets structure from Master Grid.
        List<StructEntityInterface> result = pg.getStructure();

        // Checks if empty.
        if (result == null || result.isEmpty())
        {
            AlertFactory.showInformation("No headers", "There was not found any headers in output structure.");
            return;
        }

        // Checks if any entity is null/not filled out.
        if (StructEntityUtils.isAnyEntryNull(result))
        {
            AlertFactory.showInformation("Empty Collection", "Some of the Collection in output structure is empty.");
            return;
        }

        // If name is not null nor empty.
        if (!txtFieldProfileName.getText().isEmpty() && txtFieldProfileName.getText() != null)
        {
            if (!isNext)
            {
                isNext = true;
                splitPane.setVisible(false);
                btnSource.setVisible(false);
                btnBack.setVisible(true);
                btnSave.setText("Save");
                ScrollPane sp = new ScrollPane();
                AnchorPane.setTopAnchor(sp, 0.0);
                AnchorPane.setRightAnchor(sp, 0.0);
                AnchorPane.setBottomAnchor(sp, 0.0);
                AnchorPane.setLeftAnchor(sp, 0.0);
                sp.setContent(pg.createRuleView());
                innerAnchor.getChildren().add(sp);
            }
            else
            {
                String profileName = txtFieldProfileName.getText();
                // Create Object with ProfileName and Structure.
                StructEntityObject seo = new StructEntityObject(profileName, result);

                try
                {
                    // Add Profile to database.
                    Profile profile = bll.addProfile(profileName, seo, 0);

                    // If Convert Model is set. Add it to the ComboBox.
                    if (cm != null)
                    {
                        cm.addProfile(profile);
                    }

                    bll.addLog(LogType.PROFILE, "Profile " + profileName + " was successfully added to the system.", bll.getcurrentUser());
                    AlertFactory.showInformation("Success", "Profile has successfully been added to the system.");

                    clearView();

                    // If the Tab to Convert is set. Changes to the tab.
                    if (tabConvert != null)
                    {
                        tabConvert.getTabPane().getSelectionModel().select(tabConvert);
                    }
                }
                catch (BLLException ex)
                {
                    LoggingHelper.logException(ex);
                    AlertFactory.showError("Data error", "An error happened trying to save the profile.\nERROR: " + ex.getMessage());
                }
            }
        }
        else
        {
            AlertFactory.showInformation("Name missing", "The profile has to be named.");
        }
    }

    /**
     * Clear all data for profile.
     * @param size
     */
    private void clearData()
    {
        scrollMain.setContent(pg);
        loadStructure();
        paneHeader.getChildren().clear();
    }

    /**
     * Clear view for information.
     */
    private void clearView()
    {
        clearData();
        txtfieldSourcefile.setText("");
        txtFieldProfileName.setText("");
        handleBack();
    }

    /**
     * Opens a file chooser and returns chosen path.
     */
    private Path chooseFile()
    {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Supported Files", "*.xlsx", "*.csv");
        FileChooser.ExtensionFilter xlsxfilter = new FileChooser.ExtensionFilter("XLSX Files", "*.xlsx");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().addAll(filter, xlsxfilter, csvFilter);
        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        fc.setInitialDirectory(dir);
        fc.setTitle("Choose a source file.");

        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null)
        {
            return Paths.get(selectedFile.toURI());
        }
        return null;
    }

    /**
     * Load default structure.
     */
    private void loadStructure()
    {
        List<StructEntityObject> structures;
        try
        {
            // Get all structures.
            structures = bll.getAllStructures();
            if (structures != null)
            {
                if (!structures.isEmpty())
                {
                    // Take the latest.
                    StructEntityObject newest = structures.get(structures.size() - 1);
                    pg.loadStructure(newest.getCollection());
                }
            }
        }
        catch (BLLException ex)
        {
            AlertFactory.showWarning("Loading structure", "Was not able to load default structure.");
            LoggingHelper.logException(ex);
        }
    }

    /**
     * Adds convert model and tab.
     * @param cm
     * @param tabConvert
     */
    public void addSharedInfo(ConvertModel cm, Tab tabConvert)
    {
        this.tabConvert = tabConvert;
        this.cm = cm;
    }
}
