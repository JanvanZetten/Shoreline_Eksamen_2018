/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure.entry;

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
}
