/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.structure;

/**
 * Interface which describe conversion rules and structure.
 * @author Asbamz
 */
public abstract class StructEntity implements Comparable<StructEntity>
{
    /**
     * Gets database id.
     * @return
     */
    public abstract int getId();

    /**
     * Gets column name.
     * @return
     */
    public abstract String getColumnName();

    @Override
    public int compareTo(StructEntity o)
    {
        if (this instanceof SimpleEntity && o instanceof CollectionEntity)
        {
            return -1;
        }
        else if (this instanceof CollectionEntity && o instanceof SimpleEntity)
        {
            return 1;
        }
        return getId() - o.getId();
    }
}
