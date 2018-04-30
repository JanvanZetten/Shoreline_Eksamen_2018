/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure;

import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import java.util.List;
import shoreline_exam_2018.be.output.structure.entry.StructEntryInterface;

/**
 *
 * @author Asbamz
 */
public abstract class CollectionEntry implements StructEntryInterface
{
    protected String columnName;
    protected List<StructEntryInterface> collection;
    protected CollectionStructType cst;

    @Override
    public String getColumnName()
    {
        return columnName;
    }

    public List<StructEntryInterface> getCollection()
    {
        return collection;
    }

    public CollectionStructType getCST()
    {
        return cst;
    }
}
