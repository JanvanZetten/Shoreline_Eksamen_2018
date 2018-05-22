/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.rule;

import java.util.Date;

/**
 *
 * @author Asbamz
 */
public class DefaultDateRule extends Rule<Date, Date>
{
    private Date defaultValue;
    private boolean isForced;

    public DefaultDateRule(Date defaultValue, int columnIndex, boolean isForced)
    {
        super(columnIndex);
        this.defaultValue = defaultValue;
        this.isForced = isForced;
    }

    @Override
    public Date applyRuleOn(Date item)
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
