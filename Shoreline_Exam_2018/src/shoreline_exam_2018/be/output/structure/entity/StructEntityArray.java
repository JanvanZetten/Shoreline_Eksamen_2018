/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure.entity;

import shoreline_exam_2018.be.output.structure.CollectionEntity;
import java.util.List;
import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import shoreline_exam_2018.be.output.structure.StructEntity;

/**
 * Array Collection Entity.
 * @author Asbamz
 */
public class StructEntityArray extends CollectionEntity
{
    public StructEntityArray(int id, String columnName, List<StructEntity> collection)
    {
        this.id = id;
        this.columnName = columnName;
        this.collection = collection;
        this.cst = CollectionStructType.ARRAY;
    }
}
