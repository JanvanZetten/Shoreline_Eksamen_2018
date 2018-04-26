/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A utility to convert dates to several outputs.
 * @author Asbamz
 */
public class DateUtils
{
    /**
     * Converts Java data to JSON ISO8601 format.
     * @param date
     * @return
     */
    public String dateToISO8601(Date date)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXXX");
        TimeZone tz = TimeZone.getDefault();
        df.setTimeZone(tz);
        return df.format(date);
    }
}
