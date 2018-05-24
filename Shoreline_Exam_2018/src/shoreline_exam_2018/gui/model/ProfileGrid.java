/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import shoreline_exam_2018.be.output.rule.DateFormatRule;
import shoreline_exam_2018.be.output.rule.DefaultDateRule;
import shoreline_exam_2018.be.output.rule.DefaultDoubleRule;
import shoreline_exam_2018.be.output.rule.DefaultIntegerRule;
import shoreline_exam_2018.be.output.rule.DefaultStringRule;
import shoreline_exam_2018.be.output.structure.CollectionEntity;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.entity.StructEntityArray;
import shoreline_exam_2018.be.output.structure.entity.StructEntityDate;
import shoreline_exam_2018.be.output.structure.entity.StructEntityDouble;
import shoreline_exam_2018.be.output.structure.entity.StructEntityInteger;
import shoreline_exam_2018.be.output.structure.entity.StructEntityObject;
import shoreline_exam_2018.be.output.structure.entity.StructEntityString;
import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 *
 * @author Asbamz
 */
public class ProfileGrid extends GridPane
{
    private static final String STYLESHEET = "shoreline_exam_2018/gui/view/css/style.css"; // Root CSS
    private static final double DEFAULT_INDENT = 10.0; // Indent for collections.
    private static final double DEFAULT_GAP = 5.0;
    private final boolean IS_MASTER; // Is this the master grid
    private final double DEFAULT_BUTTON_SIZE = 60.0;
    private final double DEFAULT_COMBOBOX_SIZE = 120.0;
    private final double DEFAULT_LABEL_SIZE = 160.0;
    private final double DEFAULT_TEXTFIELD_SIZE = 160.0;
    private final double DEFAULT_CHECKBOX_SIZE = 30.0;
    private final double DEFAULT_RECTANGLE_WIDTH = 4.0;
    private final double INDENT; // Indent for collections.
    private final Paint[] COLOURS = new Paint[]
    {
        Color.web("737F8C"), Color.web("4986A8"), Color.web("2C546D"), Color.web("4D4D4D")
    }; // Group/Collection colours.

    private HashMap<String, Entry<Integer, String>> headersIndexAndExamples; // Mapping column headers from input file to their example.
    private HashMap<Integer, String> headersIndexToName; // Mapping column headers from input file to their example.
    private HashMap<Integer, ProfileGrid> collectionMap; // Mapping structure index to a ProfileGrid.
    private HashMap<Node, Integer> nodeIndexMap; // Mapping Nodes to structure index.
    private List<StructEntityInterface> structure; // The structure of made from grid elements.
    private ChangeListener masterListener; // Master ChangeListener to notify changes to collections owning this ProfileGrid.

    /**
     * Choose rather or not the ProfileGrid is master.
     * @param isMaster
     */
    public ProfileGrid(boolean isMaster)
    {
        this(isMaster, isMaster ? 0.0 : DEFAULT_INDENT);
    }

    /**
     * The master grid is the main grid.
     * @param isMaster
     * @param indent
     */
    public ProfileGrid(boolean isMaster, double indent)
    {
        super();
        IS_MASTER = isMaster;
        this.INDENT = indent;
        setupGridPane(this, IS_MASTER ? 5.0 : -0.1);
        clear();
    }

    /**
     * Make grid with masterListener. MasterListener to update collections.
     * @param isMaster
     * @param indent
     * @param masterListener
     */
    private ProfileGrid(boolean isMaster, double indent, ChangeListener masterListener)
    {
        this(isMaster, indent);
        this.masterListener = masterListener;
    }

    /**
     * Setup grid.
     * @return
     */
    private void setupGridPane(GridPane gridPane, double padding)
    {
        // Clear constraints and settings.
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Set gap between grid elements and padding around grid.
        gridPane.setHgap(DEFAULT_GAP);
        gridPane.setVgap(DEFAULT_GAP);
        gridPane.setPadding(new Insets(padding, padding, padding, padding));
        gridPane.setGridLinesVisible(false);
    }

    /**
     * Gets structure.
     * @return
     */
    public List<StructEntityInterface> getStructure()
    {
        return structure;
    }

    /**
     * Makes column headers in Editor Grid.
     */
    private void addHeader()
    {
        Label lbl1 = new Label("Header");
        Label lbl2 = new Label("Example");
        Label lbl3 = new Label("Data Type");
        Label lbl4 = new Label("To Column");
        GridPane.setConstraints(lbl1, 0, 0);
        GridPane.setConstraints(lbl2, 1, 0);
        GridPane.setConstraints(lbl3, 2, 0);
        GridPane.setConstraints(lbl4, 3, 0);

        // Set size.
        lbl1.setMinWidth(DEFAULT_LABEL_SIZE);
        lbl1.setPrefWidth(DEFAULT_LABEL_SIZE);
        lbl1.setMaxWidth(DEFAULT_LABEL_SIZE);
        lbl2.setMinWidth(DEFAULT_LABEL_SIZE);
        lbl2.setPrefWidth(DEFAULT_LABEL_SIZE);
        lbl2.setMaxWidth(DEFAULT_LABEL_SIZE);
        lbl3.setMinWidth(DEFAULT_LABEL_SIZE);
        lbl3.setPrefWidth(DEFAULT_LABEL_SIZE);
        lbl3.setMaxWidth(DEFAULT_LABEL_SIZE);
        lbl4.setMinWidth(DEFAULT_LABEL_SIZE);
        lbl4.setPrefWidth(DEFAULT_LABEL_SIZE);
        lbl4.setMaxWidth(DEFAULT_LABEL_SIZE);

        this.getChildren().addAll(lbl1, lbl2, lbl3, lbl4);
    }

