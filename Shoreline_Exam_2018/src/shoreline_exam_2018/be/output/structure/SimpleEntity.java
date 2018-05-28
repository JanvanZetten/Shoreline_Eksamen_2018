/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure;

import java.util.Objects;
import shoreline_exam_2018.be.output.rule.Rule;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 * Entry for simple data.
 * @author Asbamz
 */
public abstract class SimpleEntity implements StructEntityInterface
{
    protected String columnName;
    protected int inputIndex;
    protected SimpleStructType sst;
    protected Rule defaultValue;

    @Override
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * Gets wanted index of column in input file.
     * @return
     */
    public int getInputIndex()
    {
        return inputIndex;
    }

    /**
     * Gets datatype.
     * @return
     */
    public SimpleStructType getSST()
    {
        return sst;
    }

    @Override
    public String toString()
    {
        return inputIndex + "@" + columnName + ":" + sst.name();
    }

    public Rule getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(Rule defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.columnName);
        hash = 59 * hash + this.inputIndex;
        hash = 59 * hash + Objects.hashCode(this.sst);
        hash = 59 * hash + Objects.hashCode(this.defaultValue);
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
        final SimpleEntity other = (SimpleEntity) obj;
        if (this.inputIndex != other.inputIndex)
        {
            return false;
        }
        if (!Objects.equals(this.columnName, other.columnName))
        {
            return false;
        }
        if (this.sst != other.sst)
        {
            return false;
        }
        if (!Objects.equals(this.defaultValue, other.defaultValue))
        {
            return false;
        }
        return true;
    }

}
