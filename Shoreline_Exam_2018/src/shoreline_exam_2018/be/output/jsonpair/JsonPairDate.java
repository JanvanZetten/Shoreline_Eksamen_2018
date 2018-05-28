/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.jsonpair;

import shoreline_exam_2018.be.output.OutputPair;
import java.util.Date;
import java.util.Objects;
import shoreline_exam_2018.bll.Utilities.DateUtils;

/**
 * Is a pair of a key String and a value with right JavaScript Date formatting.
 * @author Asbamz
 */
public class JsonPairDate implements OutputPair<String>
{
    private String key;
    private String value;

    /**
     * Create object with given value.
     * @param key
     * @param value
     */
    public JsonPairDate(String key, Date value)
    {
        this.key = key;

        DateUtils du = new DateUtils();
        this.value = du.dateToISO8601(value);
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "\"" + key + "\":\"" + value + "\"";
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.key);
        hash = 67 * hash + Objects.hashCode(this.value);
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
        final JsonPairDate other = (JsonPairDate) obj;
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
