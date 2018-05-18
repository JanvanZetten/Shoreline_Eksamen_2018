/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import shoreline_exam_2018.be.output.structure.CollectionEntity;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.entry.StructEntityArray;
import shoreline_exam_2018.be.output.structure.entry.StructEntityDate;
import shoreline_exam_2018.be.output.structure.entry.StructEntityDouble;
import shoreline_exam_2018.be.output.structure.entry.StructEntityInteger;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.be.output.structure.entry.StructEntityString;
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
    private final double DEFAULT_RECTANGLE_WIDTH = 4.0;
    private final double INDENT; // Indent for collections.
    private final Paint[] COLOURS = new Paint[]
    {
        Color.web("737F8C"), Color.web("4986A8"), Color.web("2C546D"), Color.web("4D4D4D")
    }; //

    private HashMap<String, Entry<Integer, String>> headersIndexAndExamples; // Mapping column headers from input file to their example.
    private HashMap<Integer, ProfileGrid> collectionMap; // Mapping row index to a ProfileGrid.
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
        if (IS_MASTER)
        {
            setupGridPane(5.0);
        }
        else
        {
            setupGridPane(-0.1);
        }
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
    private void setupGridPane(double padding)
    {
        // Clear constraints and settings.
        this.getChildren().clear();
        this.getRowConstraints().clear();
        this.getColumnConstraints().clear();

        // Set gap between grid elements and padding around grid.
        this.setHgap(DEFAULT_GAP);
        this.setVgap(DEFAULT_GAP);
        this.setPadding(new Insets(padding, padding, padding, padding));

        // Set constraints so that the whole view is used.
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(DEFAULT_TEXTFIELD_SIZE + DEFAULT_RECTANGLE_WIDTH + INDENT);
        c1.setPrefWidth(DEFAULT_TEXTFIELD_SIZE + DEFAULT_RECTANGLE_WIDTH + INDENT);
        c1.setMaxWidth(DEFAULT_TEXTFIELD_SIZE + DEFAULT_RECTANGLE_WIDTH + INDENT);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setMinWidth(DEFAULT_LABEL_SIZE);
        c2.setPrefWidth(DEFAULT_LABEL_SIZE);
        c2.setMaxWidth(DEFAULT_LABEL_SIZE);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setMinWidth(DEFAULT_COMBOBOX_SIZE);
        c3.setPrefWidth(DEFAULT_COMBOBOX_SIZE);
        c3.setMaxWidth(DEFAULT_COMBOBOX_SIZE);
        ColumnConstraints c4 = new ColumnConstraints();
        c4.setMinWidth(DEFAULT_TEXTFIELD_SIZE);
        c4.setPrefWidth(DEFAULT_TEXTFIELD_SIZE);
        c4.setMaxWidth(DEFAULT_TEXTFIELD_SIZE);
        this.getColumnConstraints().addAll(c1, c2, c3, c4);
        this.setGridLinesVisible(false);
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
            if (headersIndexAndExamples.get(fromHeader.getText()) != null)
            {
                Dragboard db = fromHeader.startDragAndDrop(TransferMode.COPY);
                db.setDragView(getDragAndDropScene(new TextField(fromHeader.getText())).snapshot(null), e.getX(), e.getY());
                ClipboardContent cc = new ClipboardContent();
                cc.putString(fromHeader.getText());
                db.setContent(cc);
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
                if (headersIndexAndExamples.get(db.getString()) != null)
                {
                    fromHeader.setText(db.getString());
                    example.setText(headersIndexAndExamples.get(db.getString()).getValue());
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
                                structure.set(getNodeIndex().get(cmb), new StructEntityArray(tf.getText(), collectionMap.get(getNodeIndex().get(cmb)).getStructure()));
                                return;
                            case OBJECT:
                                structure.set(getNodeIndex().get(cmb), new StructEntityObject(tf.getText(), collectionMap.get(getNodeIndex().get(cmb)).getStructure()));
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
    }

    /**
     * Clear data.
     */
    private void clear()
    {
        structure = new ArrayList<>();
        collectionMap = new HashMap();
        nodeIndexMap = new HashMap();
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
            if (GridPane.getRowIndex(this.getChildren().get(j)) == i)
            {
                if (!hasRemoved && structure.size() > i - 1)
                {
                    structure.remove(i - 1);
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
        for (int j = 0; j < this.getChildren().size(); j++)
        {
            int rowIndex = GridPane.getRowIndex(this.getChildren().get(j));
            if (rowIndex > i)
            {
                if (nodeIndexMap.containsKey(this.getChildren().get(j)))
                {
                    nodeIndexMap.replace(this.getChildren().get(j), (nodeIndexMap.get(this.getChildren().get(j)) - 1));
                }
                GridPane.setRowIndex(this.getChildren().get(j), rowIndex - 1);
            }
        }
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
            Node node = this.getChildren().get(j);
            if (GridPane.getRowIndex(node) == i && !hasRemoved && structure.size() > i - 1)
            {
                structure.remove(i - 1);
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
        for (int j = 0; j < this.getChildren().size(); j++)
        {
            int rowIndex = GridPane.getRowIndex(this.getChildren().get(j));
            if (rowIndex > i)
            {
                if (nodeIndexMap.containsKey(this.getChildren().get(j)))
                {
                    nodeIndexMap.replace(this.getChildren().get(j), (nodeIndexMap.get(this.getChildren().get(j)) - 2));
                }
                GridPane.setRowIndex(this.getChildren().get(j), rowIndex - 2);
            }
        }
    }
}
