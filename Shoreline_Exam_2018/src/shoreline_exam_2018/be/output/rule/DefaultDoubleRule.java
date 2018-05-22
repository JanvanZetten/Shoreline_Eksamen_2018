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
public class DefaultDoubleRule extends Rule<Double, Double>
{
    private Double defaultValue;
    private boolean isForced;

    public DefaultDoubleRule(Double defaultValue, int columnIndex, boolean isForced)
    {
        super(columnIndex);
        this.defaultValue = defaultValue;
        this.isForced = isForced;
    }

    @Override
    public Double applyRuleOn(Double item)
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
