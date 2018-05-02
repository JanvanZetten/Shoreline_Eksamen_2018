/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure;

import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 * Entry for simple data.
 * @author Asbamz
 */
public abstract class SimpleEntry implements StructEntityInterface
{
    protected String columnName;
    protected int inputIndex;
    protected SimpleStructType sst;

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
}
