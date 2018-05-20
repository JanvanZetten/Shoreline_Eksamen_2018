/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.LoggingHelper;
import shoreline_exam_2018.bll.Utilities.StructEntityUtils;

/**
 *
 * @author Asbamz
 */
public class ProfilesModel
{
    private static final String STYLESHEET = "shoreline_exam_2018/gui/view/css/style.css"; // Root CSS

    private BLLFacade bll; // BLL Manager to contact database.
    private TextField txtFieldProfileName; // Profile Name TextField.
    private TextField txtfieldSourcefile; // SourceFile TextField.
    private Button btnSource; // Source Button.
    private AnchorPane innerAnchor; // AnchorPane showing SplitPane.
    private SplitPane splitPane; // SplitPane with ScrollHeader and ScrollMain.
    private ScrollPane scrollHeader; // ScrollPane for gridDrag.
    private GridPane gridDrag; // Grid Pane which contains column headers from input file.
    private ScrollPane scrollMain; // ScrollPane for ProfileGrid.
    private Button btnBack; // Back Button.
    private Button btnSave; // Save Button.
    private HashMap<String, Entry<Integer, String>> headersIndexAndExamples; // Mapping column headers to their index and example.
    private int headerRowCount; // gridDrag row count.
    private ConvertModel cm; // Convert Model to add Profile to ComboBox.
    private Tab tabConvert; // Tab for convert view for switching tab on success.
    private ProfileGrid pg; // The Master Grid.
    private boolean isNext;

    /**
     * Takes Profile Edit GridPane as parameter. Uses BLLManager.
     */
    public ProfilesModel(TextField txtFieldProfileName, TextField txtfieldSourcefile, Button btnSource, AnchorPane innerAnchor, SplitPane splitPane, ScrollPane scrollHeader, GridPane gridDrag, ScrollPane scrollMain, Button btnBack, Button btnSave)
    {
        this.bll = BLLManager.getInstance();

        this.txtFieldProfileName = txtFieldProfileName;
        this.txtfieldSourcefile = txtfieldSourcefile;
        this.btnSource = btnSource;
        this.innerAnchor = innerAnchor;
        this.splitPane = splitPane;
        this.scrollHeader = scrollHeader;
        this.gridDrag = gridDrag;
        this.scrollMain = scrollMain;
        this.btnBack = btnBack;
        this.btnBack.setVisible(false);
        this.btnSave = btnSave;
        this.btnSave.setText("Next");

        this.pg = new ProfileGrid(true);

        // Makes ScrollPaneHeader accept drag and drop copying.
        this.scrollHeader.setOnDragOver(e ->
        {
            e.acceptTransferModes(TransferMode.COPY);
        });
        // So that the element dragged to a row can be dragged back and destroyed.
        this.scrollHeader.setOnDragDropped(destroyHeader());

        this.isNext = false;
        clearData();
    }

    /**
     * Remove index for row. Drag and Drop event Simple sets drop completed if
     * the drop is not empty.
     * @return
     */
    private EventHandler destroyHeader()
    {
        EventHandler eh = new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent e)
            {
                Dragboard db = e.getDragboard();
                if (db.hasString())
                {
                    e.setDropCompleted(true);
                }
                else
                {
                    e.setDropCompleted(false);
                }
            }
        };
        return eh;
    }

    /**
     * Add headers to scrollHeader grid pane / gridDrag
     * @param header
     */
    private void addInputHeadersAndExamples(int index, String header, String example)
    {
        // Makes TextField to hold header name.
        TextField tfHeader = new TextField(header);
        tfHeader.setId("DRAGANDDROP");
        tfHeader.setEditable(false);

        // Make drag and drop copying compatible.
        tfHeader.setOnDragDetected(e ->
        {
            Dragboard db = tfHeader.startDragAndDrop(TransferMode.COPY);
            db.setDragView(getDragAndDropScene(new TextField(header)).snapshot(null), e.getX(), e.getY());
            ClipboardContent cc = new ClipboardContent();
            cc.putString(header);
            db.setContent(cc);
        });

        // Set constraints and add to GridPane.
        GridPane.setConstraints(tfHeader, 0, headerRowCount);
        gridDrag.getChildren().addAll(tfHeader);
        headerRowCount++;
    }

    /**
     * Gets headers from file and creates the header Grid. Resets Master Grid.
     * @param path
     */
    private void getDataFromFile(Path path)
    {
        try
        {
            headersIndexAndExamples = bll.getHeadersAndExamplesFromFile(path);
            for (String string : headersIndexAndExamples.keySet())
            {
                addInputHeadersAndExamples(headersIndexAndExamples.get(string).getKey(), string, headersIndexAndExamples.get(string).getValue());
            }
            pg.addHashMap(headersIndexAndExamples);
            clearData();
        }
        catch (BLLException ex)
        {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Could not get data from file", "The program was unable to get any data from " + path.toString() + ", Try another file");
        }
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
            /*
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
             */
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
        headersIndexAndExamples = new HashMap<>();
        headerRowCount = 0;
        scrollMain.setContent(pg);
        loadStructure();
    }

    /**
     * Clear view for information.
     */
    private void clearView()
    {
        clearData();
        txtfieldSourcefile.setText("");
        txtFieldProfileName.setText("");
        gridDrag.getChildren().clear();
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
     * Adds convert model and tab.
     * @param cm
     * @param tabConvert
     */
    public void addSharedInfo(ConvertModel cm, Tab tabConvert)
    {
        this.tabConvert = tabConvert;
        this.cm = cm;
    }

    /**
     * Getting the right style for drag and drop
     * @param node
     * @return
     */
    private Scene getDragAndDropScene(Parent parent)
    {
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(STYLESHEET);
        return scene;
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
}
