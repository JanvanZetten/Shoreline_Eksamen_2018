/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.jsonpair;

import java.util.Objects;
import shoreline_exam_2018.be.output.OutputPair;

/**
 * Is a pair of a key String and a value Double.
 * @author Asbamz
 */
public class JsonPairDouble implements OutputPair<Double>
{
    private String key;
    private Double value;

    /**
     * Create object with given value.
     * @param key
     * @param value
     */
    public JsonPairDouble(String key, Double value)
    {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public Double getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "\"" + key + "\":" + value;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.key);
        hash = 47 * hash + Objects.hashCode(this.value);
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
        final JsonPairDouble other = (JsonPairDouble) obj;
        if (!Objects.equals(this.key, other.key))
        {
            return false;
        }
        if (!Objects.equals(this.value, other.value))
        {
            return false;
        }
        return true;
    }

}
