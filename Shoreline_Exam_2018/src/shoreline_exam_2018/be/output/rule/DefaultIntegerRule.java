/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.rule;

/**
 *
 * @author Asbamz
 */
public class DefaultIntegerRule extends Rule<Integer, Integer>
{
    private Integer defaultValue;
    private boolean isForced;

    public DefaultIntegerRule(Integer defaultValue, int columnIndex, boolean isForced)
    {
        super(columnIndex);
        this.defaultValue = defaultValue;
        this.isForced = isForced;
    }

    @Override
    public Integer applyRuleOn(Integer item)
    {
        if (isForced || item == null)
        {
            return defaultValue;
        }

        return item;
    }

    public boolean isForced()
    {
        return isForced;
    }
}
