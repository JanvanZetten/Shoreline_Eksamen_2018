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
public class DefaultDoubleRule extends Rule<Double>
{
    private Double defaultValue;

    public DefaultDoubleRule(Double defaultValue, int columnIndex)
    {
        super(columnIndex);
        this.defaultValue = defaultValue;
    }

    @Override
    public Double applyRuleOn(Double item)
    {
        if (item != null)
        {
            return item;
        }
        return defaultValue;
    }
}
