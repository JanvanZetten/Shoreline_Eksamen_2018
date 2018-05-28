/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.jsonpair;

import shoreline_exam_2018.be.output.OutputPair;
import java.util.List;
import java.util.Objects;
import org.json.simple.JSONObject;

/**
 * Is a pair of a key String and a value JSONObject. If given the key "object"
 * and the list { OutputPair("1", "test"), OutputPair("2", "fix") } It will
 * create an Object as this "object": {"1": "test", "2": "fix"}
 * @author Asbamz
 */
public class JsonPairJson implements OutputPair<JSONObject>
{
    private String key;
    private JSONObject value;

    /**
     * Create JSONObject with values within list.
     * @param key
     * @param value
     */
    public JsonPairJson(String key, List<OutputPair> value)
    {
        this.key = key;
        this.value = new JSONObject();
        for (OutputPair jsonPair : value)
        {
            this.value.put(jsonPair.getKey(), jsonPair.getValue());
        }
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public JSONObject getValue()
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
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.key);
        hash = 53 * hash + Objects.hashCode(this.value);
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
        final JsonPairJson other = (JsonPairJson) obj;
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
