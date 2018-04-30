/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure.entry;

import shoreline_exam_2018.be.output.structure.SimpleEntry;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 *
 * @author Asbamz
 */
public class StructEntryDate extends SimpleEntry
{
    public StructEntryDate(String columnName, int inputIndex)
    {
        this.columnName = columnName;
        this.inputIndex = inputIndex;
        sst = SimpleStructType.DATE;
    }
}
