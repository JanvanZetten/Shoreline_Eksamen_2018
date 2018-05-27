/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile.RuleView;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import shoreline_exam_2018.be.output.structure.CollectionEntity;
import shoreline_exam_2018.gui.model.profile.ProfileEntity;
import shoreline_exam_2018.gui.model.profile.ProfileEntityCollection;
import shoreline_exam_2018.gui.model.profile.ProfileSpecification;

/**
 *
 * @author Asbamz
 */
public class RuleEntityCollection
{
    private final ProfileSpecification specification;

    private Node node;

    /**
     * Gets RuleView from collection.
     * @param index
     * @param ce
     * @param specification
     */
    public RuleEntityCollection(int index, CollectionEntity ce, ProfileSpecification specification)
    {
        this.specification = specification;
        int structureIndex = specification.getStructure().indexOf(ce);
        ProfileEntity pe = specification.getStructureEntities().get(structureIndex);

        if (pe != null)
        {
            if (pe instanceof ProfileEntityCollection)
            {
                ProfileEntityCollection pec = (ProfileEntityCollection) specification.getStructureEntities().get(structureIndex);
                node = pec.getCollection().createRuleView();

                // Set margin for first element.
                GridPane.setMargin(node, new Insets(0.0, 0.0, 0.0, specification.isMaster() ? specification.getDefaultIndent() : specification.getIndent()));
                GridPane.setConstraints(node, 0, specification.isMaster() ? index + 1 : index, 6, 1);

                index++;
            }
        }
    }

    /**
     * Get node.
     * @return
     */
    Node getNode()
    {
        return node;
    }

}
