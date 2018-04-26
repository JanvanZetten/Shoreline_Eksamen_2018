/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output.json.jsonpair;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.dal.output.OutputPair;

/**
 *
 * @author Asbamz
 */
public class JsonPairJsonTest
{
    private OutputPair jpj;
    private String key;
    private JSONObject value;

    public JsonPairJsonTest()
    {

        String first = "first";
        String second = "second";
        String first2 = "1";
        String second2 = "2";

        key = "jsonobject";

        value = new JSONObject();
        value.put(first, first2);
        value.put(second, second2);

        List<OutputPair> testArr = new ArrayList<>();
        testArr.add(new JsonPairString(first, first2));
        testArr.add(new JsonPairString(second, second2));

        jpj = new JsonPairJson(key, testArr);
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
     * Test of getKey method, of class JsonPairJson.
     */
    @Test
    public void testGetKey()
    {
        assertEquals(jpj.getKey(), key);
    }

    /**
     * Test of getValue method, of class JsonPairJson.
     */
    @Test
    public void testGetValue()
    {
        assertEquals(jpj.getValue(), value);
    }

}
