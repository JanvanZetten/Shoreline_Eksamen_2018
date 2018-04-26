/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output.json.jsonpair;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.dal.output.OutputPair;

/**
 *
 * @author Asbamz
 */
public class JsonPairDateTest
{
    private OutputPair jpd;
    private String key;
    private String value;

    public JsonPairDateTest()
    {
        Date date = Calendar.getInstance().getTime();

        key = "date";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SXXX");
        TimeZone tz = TimeZone.getDefault();
        df.setTimeZone(tz);
        value = df.format(date);

        jpd = new JsonPairDate(key, date);
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    /**
     * Test of getKey method, of class JsonPairDate.
     */
    @Test
    public void testGetKey()
    {
        assertEquals(jpd.getKey(), key);
    }

    /**
     * Test of getValue method, of class JsonPairDate.
     */
    @Test
    public void testGetValue()
    {
        assertEquals(jpd.getValue(), value);
    }

}
