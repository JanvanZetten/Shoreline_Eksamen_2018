/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile.RuleView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import shoreline_exam_2018.be.output.rule.DefaultRule;
import shoreline_exam_2018.be.output.structure.CollectionEntity;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.gui.model.profile.ProfileEntity;
import shoreline_exam_2018.gui.model.profile.ProfileSpecification;
import shoreline_exam_2018.be.output.structure.StructEntity;

/**
 *
 * @author Asbamz
 */
public class RulePane extends GridPane
{
    private final ProfileSpecification specification;
    private final ObservableMap<String, Map.Entry<Integer, String>> headersIndexAndExamples;
    private final HashMap<Integer, String> headersIndexToName;
    private final List<StructEntity> structure;
    private final List<ProfileEntity> structureEntities;

    /**
     * Creates GridPane containing rule elements for structure.
     * @param specification
     */
    public RulePane(ProfileSpecification specification)
    {
        super();

        this.specification = specification;
        this.headersIndexAndExamples = specification.getHeadersIndexAndExamples();
        this.headersIndexToName = specification.getHeadersIndexToName();
        this.structure = specification.getStructure();
        this.structureEntities = specification.getStructureEntities();

        setupGridPane(this, specification.isMaster() ? 5.0 : -0.1);

        if (specification.isMaster())
        {
            addHeader();
        }

        int index = 0;
        for (int i = 0; i < structure.size(); i++)
        {
            if (structure.get(i) != null)
            {
                if (structure.get(i) instanceof SimpleEntity)
                {
                    addSimpleEntity(index, (SimpleEntity) structure.get(i));
                    index++;
                }
                else if (structure.get(i) instanceof CollectionEntity)
                {
                    addCollectionEntity(index, (CollectionEntity) structure.get(i));
                    index++;
                }
            }

        }
    }

    /**
     * Setup grid.
     * @return
     */
    void setupGridPane(GridPane gridPane, double padding)
    {
        // Clear constraints and settings.
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Set gap between grid elements and padding around grid.
        gridPane.setHgap(specification.getDefaultGap());
        gridPane.setVgap(specification.getDefaultGap());
        gridPane.setPadding(new Insets(padding, padding, padding, padding));
        gridPane.setGridLinesVisible(false);
    }

    /**
     * Adds column headers.
     */
    private void addHeader()
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

        this.getChildren().addAll(lbl1, lbl2, lbl3, lbl4, lbl5);
    }

    /**
     * Adds simple entity.
     * @param index
     * @param structureIndex
     */
    private void addSimpleEntity(int index, SimpleEntity se)
    {
        RuleEntitySimple res = new RuleEntitySimple(index, se, this, specification);
        this.getChildren().addAll(res.getNodes());
    }

    /**
     * Adds collection entity.
     * @param index
     * @param structureIndex
     */
    private void addCollectionEntity(int index, CollectionEntity ce)
    {
        RuleEntityCollection rec = new RuleEntityCollection(index, ce, specification);
        this.getChildren().addAll(rec.getNode());
    }
}
