/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.entity.StructEntityDate;
import shoreline_exam_2018.be.output.structure.entity.StructEntityDouble;
import shoreline_exam_2018.be.output.structure.entity.StructEntityInteger;
import shoreline_exam_2018.be.output.structure.entity.StructEntityString;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 *
 * @author Asbamz
 */
public class ProfileEntitySimple extends ProfileEntity
{
    private static final int ROW_SPAN = 1;
    private final StructurePane owner;

    private int index;
    private GridPane indentPane;
    private Rectangle rect;
    private TextField fromHeader;
    private Label example;
    private ComboBox<SimpleStructType> cmbType;
    private TextField toColumn;
    private Button btnDelete;

    /**
     * Makes row for simple datatype.
     * @param owner
     */
    public ProfileEntitySimple(StructurePane owner)
    {
        this.owner = owner;

        ProfileSpecification specification = owner.getSpecification();
        DragAndDropHandler dragAndDropHandler = specification.getDragAndDropHandler();
        ObservableMap<String, Map.Entry<Integer, String>> headersIndexAndExamples = specification.getHeadersIndexAndExamples();
        List<StructEntityInterface> structure = specification.getStructure();
        List<ProfileEntity> structureEntities = specification.getStructureEntities();
        ChangeListener masterListener = owner.getMasterListener();

        // Get row index.
        index = structure.size();
        structure.add(null);
        structureEntities.add(this);

        // Make TextField which represent the header from the input file.
        fromHeader = new TextField();
        fromHeader.setId(specification.getCSSIdDragAndDrop());
        fromHeader.setEditable(false);
        rect = new ColourRectangle(specification.getDefaultRectangleWidth(), fromHeader.heightProperty(), specification);

        // Label to show an example.
        example = new Label("");

        // On drag from this element. The text is copied.
        fromHeader.setOnDragDetected(dragAndDropHandler.getOnHeaderDragDetected(fromHeader));

        // On drag hovering above element. Accept copying.
        fromHeader.setOnDragOver(dragAndDropHandler.getOnDragOverAcceptCopy());

        // On drag drop. Set the text to the string from the copy.
        fromHeader.setOnDragDropped(dragAndDropHandler.getOnHeaderDragDropped(fromHeader, example));

        // On drag from element to another i done. Reset element.
        fromHeader.setOnDragDone(dragAndDropHandler.getOnHeaderDragDone(index, fromHeader, example));

        // Set margin for first element.
        GridPane.setMargin(rect, new Insets(0.0, 0.0, 0.0, specification.getIndent()));

        // ComboBox to choose SimpleStructType.
        cmbType = getSimpleTypeBox();

        // TextField for the name of the output column.
        toColumn = new TextField();

        // Delete button.
        btnDelete = new Button("Delete");
        btnDelete.setOnAction((event) ->
        {
            removeSimpleStructure();
        });

        // Set size.
        specification.setDefaultWidth(fromHeader);
        specification.setDefaultWidth(example);
        specification.setDefaultWidth(cmbType);
        specification.setDefaultWidth(toColumn);
        specification.setDefaultWidth(btnDelete);

        indentPane = new GridPane();
        GridPane.setConstraints(rect, 0, 0);
        GridPane.setConstraints(fromHeader, 1, 0);
        indentPane.getChildren().addAll(rect, fromHeader);

        // Set constraints and add to grid.
        int rowCount = owner.getRowCount() - 1;
        GridPane.setConstraints(indentPane, 0, rowCount);
        GridPane.setConstraints(example, 1, rowCount);
        GridPane.setConstraints(cmbType, 2, rowCount);
        GridPane.setConstraints(toColumn, 3, rowCount);
        GridPane.setConstraints(btnDelete, 4, rowCount);

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
    }

    /**
     * Removes simple structure.
     */
    private void removeSimpleStructure()
    {
        owner.getSpecification().getStructure().remove(index);
        owner.getSpecification().getStructureEntities().remove(index);

        owner.removeNode(indentPane);
        owner.removeNode(example);
        owner.removeNode(cmbType);
        owner.removeNode(toColumn);
        owner.removeNode(btnDelete);

        indentPane = null;
        fromHeader = null;
        rect = null;
        example = null;
        cmbType = null;
        toColumn = null;
        btnDelete = null;

        owner.shiftStructure(index, -ROW_SPAN);
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
     * Updates structure list for simple datatypes to fit changed information.
     * Only when all data is set.
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
                        List<StructEntityInterface> structure = owner.getStructure();
                        Integer fromIndex = owner.getSpecification().getHeadersIndexAndExamples().get(fromHeader.getText()).getKey();
                        if (fromIndex != null)
                        {
                            if (!newValue.equals(oldValue) && cmb.getValue() != null && !tf.getText().isEmpty())
                            {

                                switch (cmb.getValue())
                                {
                                    case DATE:
                                        structure.set(index, new StructEntityDate(tf.getText(), fromIndex));
                                        return;
                                    case DOUBLE:
                                        structure.set(index, new StructEntityDouble(tf.getText(), fromIndex));
                                        return;
                                    case INTEGER:
                                        structure.set(index, new StructEntityInteger(tf.getText(), fromIndex));
                                        return;
                                    case STRING:
                                        structure.set(index, new StructEntityString(tf.getText(), fromIndex));
                                        return;
                                    default:
                                        break;
                                }
                            }
                        }

                        structure.set(index, null);
                    }
                }
            }
        };
        return cl;
    }

    /**
     * Returns generated nodes.
     * @return
     */
    List<Node> getNodes()
    {
        List<Node> nodes = new ArrayList<>();
        nodes.add(indentPane);
        nodes.add(example);
        nodes.add(cmbType);
        nodes.add(toColumn);
        nodes.add(btnDelete);
        return nodes;
    }

    /**
     * Get ComboBox.
     * @return
     */
    ComboBox<SimpleStructType> getCmbType()
    {
        return cmbType;
    }

    /**
     * Get TextField.
     * @return
     */
    TextField getToColumn()
    {
        return toColumn;
    }

    /**
     * Get index.
     * @param index
     */
    void setIndex(int index)
    {
        this.index = index;
    }

    @Override
    void shiftPosition(int fromIndex, int shiftIdentifier)
    {
        if (index > fromIndex)
        {
            index += shiftIdentifier;
        }
    }
}
