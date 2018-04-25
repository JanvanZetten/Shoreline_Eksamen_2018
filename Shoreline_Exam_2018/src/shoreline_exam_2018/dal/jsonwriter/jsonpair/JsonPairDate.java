/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.jsonwriter.jsonpair;

import java.util.Date;

/**
 *
 * @author Asbamz
 */
public class JsonPairDate implements JsonPair<Date>
{
    private String key;
    private Date value;

    /**
     * Create object with given value.
     * @param key
     * @param value
     */
    public JsonPairDate(String key, Date value)
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
    public Date getValue()
    {
        return value;
    }
}
