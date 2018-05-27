/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Asbamz
 */
public class HeaderPane extends ScrollPane
{
    private final ProfileSpecification specification;

    private GridPane gridHeader; // Grid Pane which contains column headers.

    /**
     * Constructs ScrollPane containing a GridPane of headers.
     * @param owner
     */
    public HeaderPane(StructurePane owner)
    {
        super();

        this.specification = owner.getSpecification();

        DragAndDropHandler dragAndDropHandler = specification.getDragAndDropHandler();

        gridHeader = new GridPane();
        gridHeader.setHgap(5.0);
        gridHeader.setVgap(5.0);
        gridHeader.setPadding(new Insets(5.0));

        this.setContent(gridHeader);

        // Makes ScrollPaneHeader accept drag and drop copying.
        this.setOnDragOver(dragAndDropHandler.getOnDragOverAcceptCopy());

        // So that the element dragged to a row can be dragged back and destroyed.
        this.setOnDragDropped(dragAndDropHandler.getOnHeaderPaneDropped());

        addHeaders();
    }

    /**
     * Add headers from owners header map.
     */
    private void addHeaders()
    {
        clear();
        ObservableMap<String, Entry<Integer, String>> headersIndexAndExamples = specification.getHeadersIndexAndExamples();
        HashMap<Integer, String> headersFromIndex = specification.getHeadersIndexToName();

        List<Integer> indexes = new ArrayList<>();
        indexes.addAll(headersFromIndex.keySet());
        for (Integer index : indexes)
        {
            String headerName = headersFromIndex.get(index);
            String headerExample = headersIndexAndExamples.get(headerName).getValue();
            addInputHeadersAndExamples(index, headerName, headerExample);
        }
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
        tfHeader.setOnDragDetected(specification.getDragAndDropHandler().getOnHeaderDragDetected(tfHeader));

        // Set constraints and add to GridPane.
        GridPane.setConstraints(tfHeader, 0, getRowCount());
        gridHeader.getChildren().add(tfHeader);
    }

    /**
     * Clears GridPane.
     */
    private void clear()
    {
        gridHeader.getChildren().clear();
    }

    /**
     * Count rows in gridHeader.
     * @return
     */
    private int getRowCount()
    {
        int numRows = 0;
        for (int i = 0; i < gridHeader.getChildren().size(); i++)
        {
            Node child = gridHeader.getChildren().get(i);
            Integer rowIndex = GridPane.getRowIndex(child);
            if (rowIndex != null)
            {
                numRows = Math.max(numRows, rowIndex + 1);
            }
        }
        return numRows;
    }
}
