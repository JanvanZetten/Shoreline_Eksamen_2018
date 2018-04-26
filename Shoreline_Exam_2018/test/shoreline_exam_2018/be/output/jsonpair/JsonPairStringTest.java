/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.jsonpair;

import shoreline_exam_2018.be.output.jsonpair.JsonPairString;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.be.output.OutputPair;

/**
 *
 * @author Asbamz
 */
public class JsonPairStringTest
{
    private OutputPair jps;
    private String key;
    private String value;

    public JsonPairStringTest()
    {
        key = "string";
        value = "value";
        jps = new JsonPairString(key, value);
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
     * Test of getKey method, of class JsonPairString.
     */
    @Test
    public void testGetKey()
    {
        assertEquals(jps.getKey(), key);
    }

    /**
     * Test of getValue method, of class JsonPairString.
     */
    @Test
    public void testGetValue()
    {
        assertEquals(jps.getValue(), value);
    }

}