    /**
     * Row to add simple or collection.
     */
    private void addRowAdder()
    {
        // ComboBox
        ObservableList<String> obsCmb = FXCollections.observableArrayList();
        obsCmb.add("Simple");
        obsCmb.add("Collection");
        ComboBox<String> cmbType = new ComboBox(obsCmb);
        cmbType.getSelectionModel().selectFirst();
        Rectangle rect = getRectangle(DEFAULT_RECTANGLE_WIDTH, cmbType.heightProperty());

        // Button for adding new row.
        Button btn = new Button("Add");

        // On action add the type of row chosen in combobox.
        btn.setOnAction((event) ->
        {
            String selected = cmbType.getSelectionModel().getSelectedItem();
            if (selected.equalsIgnoreCase("Simple"))
            {
                addSimpleRow();
            }
            else
            {
                addCollectionRow();
            }
        });

        GridPane gp = new GridPane();
        GridPane cgp = new GridPane();
        gp.setHgap(5.0);
        gp.setVgap(5.0);

        // Add listener to check if new rows are added. If so the row for the combobox and button is changed to the last one.
        this.getChildren().addListener(new ListChangeListener<Node>()
        {

            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> c)
            {
                while (c.next())
                {
                    if (c.wasAdded())
                    {
                        int row = getRowCount();
                        if (row > -1)
                        {
                            GridPane.setConstraints(gp, 0, row);
                        }
                        GridPane.setConstraints(gp, 0, row);
                        break;
                    }
                    else if (c.wasRemoved())
                    {
                        int row = getRowCount();
                        if (row > 0)
                        {
                            GridPane.setConstraints(gp, 0, row - 1);
                        }
                        break;
                    }
                }
            }
        });

        // Set size.
        btn.setMinWidth(DEFAULT_BUTTON_SIZE);
        btn.setPrefWidth(DEFAULT_BUTTON_SIZE);
        btn.setMaxWidth(DEFAULT_BUTTON_SIZE);
        cmbType.setMinWidth(DEFAULT_COMBOBOX_SIZE);
        cmbType.setPrefWidth(DEFAULT_COMBOBOX_SIZE);
        cmbType.setMaxWidth(DEFAULT_COMBOBOX_SIZE);

        GridPane.setHalignment(rect, HPos.LEFT);
        GridPane.setHalignment(btn, HPos.LEFT);
        GridPane.setHalignment(cmbType, HPos.LEFT);

        GridPane.setConstraints(rect, 0, 0);
        GridPane.setConstraints(cmbType, 1, 0);
        GridPane.setConstraints(cgp, 0, 0);
        GridPane.setConstraints(btn, 1, 0);

        cgp.getChildren().addAll(rect, cmbType);
        gp.getChildren().addAll(cgp, btn);

