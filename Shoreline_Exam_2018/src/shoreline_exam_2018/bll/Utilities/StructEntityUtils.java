/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.Utilities;

import java.util.List;
import shoreline_exam_2018.be.output.structure.CollectionEntity;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;

/**
 *
 * @author Asbamz
 */
public class StructEntityUtils
{
    /**
     * Checks if any entities are null.
     * @param lst
     * @return
     */
    public static boolean isAnyEntryNull(List<StructEntityInterface> lst)
    {
        for (StructEntityInterface entity : lst)
        {
            if (entity instanceof SimpleEntity)
            {
                continue;
            }
            else if (entity instanceof CollectionEntity)
            {
                CollectionEntity ce = (CollectionEntity) entity;
                if (isAnyEntryNull(ce.getCollection()))
                {
                    return true;
                }
            }
            else
            {
                return true;
            }
        }
        return false;
    }
}
