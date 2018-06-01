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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import shoreline_exam_2018.be.output.structure.entity.StructEntityArray;
import shoreline_exam_2018.be.output.structure.entity.StructEntityObject;
import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import shoreline_exam_2018.be.output.structure.StructEntity;

/**
 *
 * @author Asbamz
 */
public class ProfileEntityCollection extends ProfileEntity
{
    private static final int ROW_SPAN = 2;
    private final StructurePane owner;

    private int index;
    private Rectangle rect;
    private ComboBox<CollectionStructType> cmbType;
    private TextField toColumn;
    private Button btnDelete;
    private StructurePane collection;

    /**
     * Makes row for simple datatype.
     * @param owner
     */
    public ProfileEntityCollection(StructurePane owner)
    {
        this.owner = owner;

        ProfileSpecification specification = owner.getSpecification();
        DragAndDropHandler dragAndDropHandler = specification.getDragAndDropHandler();
        ObservableMap<String, Map.Entry<Integer, String>> headersIndexAndExamples = specification.getHeadersIndexAndExamples();
        List<StructEntity> structure = specification.getStructure();
        List<ProfileEntity> structureEntities = specification.getStructureEntities();
        ChangeListener masterListener = owner.getMasterListener();

        // Nodes for row.
        cmbType = getCollectionTypeBox();
        rect = new ColourRectangle(specification.getDefaultRectangleWidth() + specification.getDefaultTextFieldWidth() + specification.getDefaultLabelWidth() + (specification.getDefaultGap() * 6), cmbType.heightProperty(), specification);
        toColumn = new TextField();

        // Delete button.
        btnDelete = new Button("Delete");
        btnDelete.setOnAction((event) ->
        {
            removeCollectionStructure();
        });

        // Index of row.
        index = structure.size();
        structure.add(null);
        structureEntities.add(this);

        // Set Grid constraints.
        int rowCount = owner.getRowCount() - 1;
        GridPane.setMargin(rect, new Insets(0.0, 0.0, 0.0, specification.getIndent()));
        GridPane.setConstraints(rect, 0, rowCount);
        GridPane.setColumnSpan(rect, 2);
        GridPane.setConstraints(cmbType, 2, rowCount);
        GridPane.setConstraints(toColumn, 3, rowCount);
        GridPane.setConstraints(btnDelete, 4, rowCount);

        // Set size.
        specification.setDefaultWidth(cmbType);
        specification.setDefaultWidth(toColumn);
        specification.setDefaultWidth(btnDelete);
        // Add nodes to Grid.
        rowCount++;

        // Make ChangeListener.
        ChangeListener cl = getCollectionChangeListener(cmbType, toColumn);

        // Make new grid for structure handling.
        collection = new StructurePane(false, specification.getIndent() + specification.getDefaultIndent(), cl);

        // Add header mapping to StructurePane and add the new grid to this grid.
        collection.addHashMap(headersIndexAndExamples);
        GridPane.setConstraints(collection, 0, rowCount, 5, 1);

        // Add listeners
        cmbType.valueProperty().addListener(cl);
        toColumn.textProperty().addListener(cl);

        // Run master ChangeListener to update collection.
        if (masterListener != null)
        {
            cmbType.valueProperty().addListener(masterListener);
            toColumn.textProperty().addListener(masterListener);
        }
    }

    /**
     * Removes Collection structure.
     * @param i
     */
    private void removeCollectionStructure()
    {
        owner.getSpecification().getStructure().remove(index);
        owner.getSpecification().getStructureEntities().remove(index);

        owner.removeNode(rect);
        owner.removeNode(cmbType);
        owner.removeNode(toColumn);
        owner.removeNode(btnDelete);
        owner.removeNode(collection);

        rect = null;
        cmbType = null;
        toColumn = null;
        btnDelete = null;
        collection = null;

        owner.shiftStructure(index, -ROW_SPAN);
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
                    List<StructEntity> structure = owner.getStructure();
                    if (!newValue.equals(oldValue) && cmb.getValue() != null && !tf.getText().isEmpty())
                    {
                        switch (cmb.getValue())
                        {
                            case ARRAY:
                                structure.set(index, new StructEntityArray(index, tf.getText(), collection.getStructure()));
                                return;
                            case OBJECT:
                                structure.set(index, new StructEntityObject(index, tf.getText(), collection.getStructure()));
                                return;
                            default:
                                break;
                        }
                    }
                    structure.set(index, null);
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
        nodes.add(rect);
        nodes.add(cmbType);
        nodes.add(toColumn);
        nodes.add(btnDelete);
        nodes.add(collection);
        return nodes;
    }

    ComboBox<CollectionStructType> getCmbType()
    {
        return cmbType;
    }

    TextField getToColumn()
    {
        return toColumn;
    }

    void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * Returns the StructurePane.
     * @return
     */
    public StructurePane getCollection()
    {
        return collection;
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
