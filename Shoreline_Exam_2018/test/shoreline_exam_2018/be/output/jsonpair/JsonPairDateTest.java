/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.jsonpair;

import java.util.Calendar;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.bll.Utilities.DateUtils;

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

        DateUtils du = new DateUtils();
        value = du.dateToISO8601(date);

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
