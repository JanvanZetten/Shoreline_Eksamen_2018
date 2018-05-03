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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.entry.StructEntityDate;
import shoreline_exam_2018.be.output.structure.entry.StructEntityDouble;
import shoreline_exam_2018.be.output.structure.entry.StructEntityInteger;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.be.output.structure.entry.StructEntityString;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;
import shoreline_exam_2018.bll.BLLExeption;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author Asbamz
 */
public class ProfilesModel
{
    private BLLManager bll;
    private GridPane gridPane;
    private int rowCount;
    private List<StructEntityInterface> structure;
    private StringProperty tp;
    private TextField txtfieldSourcefile;
    private ConvertModel cm;
    private Tab tabConvert;

    /**
     * Takes Profile Edit GridPane as parameter. Uses BLLManager
     * @param gridPane
     */
    public ProfilesModel(GridPane gridPane, TextField source, TextField tf)
    {
        this.bll = new BLLManager();
        this.gridPane = gridPane;
        this.txtfieldSourcefile = source;
        tp = tf.textProperty();
    }

    /**
     * Gets headers from file and creates the editor Grid.
     * @param path
     */
    private void getDataFromFile(Path path)
    {
        try
        {
            List<String> headers = bll.getHeadersFromFile(path);
            structure = new ArrayList<>();
            for (int i = 0; i < headers.size(); i++)
            {
                structure.add(null);
            }
            gridPane.getChildren().clear();
            rowCount = 1;
            addHeaderGridRow();
            for (String header : headers)
            {
                addGridRow(header);
            }
        }
        catch (BLLExeption ex)
        {
            Logger.getLogger(ProfilesModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Makes column headers in Editor Grid.
     */
    private void addHeaderGridRow()
    {
        Label lbl1 = new Label("Header");
        Label lbl2 = new Label("Example");
        Label lbl3 = new Label("Data Type");
        Label lbl4 = new Label("To Column");
        Label lbl5 = new Label("Include");
        GridPane.setConstraints(lbl1, 0, 0);
        GridPane.setConstraints(lbl2, 1, 0);
        GridPane.setConstraints(lbl3, 2, 0);
        GridPane.setConstraints(lbl4, 3, 0);
        GridPane.setConstraints(lbl5, 4, 0);
        gridPane.getChildren().addAll(lbl1, lbl2, lbl3, lbl4, lbl5);
    }

    /**
     * Makes a row in Editors Grid.
     * @param header
     */
    private void addGridRow(String header)
    {
        Label lbl = new Label(header);
        ComboBox<SimpleStructType> cmb = getDataTypeBox();
        TextField txtField = new TextField();
        CheckBox cb = new CheckBox();
        cmb.valueProperty().addListener(getDataChangeListener(rowCount - 1, cmb, txtField, cb));
        txtField.textProperty().addListener(getDataChangeListener(rowCount - 1, cmb, txtField, cb));
        cb.selectedProperty().addListener(getDataChangeListener(rowCount - 1, cmb, txtField, cb));
        GridPane.setConstraints(lbl, 0, rowCount);
        GridPane.setConstraints(cmb, 2, rowCount);
        GridPane.setConstraints(txtField, 3, rowCount);
        GridPane.setConstraints(cb, 4, rowCount);
        gridPane.getChildren().addAll(lbl, cmb, txtField, cb);
        rowCount++;
    }

    /**
     * Updates structure list to fit changed information.
     * @param index
     * @param cmb
     * @param tf
     * @param cb
     * @return
     */
    private ChangeListener getDataChangeListener(int index, ComboBox<SimpleStructType> cmb, TextField tf, CheckBox cb)
    {
        ChangeListener cl = new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                if (cb.isSelected())
                {
                    if (!newValue.equals(oldValue) && cmb.getValue() != null && !tf.getText().isEmpty())
                    {
                        switch (cmb.getValue())
                        {
                            case DATE:
                                structure.set(index, new StructEntityDate(tf.getText(), index));
                                return;
                            case DOUBLE:
                                structure.set(index, new StructEntityDouble(tf.getText(), index));
                                return;
                            case INTEGER:
                                structure.set(index, new StructEntityInteger(tf.getText(), index));
                                return;
                            case STRING:
                                structure.set(index, new StructEntityString(tf.getText(), index));
                                return;
                            default:
                                break;
                        }
                    }
                }

                structure.set(index, null);
            }
        };
        return cl;
    }

    /**
     * Returns combobox with all datatypes.
     * @return
     */
    private ComboBox<SimpleStructType> getDataTypeBox()
    {
        ComboBox<SimpleStructType> cb = new ComboBox();
        ObservableList<SimpleStructType> items = FXCollections.observableArrayList();
        for (SimpleStructType value : SimpleStructType.values())
        {
            items.add(value);
        }
        cb.setItems(items);
        return cb;
    }

    /**
     * Makes Editors Grid from a filechoosers chosen path and update textfield
     * to show the path.
     * @param tf
     */
    public void handleSource()
    {
        Path sourceFile = chooseFile();
        if (sourceFile != null)
        {
            txtfieldSourcefile.setText(sourceFile.toString());
            txtfieldSourcefile.positionCaret(txtfieldSourcefile.getText().length());
            getDataFromFile(sourceFile);
        }
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
     * Handles saves.
     */
    public void handleSave()
    {
        if (structure == null || structure.isEmpty())
        {
            AlertFactory.showInformation("No headers", "There was not found any headers.");
            return;
        }

        List<StructEntityInterface> result = new ArrayList<>();
        for (StructEntityInterface structEntityInterface : structure)
        {
            if (structEntityInterface != null)
            {
                result.add(structEntityInterface);
            }
        }
        if (!result.isEmpty())
        {
            if (tp.isNotEmpty().get() && tp.isNotNull().get())
            {
                StructEntityObject seo = new StructEntityObject(tp.get(), result);
                try
                {
                    Profile profile = bll.addProfile(tp.get(), seo, 0);

                    if (cm != null)
                    {
                        cm.addProfile(profile);
                    }

                    AlertFactory.showInformation("Success", "Profile has succesfully been added to the system.");

                    clearView();

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
        else
        {
            AlertFactory.showInformation("No Structure", "There is no structure. Check the wanted columns.");
        }
    }

    /**
     * Clear view for information.
     */
    private void clearView()
    {
        txtfieldSourcefile.setText("");
        tp.set("");
        gridPane.getChildren().clear();
    }

    /**
     * Adds model and tab.
     * @param cm
     * @param tabConvert
     */
    public void addSharedInfo(ConvertModel cm, Tab tabConvert)
    {
        this.tabConvert = tabConvert;
        this.cm = cm;
    }
}
