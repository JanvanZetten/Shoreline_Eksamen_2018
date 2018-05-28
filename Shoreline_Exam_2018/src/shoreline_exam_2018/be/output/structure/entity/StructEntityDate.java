/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure.entity;

import java.util.Objects;
import shoreline_exam_2018.be.output.rule.DateFormatRule;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 * Date Entity
 * @author Asbamz
 */
public class StructEntityDate extends SimpleEntity
{
    DateFormatRule dfr;

    public StructEntityDate(String columnName, int inputIndex)
    {
        this(columnName, inputIndex, null);
    }

    public StructEntityDate(String columnName, int inputIndex, DateFormatRule dfr)
    {
        this.columnName = columnName;
        this.inputIndex = inputIndex;
        this.dfr = dfr;
        sst = SimpleStructType.DATE;
    }

    public DateFormatRule getDfr()
    {
        return dfr;
    }

    public void setDfr(DateFormatRule dfr)
    {
        this.dfr = dfr;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 71 * hash + super.hashCode() + Objects.hashCode(this.dfr);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        super.equals(obj);
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
        final StructEntityDate other = (StructEntityDate) obj;
        if (!Objects.equals(this.dfr, other.dfr))
        {
            return false;
        }
        return true;
    }

}
