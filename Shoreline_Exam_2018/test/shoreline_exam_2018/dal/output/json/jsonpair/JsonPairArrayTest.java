/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output.json.jsonpair;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.dal.output.OutputPair;

/**
 *
 * @author Asbamz
 */
public class JsonPairArrayTest
{
    private OutputPair jpa;
    private String key;
    private JSONArray value;

    public JsonPairArrayTest()
    {
        String first = "1";
        String second = "2";

        key = "array";

        value = new JSONArray();
        value.add(first);
        value.add(second);

        List<OutputPair> testArr = new ArrayList<>();
        testArr.add(new JsonPairString("et", first));
        testArr.add(new JsonPairString("to", second));
        jpa = new JsonPairArray(key, testArr);
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
     * Test of getKey method, of class JsonPairArray.
     */
    @Test
    public void testGetKey()
    {
        assertEquals(jpa.getKey(), key);
    }

    /**
     * Test of getValue method, of class JsonPairArray.
     */
    @Test
    public void testGetValue()
    {
        assertEquals(jpa.getValue(), value);
    }

}
