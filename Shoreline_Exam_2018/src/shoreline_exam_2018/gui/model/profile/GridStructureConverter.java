/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile;

import java.util.List;
import shoreline_exam_2018.be.output.structure.CollectionEntity;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 *
 * @author Asbamz
 */
public class GridStructureConverter
{
    private final StructurePane owner;

    /**
     * From structure to grid converter.
     * @param owner
     */
    public GridStructureConverter(StructurePane owner)
    {
        this.owner = owner;
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
        ProfileEntitySimple pes = owner.addSimpleRow();
        pes.getToColumn().setText(se.getColumnName());
        switch (se.getSST())
        {
            case DATE:
                pes.getCmbType().getSelectionModel().select(SimpleStructType.DATE);
                break;
            case DOUBLE:
                pes.getCmbType().getSelectionModel().select(SimpleStructType.DOUBLE);
                break;
            case INTEGER:
                pes.getCmbType().getSelectionModel().select(SimpleStructType.INTEGER);
                break;
            case STRING:
                pes.getCmbType().getSelectionModel().select(SimpleStructType.STRING);
                break;
            default:
                pes.getCmbType().getSelectionModel().selectFirst();
                break;
        }
    }

    /**
     * Setup collection structure and add collection to new StructurePane.
     * @param ce
     */
    private void loadCollectionStructure(CollectionEntity ce)
    {
        ProfileEntityCollection pec = owner.addCollectionRow();
        pec.getToColumn().setText(ce.getColumnName());
        switch (ce.getCST())
        {
            case ARRAY:
                pec.getCmbType().getSelectionModel().select(CollectionStructType.ARRAY);
                break;
            case OBJECT:
                pec.getCmbType().getSelectionModel().select(CollectionStructType.OBJECT);
                break;
            default:
                pec.getCmbType().getSelectionModel().selectFirst();
                break;
        }
        pec.getCollection().loadStructure(ce.getCollection());
    }
}
