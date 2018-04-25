/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.jsonwriter.jsonpair;

/**
 *
 * @author Asbamz
 */
public class JsonPairInteger implements JsonPair<Integer>
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

}
