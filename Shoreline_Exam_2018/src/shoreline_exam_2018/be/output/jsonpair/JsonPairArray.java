/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.jsonpair;

import shoreline_exam_2018.be.output.OutputPair;
import java.util.List;
import java.util.Objects;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Is a pair of a key String and a value JSONArray. If given the key "array" and
 * the list { OutputPair("1", "test"), OutputPair("2", "fix") } It will create
 * an array as this "array": ["test", "fix"]
 * @author Asbamz
 */
public class JsonPairArray implements OutputPair<JSONArray>
{
    private String key;
    private JSONArray value;

    /**
     * Create JSONArray with values within list.
     * @param key
     * @param value
     */
    public JsonPairArray(String key, List<OutputPair> value)
    {
        this.key = key;
        this.value = new JSONArray();
        JSONObject jo = new JSONObject();
        for (OutputPair jsonPair : value)
        {
            this.value.add(jsonPair.getValue());
        }
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public JSONArray getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return value.toJSONString();
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.key);
        hash = 29 * hash + Objects.hashCode(this.value);
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
        final JsonPairArray other = (JsonPairArray) obj;
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
