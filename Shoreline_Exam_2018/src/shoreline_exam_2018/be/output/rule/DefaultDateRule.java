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
public class DefaultDateRule extends Rule<Date>
{
    private Date defaultValue;

    public DefaultDateRule(Date defaultValue, int columnIndex)
    {
        super(columnIndex);
        this.defaultValue = defaultValue;
    }

    @Override
    public Date applyRuleOn(Date item)
    {
        if (item != null)
        {
            return item;
        }
        return defaultValue;
    }
}
