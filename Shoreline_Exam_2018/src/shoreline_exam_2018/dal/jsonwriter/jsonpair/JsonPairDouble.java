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
public class JsonPairDouble implements JsonPair<Double>
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

}
