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
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.bll.BLLExeption;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.Utilities.StructEntityUtils;

/**
 *
 * @author Asbamz
 */
public class ProfilesModel
{
    private static final String STYLESHEET = "shoreline_exam_2018/gui/view/css/style.css"; // Root CSS

    private BLLManager bll; // BLL Manager to contact database.
    private GridPane gridDrag; // Grid Pane which contains column headers from input file.
    private ScrollPane scrollHeader; // ScrollPane for gridDrag.
    private ScrollPane scrollMain; // ScrollPane for ProfileGrid.
    private HashMap<String, Integer> headerMap; // Mapping column headers to their index.
    private int headerRowCount; // gridDrag row count.
    private StringProperty tp; // Profile Name StringProperty.
    private TextField txtfieldSourcefile; // SourceFile TextField.
    private ConvertModel cm; // Convert Model to add Profile to ComboBox.
    private Tab tabConvert; // Tab for convert view for switching tab on success.
    private ProfileGrid pg; // The Master Grid.

    /**
     * Takes Profile Edit GridPane as parameter. Uses BLLManager.
     */
    public ProfilesModel(GridPane gridDrag, ScrollPane scrollHeader, ScrollPane scrollMain, TextField source, TextField tf)
    {
        this.bll = new BLLManager();
        this.gridDrag = gridDrag;
        this.scrollHeader = scrollHeader;
        this.scrollMain = scrollMain;

        // Makes ScrollPaneHeader accept drag and drop copying.
        this.scrollHeader.setOnDragOver(e ->
        {
            e.acceptTransferModes(TransferMode.COPY);
        });
        // So that the element dragged to a row can be dragged back and destroyed.
        this.scrollHeader.setOnDragDropped(destroyHeader());

        this.txtfieldSourcefile = source;
        tp = tf.textProperty();
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
    private void addInputHeaders(String header)
    {
        // Makes TextField to hold header name.
        TextField tfHeader = new TextField(header);
        tfHeader.setEditable(false);

        // Add to map.
        headerMap.put(header, headerRowCount);

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
            List<String> headers = bll.getHeadersFromFile(path);
            clearData();
            for (String header : headers)
            {
                addInputHeaders(header);
            }
            pg.addHeaderHashMap(headerMap);
        }
        catch (BLLExeption ex)
        {
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
            AlertFactory.showInformation("Empty headers", "Some of the headers in output structure is empty.");
            return;
        }

        // If name is not null nor empty.
        if (tp.isNotEmpty().get() && tp.isNotNull().get())
        {
            // Create Object with ProfileName and Structure.
            StructEntityObject seo = new StructEntityObject(tp.get(), result);

            try
            {
                // Add Profile to database.
                Profile profile = bll.addProfile(tp.get(), seo, 0);

                // If Convert Model is set. Add it to the ComboBox.
                if (cm != null)
                {
                    cm.addProfile(profile);
                }

                AlertFactory.showInformation("Success", "Profile has succesfully been added to the system.");

                clearView();

                // If the Tab to Convert is set. Changes to the tab.
                if (tabConvert != null)
                {
                    tabConvert.getTabPane().getSelectionModel().select(tabConvert);
                }
            }
            catch (BLLExeption ex)
            {
                AlertFactory.showError("Data error", "An error happened trying to save the profile.\nERROR: " + ex.getMessage());
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
        scrollMain.setContent(null);
        headerMap = new HashMap<>();
        headerRowCount = 0;
        pg = new ProfileGrid(true);
        scrollMain.setContent(pg);
    }

    /**
     * Clear view for information.
     */
    private void clearView()
    {
        clearData();
        txtfieldSourcefile.setText("");
        tp.set("");
        gridDrag.getChildren().clear();
        pg = new ProfileGrid(true);
        scrollMain.setContent(pg);
    }

    /**
     * Opens a file chooser and returns chosen path.
     */
    private Path chooseFile()
    {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XLSX Files", "*.xlsx");
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(filter);
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
}
