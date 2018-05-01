/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure.entry;

import shoreline_exam_2018.be.output.structure.SimpleEntry;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 * Double Entity.
 * @author Asbamz
 */
public class StructEntityDouble extends SimpleEntry
{
    public StructEntityDouble(String columnName, int inputIndex)
    {
        this.columnName = columnName;
        this.inputIndex = inputIndex;
        sst = SimpleStructType.DOUBLE;
    }
}
