/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure.entry;

import shoreline_exam_2018.be.output.structure.CollectionEntry;
import java.util.List;
import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;

/**
 * Array Collection Entity.
 * @author Asbamz
 */
public class StructEntityArray extends CollectionEntry
{
    public StructEntityArray(String columnName, List<StructEntityInterface> collection)
    {
        this.columnName = columnName;
        this.collection = collection;
        this.cst = CollectionStructType.ARRAY;
    }
}
