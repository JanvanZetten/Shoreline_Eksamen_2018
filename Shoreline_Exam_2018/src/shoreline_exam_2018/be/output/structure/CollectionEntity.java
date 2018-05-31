/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure;

import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import java.util.List;
import java.util.Objects;

/**
 * Entry for Collections.
 * @author Asbamz
 */
public abstract class CollectionEntity extends StructEntity
{
    protected int id;
    protected String columnName;
    protected List<StructEntity> collection;
    protected CollectionStructType cst;

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * Gets collection of entities.
     * @return
     */
    public List<StructEntity> getCollection()
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
        for (StructEntity structEntryInterface : collection)
        {
            str += "    " + structEntryInterface.toString() + "\n";
        }
        return str;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.columnName);
        hash = 41 * hash + Objects.hashCode(this.collection);
        hash = 41 * hash + Objects.hashCode(this.cst);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final CollectionEntity other = (CollectionEntity) obj;
        if (!Objects.equals(this.columnName, other.columnName))
        {
            return false;
        }
        if (!Objects.equals(this.collection, other.collection))
        {
            return false;
        }
        if (this.cst != other.cst)
        {
            return false;
        }
        return true;
    }
}
