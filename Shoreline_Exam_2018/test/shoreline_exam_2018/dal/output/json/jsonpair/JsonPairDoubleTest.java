/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output.json.jsonpair;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.dal.output.OutputPair;

/**
 *
 * @author Asbamz
 */
public class JsonPairDoubleTest
{
    private OutputPair jpd;
    private String key;
    private Double value;

    public JsonPairDoubleTest()
    {
        key = "double";
        value = 2.0;
        jpd = new JsonPairDouble(key, value);
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
     * Test of getKey method, of class JsonPairDouble.
     */
    @Test
    public void testGetKey()
    {
        assertEquals(jpd.getKey(), key);
    }

    /**
     * Test of getValue method, of class JsonPairDouble.
     */
    @Test
    public void testGetValue()
    {
        assertEquals(jpd.getValue(), value);
    }

}
