/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.jsonpair;

import shoreline_exam_2018.be.output.jsonpair.JsonPairInteger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.be.output.OutputPair;

/**
 *
 * @author Asbamz
 */
public class JsonPairIntegerTest
{
    private OutputPair jpi;
    private String key;
    private Integer value;

    public JsonPairIntegerTest()
    {
        key = "integer";
        value = 3;
        jpi = new JsonPairInteger(key, value);
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
     * Test of getKey method, of class JsonPairInteger.
     */
    @Test
    public void testGetKey()
    {
        assertEquals(jpi.getKey(), key);
    }

    /**
     * Test of getValue method, of class JsonPairInteger.
     */
    @Test
    public void testGetValue()
    {
        assertEquals(jpi.getValue(), value);
    }

}
