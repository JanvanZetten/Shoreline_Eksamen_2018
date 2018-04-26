/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.jsonwriter.jsonpair;

import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Asbamz
 */
public class JsonPairJson implements JsonPair<JSONObject>
{
    private String key;
    private JSONObject value;

    /**
     * Create JSONObject with values within list.
     * @param key
     * @param value
     */
    public JsonPairJson(String key, List<JsonPair> value)
    {
        this.key = key;
        this.value = new JSONObject();
        for (JsonPair jsonPair : value)
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
}