        // Set margin for first item and adds them to the grid.
        GridPane.setMargin(cgp, new Insets(0.0, 0.0, 0.0, INDENT));
        this.getChildren().addAll(gp);
    }

    /**
     * Add a row for simple datatype.
     */
    private SimpleEntry<ComboBox<SimpleStructType>, TextField> addSimpleRow()
    {
        // Get row index.
        int index = structure.size();
        structure.add(null);

        // Make TextField which represent the header from the input file.
        TextField fromHeader = new TextField();
        fromHeader.setId("DRAGANDDROP");
        fromHeader.setEditable(false);
        Rectangle rect = getRectangle(DEFAULT_RECTANGLE_WIDTH, fromHeader.heightProperty());

        // Label to show an example.
        Label example = new Label("");

        // On drag from this element. The text is copied.
        fromHeader.setOnDragDetected(e ->
        {
            if (headersIndexAndExamples != null)
            {
                if (headersIndexAndExamples.containsKey(fromHeader.getText()))
                {
                    Dragboard db = fromHeader.startDragAndDrop(TransferMode.COPY);
                    db.setDragView(getDragAndDropScene(new TextField(fromHeader.getText())).snapshot(null), e.getX(), e.getY());
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(fromHeader.getText());
                    db.setContent(cc);
                }
            }
        });

        // On drag hovering above element. Accept copying.
        fromHeader.setOnDragOver(e ->
        {
            e.acceptTransferModes(TransferMode.COPY);
        });

        // On drag drop. Set the text to the string from the copy.
        fromHeader.setOnDragDropped(e ->
        {
            Dragboard db = e.getDragboard();
            if (db.hasString())
            {
                if (headersIndexAndExamples != null)
                {
                    if (headersIndexAndExamples.containsKey(db.getString()))
                    {
                        fromHeader.setText(db.getString());
                        example.setText(headersIndexAndExamples.get(db.getString()).getValue());
                    }
                }
                e.setDropCompleted(true);
            }
            else
            {
                e.setDropCompleted(false);
            }
        });

        // On drag from element to another i done. Reset element.
        fromHeader.setOnDragDone(e ->
        {
            List<StructEntityInterface> sei = getStructure();
            if (sei.size() > 0)
            {
                Integer fromIndex = headersIndexAndExamples.get(fromHeader.getText()).getKey();

                if (fromIndex == null)
                {
                    e.setDropCompleted(false);
                }
                sei.set(index, null);
            }
            fromHeader.setText("");
            example.setText("");
        });

        // Set margin for first element.
        GridPane.setMargin(rect, new Insets(0.0, 0.0, 0.0, INDENT));

        // ComboBox to choose SimpleStructType.
        ComboBox<SimpleStructType> cmbType = getSimpleTypeBox();

        // TextField for the name of the output column.
        TextField toColumn = new TextField();

        // Delete button.
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction((event) ->
        {
            removeSimpleStructure(GridPane.getRowIndex(btnDelete));
        });

        // Set size.
        fromHeader.setMinWidth(DEFAULT_TEXTFIELD_SIZE);
        fromHeader.setPrefWidth(DEFAULT_TEXTFIELD_SIZE);
        fromHeader.setMaxWidth(DEFAULT_TEXTFIELD_SIZE);
        example.setMinWidth(DEFAULT_LABEL_SIZE);
        example.setPrefWidth(DEFAULT_LABEL_SIZE);
        example.setMaxWidth(DEFAULT_LABEL_SIZE);
        cmbType.setMinWidth(DEFAULT_COMBOBOX_SIZE);
        cmbType.setPrefWidth(DEFAULT_COMBOBOX_SIZE);
        cmbType.setMaxWidth(DEFAULT_COMBOBOX_SIZE);
        toColumn.setMinWidth(DEFAULT_TEXTFIELD_SIZE);
        toColumn.setPrefWidth(DEFAULT_TEXTFIELD_SIZE);
        toColumn.setMaxWidth(DEFAULT_TEXTFIELD_SIZE);
        btnDelete.setMinWidth(DEFAULT_TEXTFIELD_SIZE);
        btnDelete.setPrefWidth(DEFAULT_TEXTFIELD_SIZE);
        btnDelete.setMaxWidth(DEFAULT_TEXTFIELD_SIZE);

        GridPane gp = new GridPane();
        GridPane.setConstraints(rect, 0, 0);
        GridPane.setConstraints(fromHeader, 1, 0);
        gp.getChildren().addAll(rect, fromHeader);

        // Set constraints and add to grid.
        int rowCount = getRowCount() - 1;
        GridPane.setConstraints(gp, 0, rowCount);
        GridPane.setConstraints(example, 1, rowCount);
        GridPane.setConstraints(cmbType, 2, rowCount);
        GridPane.setConstraints(toColumn, 3, rowCount);
        GridPane.setConstraints(btnDelete, 4, rowCount);
        this.getChildren().addAll(gp, example, cmbType, toColumn, btnDelete);

        // Map nodes to structure index
        nodeIndexMap.put(gp, index);
        nodeIndexMap.put(example, index);
        nodeIndexMap.put(cmbType, index);
        nodeIndexMap.put(toColumn, index);
        nodeIndexMap.put(btnDelete, index);

        // Create and adds a ChangeListener.
        ChangeListener cl = getSimpleChangeListener(fromHeader, cmbType, toColumn);
        fromHeader.textProperty().addListener(cl);
        cmbType.valueProperty().addListener(cl);
        toColumn.textProperty().addListener(cl);

        // Add masterListener.
        if (masterListener != null)
        {
            fromHeader.textProperty().addListener(masterListener);
            cmbType.valueProperty().addListener(masterListener);
            toColumn.textProperty().addListener(masterListener);
        }

        return new SimpleEntry<>(cmbType, toColumn);
    }

    /**
     * Add a row for collection.
     */
    private SimpleEntry<ComboBox<CollectionStructType>, SimpleEntry<TextField, ProfileGrid>> addCollectionRow()
    {
        // Nodes for row.
        ComboBox<CollectionStructType> cmbType = getCollectionTypeBox();
        Rectangle rect = getRectangle(DEFAULT_RECTANGLE_WIDTH + DEFAULT_TEXTFIELD_SIZE + DEFAULT_LABEL_SIZE + (DEFAULT_GAP * 2), cmbType.heightProperty());
        TextField toColumn = new TextField();

        // Delete button.
        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction((event) ->
        {
            removeCollectionStructure(GridPane.getRowIndex(btnDelete));
        });

        // Index of row.
        int index = structure.size();
        structure.add(null);

        // Set Grid constraints.
        int rowCount = getRowCount() - 1;
        GridPane.setMargin(rect, new Insets(0.0, 0.0, 0.0, INDENT));
        GridPane.setConstraints(rect, 0, rowCount);
        GridPane.setColumnSpan(rect, 2);
        GridPane.setConstraints(cmbType, 2, rowCount);
        GridPane.setConstraints(toColumn, 3, rowCount);
        GridPane.setConstraints(btnDelete, 4, rowCount);

        // Set size.
        cmbType.setMinWidth(DEFAULT_COMBOBOX_SIZE);
        cmbType.setPrefWidth(DEFAULT_COMBOBOX_SIZE);
        cmbType.setMaxWidth(DEFAULT_COMBOBOX_SIZE);
        toColumn.setMinWidth(DEFAULT_TEXTFIELD_SIZE);
        toColumn.setPrefWidth(DEFAULT_TEXTFIELD_SIZE);
        toColumn.setMaxWidth(DEFAULT_TEXTFIELD_SIZE);
        btnDelete.setMinWidth(DEFAULT_TEXTFIELD_SIZE);
        btnDelete.setPrefWidth(DEFAULT_TEXTFIELD_SIZE);
        btnDelete.setMaxWidth(DEFAULT_TEXTFIELD_SIZE);

        // Add nodes to Grid.
        this.getChildren().addAll(rect, cmbType, toColumn, btnDelete);
        rowCount = getRowCount() - 1;

        // Make ChangeListener.
        ChangeListener cl = getCollectionChangeListener(cmbType, toColumn);

        // Make new grid for structure handling.
        ProfileGrid col = new ProfileGrid(false, INDENT + DEFAULT_INDENT, cl);

        // Add header mapping to ProfileGrid and add the new grid to this grid.
        col.addHashMap(headersIndexAndExamples);
        GridPane.setConstraints(col, 0, rowCount, 5, 1);
        this.getChildren().add(col);

        // Map nodes to structure index
        nodeIndexMap.put(col, index);
        nodeIndexMap.put(cmbType, index);
        nodeIndexMap.put(toColumn, index);
        nodeIndexMap.put(btnDelete, index);

        // Map Grid to row index and add listeners
        collectionMap.put(index, col);
        cmbType.valueProperty().addListener(cl);
        toColumn.textProperty().addListener(cl);

        // Run master ChangeListener to update collection.
        if (masterListener != null)
        {
            cmbType.valueProperty().addListener(masterListener);
            toColumn.textProperty().addListener(masterListener);
        }

        return new SimpleEntry<>(cmbType, new SimpleEntry<>(toColumn, col));
    }

    /**
     * Count rows in gridpane.
     * @param pane
     * @return
     */
    private int getRowCount()
    {
        int numRows = 0;
        for (int i = 0; i < this.getChildren().size(); i++)
        {
            Node child = this.getChildren().get(i);
            Integer rowIndex = GridPane.getRowIndex(child);
            if (rowIndex != null)
            {
                numRows = Math.max(numRows, rowIndex + 1);
            }
        }
        return numRows;
    }

    /**
     * Returns combobox with all simple datatypes.
     * @return
     */
    private ComboBox<SimpleStructType> getSimpleTypeBox()
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
     * Returns combobox with all collection datatypes.
     * @return
     */
    private ComboBox<CollectionStructType> getCollectionTypeBox()
    {
        ComboBox<CollectionStructType> cb = new ComboBox();
        ObservableList<CollectionStructType> items = FXCollections.observableArrayList();
        for (CollectionStructType value : CollectionStructType.values())
        {
            items.add(value);
        }
        cb.setItems(items);
        return cb;
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

    private HashMap<Node, Integer> getNodeIndex()
    {
        return nodeIndexMap;
    }

    /**
     * Updates structure list for simple datatypes to fit changed information.
     * @param cmb
     * @param tf
     * @param cb
     * @return
     */
    private ChangeListener getSimpleChangeListener(TextField fromHeader, ComboBox<SimpleStructType> cmb, TextField tf)
    {
        ChangeListener cl = new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                if (fromHeader != null && cmb != null && tf != null)
                {
                    if (!fromHeader.getText().isEmpty())
                    {
                        Integer fromIndex = headersIndexAndExamples.get(fromHeader.getText()).getKey();
                        if (fromIndex != null)
                        {
                            if (!newValue.equals(oldValue) && cmb.getValue() != null && !tf.getText().isEmpty())
                            {
                                switch (cmb.getValue())
                                {
                                    case DATE:
                                        structure.set(nodeIndexMap.get(cmb), new StructEntityDate(tf.getText(), fromIndex));
                                        return;
                                    case DOUBLE:
                                        structure.set(nodeIndexMap.get(cmb), new StructEntityDouble(tf.getText(), fromIndex));
                                        return;
                                    case INTEGER:
                                        structure.set(nodeIndexMap.get(cmb), new StructEntityInteger(tf.getText(), fromIndex));
                                        return;
                                    case STRING:
                                        structure.set(nodeIndexMap.get(cmb), new StructEntityString(tf.getText(), fromIndex));
                                        return;
                                    default:
                                        break;
                                }
                            }
                        }

                        structure.set(nodeIndexMap.get(cmb), null);
                    }
                }
            }
        };
        return cl;
    }

    private HashMap<Integer, ProfileGrid> getCollectionMap()
    {
        return collectionMap;
    }

    /**
     * Updates structure list for collection datatypes to fit changed
     * information.
     * @param cmb
     * @param tf
     * @param cb
     * @return
     */
    private ChangeListener getCollectionChangeListener(ComboBox<CollectionStructType> cmb, TextField tf)
    {
        ChangeListener cl = new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                if (cmb != null && tf != null)
                {
                    if (!newValue.equals(oldValue) && cmb.getValue() != null && !tf.getText().isEmpty())
                    {
                        switch (cmb.getValue())
                        {
                            case ARRAY:
                                structure.set(getNodeIndex().get(cmb), new StructEntityArray(tf.getText(), getCollectionMap().get(getNodeIndex().get(cmb)).getStructure()));
                                return;
                            case OBJECT:
                                structure.set(getNodeIndex().get(cmb), new StructEntityObject(tf.getText(), getCollectionMap().get(getNodeIndex().get(cmb)).getStructure()));
                                return;
                            default:
                                break;
                        }
                    }
                    structure.set(getNodeIndex().get(cmb), null);
                }
            }
        };
        return cl;
    }

    /**
     * Add Map from other class.
     * @param headersIndexAndExamples
     */
    public void addHashMap(HashMap<String, Entry<Integer, String>> headersIndexAndExamples)
    {
        clear();
        this.headersIndexAndExamples = headersIndexAndExamples;
        if (headersIndexAndExamples != null)
        {
            if (!headersIndexAndExamples.isEmpty())
            {
                for (String string : headersIndexAndExamples.keySet())
                {
                    headersIndexToName.put(headersIndexAndExamples.get(string).getKey(), string);
                }
            }
        }
    }

    /**
     * Clear data.
     */
    private void clear()
    {
        structure = new ArrayList<>();
        collectionMap = new HashMap();
        nodeIndexMap = new HashMap();
        headersIndexToName = new HashMap();
        this.getChildren().clear();
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();
        if (IS_MASTER)
        {
            addHeader();
        }
        addRowAdder();
    }

    /**
     * Creates a rectangle with the right indent colour.
     * @param width
     * @param height
     * @return
     */
    private Rectangle getRectangle(double width, ReadOnlyDoubleProperty height)
    {
        Rectangle rect = new Rectangle();
        rect.setWidth(width);
        rect.heightProperty().bind(height);
        GridPane.setValignment(rect, VPos.TOP);
        Double colourCount = (INDENT / DEFAULT_INDENT) % COLOURS.length;
        rect.setFill(COLOURS[colourCount.intValue()]);
        return rect;
    }

    /**
     * Load a structure to profile view.
     * @param structure
     */
    public void loadStructure(List<StructEntityInterface> structure)
    {
        for (StructEntityInterface sei : structure)
        {
            if (sei instanceof SimpleEntity)
            {
                loadSimpleStructure((SimpleEntity) sei);
            }
            else if (sei instanceof CollectionEntity)
            {
                loadCollectionStructure((CollectionEntity) sei);
            }
        }
    }

    /**
     * Setup simple structure.
     * @param se
     */
    private void loadSimpleStructure(SimpleEntity se)
    {
        SimpleEntry<ComboBox<SimpleStructType>, TextField> entry = addSimpleRow();
        entry.getValue().setText(se.getColumnName());
        switch (se.getSST())
        {
            case DATE:
                entry.getKey().getSelectionModel().select(SimpleStructType.DATE);
                break;
            case DOUBLE:
                entry.getKey().getSelectionModel().select(SimpleStructType.DOUBLE);
                break;
            case INTEGER:
                entry.getKey().getSelectionModel().select(SimpleStructType.INTEGER);
                break;
            case STRING:
                entry.getKey().getSelectionModel().select(SimpleStructType.STRING);
                break;
            default:
                entry.getKey().getSelectionModel().selectFirst();
                break;
        }
    }

    /**
     * Setup collection structure and add collection to new ProfileGrid.
     * @param ce
     */
    private void loadCollectionStructure(CollectionEntity ce)
    {
        SimpleEntry<ComboBox<CollectionStructType>, SimpleEntry<TextField, ProfileGrid>> entry = addCollectionRow();
        entry.getValue().getKey().setText(ce.getColumnName());
        switch (ce.getCST())
        {
            case ARRAY:
                entry.getKey().getSelectionModel().select(CollectionStructType.ARRAY);
                break;
            case OBJECT:
                entry.getKey().getSelectionModel().select(CollectionStructType.OBJECT);
                break;
            default:
                entry.getKey().getSelectionModel().selectFirst();
                break;
        }
        entry.getValue().getValue().loadStructure(ce.getCollection());
    }

    /**
     * Removes simple structure.
     * @param i
     */
    private void removeSimpleStructure(int i)
    {
        boolean hasRemoved = false;
        for (int j = 0; j < this.getChildren().size(); j *= 1)
        {
            int structureIndex = i;
            if (IS_MASTER)
            {
                structureIndex = i - 1;
            }
            if (GridPane.getRowIndex(this.getChildren().get(j)) == i)
            {
                if (!hasRemoved && structure.size() > structureIndex)
                {
                    structure.remove(structureIndex);
                    hasRemoved = true;
                }
                if (nodeIndexMap.containsKey(this.getChildren().get(j)))
                {
                    nodeIndexMap.remove(this.getChildren().get(j));
                }
                this.getChildren().remove(this.getChildren().get(j));
            }
            else
            {
                j++;
            }
        }
        shiftStructure(i, -1);
    }

    /**
     * Removes Collection structure.
     * @param i
     */
    private void removeCollectionStructure(int i)
    {
        boolean hasRemoved = false;
        for (int j = 0; j < this.getChildren().size(); j *= 1)
        {
            int structureIndex = IS_MASTER ? i - 1 : i;

            Node node = this.getChildren().get(j);
            if (GridPane.getRowIndex(node) == i && !hasRemoved && structure.size() > structureIndex)
            {
                structure.remove(structureIndex);

                if (collectionMap.containsKey(structureIndex))
                {
                    collectionMap.remove(collectionMap.get(structureIndex));
                }
                hasRemoved = true;
            }
            if (GridPane.getRowIndex(node) == i || GridPane.getRowIndex(node) == i + 1)
            {
                if (nodeIndexMap.containsKey(node))
                {
                    nodeIndexMap.remove(node);
                }
                this.getChildren().remove(node);
            }
            else
            {
                j++;
            }
        }
        shiftStructure(i, -2);
    }

    /**
     *
     * @param fromIndex
     * @param shiftIdentifier
     */
    private void shiftStructure(int fromIndex, int shiftIdentifier)
    {
        for (int j = 0; j < this.getChildren().size(); j++)
        {
            int rowIndex = GridPane.getRowIndex(this.getChildren().get(j));
            if (rowIndex > fromIndex)
            {
                if (nodeIndexMap.containsKey(this.getChildren().get(j)))
                {
                    nodeIndexMap.replace(this.getChildren().get(j), (nodeIndexMap.get(this.getChildren().get(j)) + shiftIdentifier));
                }

                int structureIndex = IS_MASTER ? rowIndex - 1 : rowIndex;
                if (collectionMap.containsKey(structureIndex))
                {
                    ProfileGrid removed = collectionMap.remove(structureIndex);
                    collectionMap.put(structureIndex + shiftIdentifier, removed);
                }
                GridPane.setRowIndex(this.getChildren().get(j), rowIndex + shiftIdentifier);
            }
        }
    }

    /**
     * Creates Rule View.
     * @return
     */
    public GridPane createRuleView()
    {
        GridPane ruleView = new GridPane();
        setupGridPane(ruleView, IS_MASTER ? 5.0 : -0.1);

        if (IS_MASTER)
        {
            // Header
            Label lbl1 = new Label("From Header");
            Label lbl2 = new Label("Example");
            Label lbl3 = new Label("To");
            Label lbl4 = new Label("Rule Type");
            Label lbl5 = new Label("Rule Value");

            GridPane.setConstraints(lbl1, 1, 0);
            GridPane.setConstraints(lbl2, 2, 0);
            GridPane.setConstraints(lbl3, 3, 0);
            GridPane.setConstraints(lbl4, 4, 0);
            GridPane.setConstraints(lbl5, 5, 0);

            ruleView.getChildren().addAll(lbl1, lbl2, lbl3, lbl4, lbl5);
        }

        int index = 0;
        for (int i = 0; i < structure.size(); i++)
        {
            if (structure.get(i) != null)
            {
                if (structure.get(i) instanceof SimpleEntity)
                {
                    final int entityIndex = index;

                    SimpleEntity se = (SimpleEntity) structure.get(i);
                    String headerName = headersIndexToName.get(se.getInputIndex());
                    Label lblHeader = new Label(headerName);
                    Label lblExample = new Label(headersIndexAndExamples.get(headerName).getValue());
                    Label lblTo = new Label(se.getColumnName());
                    ComboBox<String> cmbDefaultRule = new ComboBox<>();
                    DatePicker defaultDate = new DatePicker();
                    TextField defaultString = new TextField();
                    TextField dateFormat = new TextField();
                    Label lblForced = new Label("Force:");
                    CheckBox cbForced = new CheckBox();
                    Rectangle colourBox = getRectangle(DEFAULT_RECTANGLE_WIDTH, defaultString.heightProperty());

                    GridPane.setConstraints(colourBox, 0, IS_MASTER ? entityIndex + 1 : entityIndex);
                    GridPane.setConstraints(lblHeader, 1, IS_MASTER ? entityIndex + 1 : entityIndex);
                    GridPane.setConstraints(lblExample, 2, IS_MASTER ? entityIndex + 1 : entityIndex);
                    GridPane.setConstraints(lblTo, 3, IS_MASTER ? entityIndex + 1 : entityIndex);
                    GridPane.setConstraints(cmbDefaultRule, 4, IS_MASTER ? entityIndex + 1 : entityIndex);
                    GridPane defaultValueRow = new GridPane();
                    setupGridPane(defaultValueRow, -0.1);
                    GridPane.setConstraints(lblForced, 0, 0);
                    GridPane.setConstraints(cbForced, 1, 0);
                    GridPane.setConstraints(defaultDate, 2, 0);
                    GridPane.setConstraints(defaultString, 2, 0);
                    GridPane.setConstraints(dateFormat, 0, 0);
                    defaultValueRow.getChildren().addAll(defaultDate, defaultString, lblForced, cbForced);
                    GridPane.setConstraints(defaultValueRow, 5, IS_MASTER ? entityIndex + 1 : entityIndex);
                    GridPane dateFormatRow = new GridPane();
                    setupGridPane(dateFormatRow, -0.1);
                    GridPane.setConstraints(dateFormatRow, 5, IS_MASTER ? entityIndex + 1 : entityIndex);
                    dateFormatRow.getChildren().addAll(dateFormat);

                    // Set size.
                    lblHeader.setMinWidth(DEFAULT_LABEL_SIZE);
                    lblHeader.setPrefWidth(DEFAULT_LABEL_SIZE);
                    lblHeader.setMaxWidth(DEFAULT_LABEL_SIZE);
                    lblExample.setMinWidth(DEFAULT_LABEL_SIZE);
                    lblExample.setPrefWidth(DEFAULT_LABEL_SIZE);
                    lblExample.setMaxWidth(DEFAULT_LABEL_SIZE);
                    lblTo.setMinWidth(DEFAULT_LABEL_SIZE);
                    lblTo.setPrefWidth(DEFAULT_LABEL_SIZE);
                    lblTo.setMaxWidth(DEFAULT_LABEL_SIZE);
                    cmbDefaultRule.setMinWidth(DEFAULT_COMBOBOX_SIZE);
                    cmbDefaultRule.setPrefWidth(DEFAULT_COMBOBOX_SIZE);
                    cmbDefaultRule.setMaxWidth(DEFAULT_COMBOBOX_SIZE);
                    defaultDate.setMinWidth(DEFAULT_TEXTFIELD_SIZE);
                    defaultDate.setPrefWidth(DEFAULT_TEXTFIELD_SIZE);
                    defaultDate.setMaxWidth(DEFAULT_TEXTFIELD_SIZE);
                    defaultString.setMinWidth(DEFAULT_TEXTFIELD_SIZE);
                    defaultString.setPrefWidth(DEFAULT_TEXTFIELD_SIZE);
                    defaultString.setMaxWidth(DEFAULT_TEXTFIELD_SIZE);
                    cbForced.setMinWidth(DEFAULT_CHECKBOX_SIZE);
                    cbForced.setPrefWidth(DEFAULT_CHECKBOX_SIZE);
                    cbForced.setMaxWidth(DEFAULT_CHECKBOX_SIZE);
                    dateFormat.setMinWidth(DEFAULT_TEXTFIELD_SIZE);
                    dateFormat.setPrefWidth(DEFAULT_TEXTFIELD_SIZE);
                    dateFormat.setMaxWidth(DEFAULT_TEXTFIELD_SIZE);

                    defaultDate.setVisible(false);
                    defaultString.setVisible(false);
                    defaultValueRow.setVisible(false);

                    defaultDate.valueProperty().addListener((observable, oldValue, newValue) ->
                    {
                        if (newValue != null)
                        {
                            se.setDefaultValue(new DefaultDateRule(Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant()), entityIndex, cbForced.isSelected()));
                        }
                        else
                        {
                            se.setDefaultValue(null);
                        }
                    });

                    defaultString.focusedProperty().addListener((observable, oldValue, newValue) ->
                    {
                        if (!newValue)
                        {
                            switch (se.getSST())
                            {
                                case INTEGER:
                                    if (defaultString.getText() != null)
                                    {
                                        if (defaultString.getText().isEmpty())
                                        {
                                            defaultString.setText("0");
                                        }
                                    }
                                    else
                                    {
                                        defaultString.setText("0");
                                    }
                                    break;
                                case DOUBLE:
                                    if (defaultString.getText() != null)
                                    {
                                        if (defaultString.getText().isEmpty())
                                        {
                                            defaultString.setText("0.0");
                                        }
                                    }
                                    else
                                    {
                                        defaultString.setText("0.0");
                                    }
                                    break;
                                case STRING:
                                    if (defaultString.getText() != null)
                                    {
                                        if (defaultString.getText().isEmpty())
                                        {
                                            defaultString.setText("");
                                        }
                                    }
                                    else
                                    {
                                        defaultString.setText("");
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                    defaultString.textProperty().addListener((observable, oldValue, newValue) ->
                    {
                        if (newValue != null)
                        {
                            switch (se.getSST())
                            {
                                case STRING:
                                    se.setDefaultValue(new DefaultStringRule(newValue, entityIndex, cbForced.isSelected()));
                                    break;
                                case INTEGER:
                                    if (newValue.isEmpty())
                                    {
                                        break;
                                    }
                                    try
                                    {
                                        int integer = Integer.parseInt(newValue);
                                        se.setDefaultValue(new DefaultIntegerRule(integer, entityIndex, cbForced.isSelected()));
                                    }
                                    catch (NumberFormatException ex)
                                    {
                                        if (oldValue == null)
                                        {
                                            defaultString.setText("");
                                            break;
                                        }
                                        try
                                        {
                                            int integer = Integer.parseInt(oldValue);
                                            defaultString.setText(String.valueOf(integer));
                                        }
                                        catch (NumberFormatException ex1)
                                        {
                                            defaultString.setText("");
                                        }
                                    }
                                    break;
                                case DOUBLE:
                                    if (newValue.isEmpty())
                                    {
                                        break;
                                    }
                                    try
                                    {
                                        Double dbl = Double.parseDouble(newValue);
                                        se.setDefaultValue(new DefaultDoubleRule(dbl, entityIndex, cbForced.isSelected()));
                                    }
                                    catch (NumberFormatException ex)
                                    {
                                        if (oldValue == null)
                                        {
                                            defaultString.setText("");
                                            break;
                                        }
                                        try
                                        {
                                            Double dbl = Double.parseDouble(oldValue);
                                            defaultString.setText(String.valueOf(dbl));
                                        }
                                        catch (NumberFormatException ex1)
                                        {
                                            defaultString.setText("");
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        else
                        {
                            se.setDefaultValue(null);
                        }
                    });

                    cbForced.selectedProperty().addListener((observable, oldValue, newValue) ->
                    {
                        LocalDate ld = defaultDate.getValue();
                        defaultDate.setValue(null);
                        defaultDate.setValue(ld);
                        String str = defaultString.getText();
                        defaultString.setText(null);
                        defaultString.setText(str);
                    });

                    dateFormat.textProperty().addListener((observable, oldValue, newValue) ->
                    {
                        StructEntityDate sed = (StructEntityDate) se;

                        if (newValue != null)
                        {
                            sed.setDfr(new DateFormatRule(newValue, entityIndex));
                        }
                        else
                        {
                            sed.setDfr(null);
                        }
                    });

                    cmbDefaultRule.valueProperty().addListener((observable, oldValue, newValue) ->
                    {
                        switch (newValue)
                        {
                            case "Default":
                                switch (se.getSST())
                                {
                                    case DATE:
                                        defaultDate.setVisible(true);
                                        defaultString.setVisible(false);
                                        defaultValueRow.setVisible(true);
                                        dateFormatRow.setVisible(false);
                                        LocalDate ld = defaultDate.getValue();
                                        defaultDate.setValue(null);
                                        defaultDate.setValue(ld);
                                        break;
                                    default:
                                        defaultDate.setVisible(false);
                                        defaultString.setVisible(true);
                                        defaultValueRow.setVisible(true);
                                        dateFormatRow.setVisible(false);
                                        String str = defaultString.getText();
                                        defaultString.setText(null);

                                        if (str == null)
                                        {
                                            switch (se.getSST())
                                            {
                                                case INTEGER:
                                                    defaultString.setText("0");
                                                    break;
                                                case DOUBLE:
                                                    defaultString.setText("0.0");
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        else
                                        {
                                            defaultString.setText(str);
                                        }
                                        break;
                                }
                                break;
                            case "DateFormat":
                                se.setDefaultValue(null);
                                defaultDate.setVisible(false);
                                defaultString.setVisible(false);
                                defaultValueRow.setVisible(false);
                                dateFormatRow.setVisible(true);
                                defaultDate.setValue(null);
                                defaultString.setText(null);
                                break;
                            default:
                                se.setDefaultValue(null);
                                defaultDate.setVisible(false);
                                defaultString.setVisible(false);
                                defaultValueRow.setVisible(false);
                                dateFormatRow.setVisible(false);
                                defaultDate.setValue(null);
                                defaultString.setText(null);
                                break;
                        }
                    });

                    ObservableList<String> rules = FXCollections.observableArrayList();
                    rules.addAll("No Rule", "Default");
                    if (se.getSST() == SimpleStructType.DATE)
                    {
                        rules.add("DateFormat");
                    }
                    cmbDefaultRule.setItems(rules);
                    cmbDefaultRule.getSelectionModel().selectFirst();

                    ruleView.getChildren().addAll(colourBox, lblHeader, lblExample, lblTo, cmbDefaultRule, defaultValueRow, dateFormatRow);

                    index++;
                }
                else if (structure.get(i) instanceof CollectionEntity)
                {
                    if (collectionMap.containsKey(i))
                    {
                        Node node = collectionMap.get(i).createRuleView();

                        // Set margin for first element.
                        GridPane.setMargin(node, new Insets(0.0, 0.0, 0.0, IS_MASTER ? DEFAULT_INDENT : INDENT));
                        GridPane.setConstraints(node, 0, IS_MASTER ? index + 1 : index, 6, 1);

                        ruleView.getChildren().add(node);

                        index++;
                    }
                }
            }

        }
        return ruleView;
    }
}
