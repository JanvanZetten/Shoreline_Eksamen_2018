/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure;

import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import java.util.List;

/**
 * Entry for Collections.
 * @author Asbamz
 */
public abstract class CollectionEntry implements StructEntityInterface
{
    protected String columnName;
    protected List<StructEntityInterface> collection;
    protected CollectionStructType cst;

    @Override
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * Gets collection of entities.
     * @return
     */
    public List<StructEntityInterface> getCollection()
    {
        return collection;
    }

    /**
     * Get datatype.
     * @return
     */
    public CollectionStructType getCST()
    {
        return cst;
    }

    @Override
    public String toString()
    {
        String str = "";
        str += columnName + ":" + cst + "\n";
        for (StructEntityInterface structEntryInterface : collection)
        {
            str += "    " + structEntryInterface.toString() + "\n";
        }
        return str;
    }

}
