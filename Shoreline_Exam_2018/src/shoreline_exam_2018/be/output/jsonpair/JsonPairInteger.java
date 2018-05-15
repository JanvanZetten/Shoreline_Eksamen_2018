/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.jsonpair;

import shoreline_exam_2018.be.output.OutputPair;

/**
 * Is a pair of a key String and a value Integer.
 * @author Asbamz
 */
public class JsonPairInteger implements OutputPair<Integer>
{
    private String key;
    private Integer value;

    /**
     * Create object with given value.
     * @param key
     * @param value
     */
    public JsonPairInteger(String key, Integer value)
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
    public Integer getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "\"" + key + "\":" + value;
    }
}
