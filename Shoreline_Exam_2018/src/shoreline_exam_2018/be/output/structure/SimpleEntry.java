/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure;

import shoreline_exam_2018.be.output.structure.entry.StructEntryInterface;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 *
 * @author Asbamz
 */
public abstract class SimpleEntry implements StructEntryInterface
{
    protected String columnName;
    protected int inputIndex;
    protected SimpleStructType sst;

    @Override
    public String getColumnName()
    {
        return columnName;
    }

    public int getInputIndex()
    {
        return inputIndex;
    }

    public SimpleStructType getSST()
    {
        return sst;
    }

    @Override
    public String toString()
    {
        return columnName + ":" + sst.name();
    }
}
