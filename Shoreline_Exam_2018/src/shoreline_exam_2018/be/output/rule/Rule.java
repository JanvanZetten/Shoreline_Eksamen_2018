/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.rule;

import java.lang.reflect.ParameterizedType;

/**
 *
 * @author Asbamz
 * @param <T>
 */
public abstract class Rule<T>
{
    protected int columnIndex;

    /**
     * Set column index.
     * @param columnIndex
     */
    public Rule(int columnIndex)
    {
        this.columnIndex = columnIndex;
    }

    /**
     * Applies rule to item and return result.
     * @param item
     * @return
     */
    public abstract T applyRuleOn(T item);

    /**
     * Get index of the column the rules fits to.
     * @return
     */
    public int getColumnIndex()
    {
        return columnIndex;
    }
}
