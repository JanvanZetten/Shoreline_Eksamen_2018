/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure.entity;

import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;

/**
 * String Entity.
 * @author Asbamz
 */
public class StructEntityString extends SimpleEntity
{
    public StructEntityString(int id, String columnName, int inputIndex)
    {
        this.id = id;
        this.columnName = columnName;
        this.inputIndex = inputIndex;
        sst = SimpleStructType.STRING;
    }
}
