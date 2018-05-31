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
public class DefaultDoubleRule extends Rule<Double, Double> implements DefaultRule
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

    @Override
    public boolean isForced()
    {
        return isForced;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.defaultValue);
        hash = 67 * hash + (this.isForced ? 1 : 0);
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
        final DefaultDoubleRule other = (DefaultDoubleRule) obj;
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
