/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.rule;

import java.util.Objects;

/**
 *
 * @author Asbamz
 */
public class DefaultIntegerRule extends Rule<Integer, Integer> implements DefaultRule
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

    @Override
    public boolean isForced()
    {
        return isForced;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.defaultValue);
        hash = 23 * hash + (this.isForced ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final DefaultIntegerRule other = (DefaultIntegerRule) obj;
        if (this.isForced != other.isForced)
        {
            return false;
        }
        if (!Objects.equals(this.defaultValue, other.defaultValue))
        {
            return false;
        }
        return true;
    }

}
