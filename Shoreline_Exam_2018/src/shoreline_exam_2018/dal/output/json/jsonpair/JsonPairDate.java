/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output.json.jsonpair;

import java.text.SimpleDateFormat;
import shoreline_exam_2018.dal.output.OutputPair;
import java.util.Date;
import java.util.TimeZone;

/**
 *
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
        this.value = dateToISO8601(value);
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

    /**
     * Converts Java data to JSON ISO8601 format.
     * @param date
     * @return
     */
    private String dateToISO8601(Date date)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXXX");
        TimeZone tz = TimeZone.getDefault();
        df.setTimeZone(tz);
        return df.format(date);
    }
}
