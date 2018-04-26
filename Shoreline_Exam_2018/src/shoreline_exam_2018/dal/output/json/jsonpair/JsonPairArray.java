/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output.json.jsonpair;

import shoreline_exam_2018.dal.output.OutputPair;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
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
}