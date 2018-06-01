/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile;

import shoreline_exam_2018.gui.model.profile.RuleView.RulePane;
import java.util.List;
import java.util.Map.Entry;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.StructEntity;

/**
 *
 * @author Asbamz
 */
public class StructurePane extends GridPane
{
    private ProfileSpecification specification;
    private ChangeListener masterListener; // Master ChangeListener to notify changes to collections owning this StructurePane.

    /**
     * Choose rather or not the ProfileGrid is master.
     * @param isMaster
     */
    public StructurePane(boolean isMaster)
    {
        this(isMaster, null);
    }

    /**
     * The master grid is the main grid.
     * @param isMaster
     * @param indent
     */
    public StructurePane(boolean isMaster, Double indent)
    {
        super();
        specification = new ProfileSpecification(isMaster, indent != null ? isMaster ? 0.0 : indent : 0.0);
        setupGridPane(specification.isMaster() ? 5.0 : -0.1);
        clearView();
    }

    /**
     * Make grid with masterListener. MasterListener to update collections.
     * @param isMaster
     * @param indent
     * @param masterListener
     */
    StructurePane(boolean isMaster, double indent, ChangeListener masterListener)
    {
        this(isMaster, indent);
        this.masterListener = masterListener;
    }

    /**
     * Sets up grid.
     * @return
     */
    private void setupGridPane(double padding)
    {
        // Clear constraints and settings.
        this.getChildren().clear();
        this.getRowConstraints().clear();
        this.getColumnConstraints().clear();

        // Set gap between grid elements and padding around grid.
        this.setHgap(specification.getDefaultGap());
        this.setVgap(specification.getDefaultGap());
        this.setPadding(new Insets(padding, padding, padding, padding));
        this.setGridLinesVisible(false);
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

        this.getChildren().addAll(lbl1, lbl2, lbl3, lbl4);
    }

    /**
     * Row with controls to add simple or collection row.
     */
    private void addRowAdder()
    {
        // ComboBox
        ObservableList<String> obsCmb = FXCollections.observableArrayList();
        obsCmb.add("Simple");
        obsCmb.add("Collection");
        ComboBox<String> cmbType = new ComboBox(obsCmb);
        cmbType.getSelectionModel().selectFirst();
        Rectangle rect = new ColourRectangle(specification.getDefaultRectangleWidth(), cmbType.heightProperty(), specification);

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

        // Add listener to check if new rows are added or removed from GridPane. If so the controls are moved to the last row.
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
        specification.setDefaultWidth(btn);
        specification.setDefaultWidth(cmbType);

        GridPane.setConstraints(rect, 0, 0);
        GridPane.setConstraints(cmbType, 1, 0);
        GridPane.setConstraints(cgp, 0, 0);
        GridPane.setConstraints(btn, 1, 0);

        cgp.getChildren().addAll(rect, cmbType);
        gp.getChildren().addAll(cgp, btn);

        // Set margin for first item and adds them to the grid.
        GridPane.setMargin(cgp, new Insets(0.0, 0.0, 0.0, specification.getIndent()));
        this.getChildren().addAll(gp);
    }

    /**
     * Add a row for simple data.
     */
    ProfileEntitySimple addSimpleRow()
    {
        ProfileEntitySimple newPES = new ProfileEntitySimple(this);
        this.getChildren().addAll(newPES.getNodes());
        return newPES;
    }

    /**
     * Add a row for collection.
     */
    ProfileEntityCollection addCollectionRow()
    {
        ProfileEntityCollection newPEC = new ProfileEntityCollection(this);
        this.getChildren().addAll(newPEC.getNodes());
        return newPEC;
    }

    /**
     * Count rows in gridpane.
     * @param pane
     * @return
     */
    int getRowCount()
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
     * Add Map from other class.
     * @param headersIndexAndExamples
     */
    public void addHashMap(ObservableMap<String, Entry<Integer, String>> headersIndexAndExamples)
    {
        specification.addHashMap(headersIndexAndExamples);
    }

    /**
     * Get Header Map.
     * @return
     */
    public ObservableMap<String, Entry<Integer, String>> getHashMap()
    {
        return specification.getHeadersIndexAndExamples();
    }

    /**
     * Clear data.
     */
    private void clearData()
    {
        specification.clearStructure();
    }

    /**
     * Clear data.
     */
    private void clearView()
    {
        clearData();
        this.getChildren().clear();
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();
        if (specification.isMaster())
        {
            addHeader();
        }
        addRowAdder();
    }

    /**
     * Shift Structure
     * @param fromIndex
     * @param shiftIdentifier
     */
    void shiftStructure(int fromIndex, int shiftIdentifier)
    {
        for (ProfileEntity profileEntity : specification.getStructureEntities())
        {
            if (profileEntity != null)
            {
                profileEntity.shiftPosition(fromIndex, shiftIdentifier);
            }
        }
        for (int j = 0; j < this.getChildren().size(); j++)
        {
            int rowIndex = GridPane.getRowIndex(this.getChildren().get(j));
            if (rowIndex > fromIndex)
            {
                int structureIndex = specification.isMaster() ? rowIndex - 1 : rowIndex;
                GridPane.setRowIndex(this.getChildren().get(j), rowIndex + shiftIdentifier);
            }
        }
    }

    /**
     * Returns Profile specification.
     * @return
     */
    ProfileSpecification getSpecification()
    {
        return specification;
    }

    /**
     * Load a structure to profile view.
     * @param structure
     */
    public void loadStructure(List<StructEntity> structure)
    {
        clearView();
        GridStructureConverter gsc = new GridStructureConverter(this);
        gsc.loadStructure(structure);
    }

    /**
     * Load Profile to the View.
     * @param profile
     */
    public void loadProfile(Profile profile)
    {
        clearView();
        loadProfile(profile.getStructure().getCollection(), profile);
    }

    /**
     * Load Structure with Profile to the View.
     * @param structure
     * @param profile
     */
    void loadProfile(List<StructEntity> structure, Profile profile)
    {
        GridProfileConverter gpc = new GridProfileConverter(this);
        gpc.loadProfile(structure, profile);
    }

    /**
     * Gets the Structure.
     * @return
     */
    public List<StructEntity> getStructure()
    {
        return specification.getStructure();
    }

    /**
     * Get master listener.
     * @return
     */
    ChangeListener getMasterListener()
    {
        return masterListener;
    }

    /**
     * Removes node from this GridPane.
     * @param node
     */
    void removeNode(Node node)
    {
        this.getChildren().remove(node);
    }

    /**
     * Creates a rule pane with the information from this Structure.
     * @return
     */
    public GridPane createRuleView()
    {
        return new RulePane(specification);
    }
}
