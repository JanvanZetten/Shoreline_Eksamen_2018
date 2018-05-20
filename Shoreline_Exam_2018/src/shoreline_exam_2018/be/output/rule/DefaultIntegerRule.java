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
public class DefaultIntegerRule extends Rule<Integer>
{
    private Integer defaultValue;

    public DefaultIntegerRule(Integer defaultValue, int columnIndex)
    {
        super(columnIndex);
        this.defaultValue = defaultValue;
    }

    @Override
    public Integer applyRuleOn(Integer item)
    {
        if (item != null)
        {
            return item;
        }
        return defaultValue;
    }
}
