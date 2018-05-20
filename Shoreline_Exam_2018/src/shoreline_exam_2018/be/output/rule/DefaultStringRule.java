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

    public DefaultStringRule(String defaultValue, int columnIndex)
    {
        super(columnIndex);
        this.defaultValue = defaultValue;
    }

    @Override
    public String applyRuleOn(String item)
    {
        if (item != null)
        {
            if (item.isEmpty())
            {
                return item;
            }
        }
        return defaultValue;
    }
}
