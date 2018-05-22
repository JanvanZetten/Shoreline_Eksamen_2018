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
public class DefaultStringRule extends Rule<String>
{
    private String defaultValue;
    private boolean isForced;

    public DefaultStringRule(String defaultValue, int columnIndex, boolean isForced)
    {
        super(columnIndex);
        this.defaultValue = defaultValue;
        this.isForced = isForced;
    }

    @Override
    public String applyRuleOn(String item)
    {
        if (isForced || item == null)
        {
            return defaultValue;
        }
        if (!item.isEmpty())
        {
            return item;
        }
        return defaultValue;
    }

    public boolean isForced()
    {
        return isForced;
    }
}
