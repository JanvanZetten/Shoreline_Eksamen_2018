/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output.json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.dal.output.OutputDAO;
import shoreline_exam_2018.dal.output.OutputPair;
import shoreline_exam_2018.dal.output.json.jsonpair.JsonPairArray;
import shoreline_exam_2018.dal.output.json.jsonpair.JsonPairDate;
import shoreline_exam_2018.dal.output.json.jsonpair.JsonPairJson;
import shoreline_exam_2018.dal.output.json.jsonpair.JsonPairString;

/**
 *
 * @author Asbamz
 */
public class JsonDAOTest
{

    public JsonDAOTest()
    {
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
     * Test of createFile method, of class JsonDAO.
     */
    @Test
    public void testCreateFile() throws Exception
    {
        List<OutputPair> thisShouldWork = new ArrayList<>();
        thisShouldWork.add(new JsonPairString("siteName", ""));
        thisShouldWork.add(new JsonPairString("assetSerialNumber", "asset._id"));
        thisShouldWork.add(new JsonPairString("type", "SAP import field -> 'Order Type'"));
        thisShouldWork.add(new JsonPairString("externalWorkOrderId", "SAP import field -> 'Order'"));
        thisShouldWork.add(new JsonPairString("systemStatus", "SAP import field -> 'System status'"));
        thisShouldWork.add(new JsonPairString("userStatus", "SAP import field -> 'User status'"));
        thisShouldWork.add(new JsonPairDate("createdOn", Calendar.getInstance().getTime()));
        thisShouldWork.add(new JsonPairString("createdBy", "SAP"));
        thisShouldWork.add(new JsonPairString("name", "SAP import field -> 'Opr. short text' if empty then  'Description2'"));
        thisShouldWork.add(new JsonPairString("priority", "SAP import field -> 'priority' if set else 'Low'"));
        thisShouldWork.add(new JsonPairString("status", "NEW"));

        List<OutputPair> oioArr = new ArrayList<>();
        oioArr.add(new JsonPairString("latestFinishDate", "Datetime Object"));
        oioArr.add(new JsonPairString("earliestStartDate", "Datetime Object"));
        oioArr.add(new JsonPairString("latestStartDate", "Datetime Object"));
        oioArr.add(new JsonPairString("estimatedTime", ""));
        OutputPair oio = new JsonPairJson("planning", oioArr);
        thisShouldWork.add(oio);

        JsonPairJson jsonObj = new JsonPairJson("jsonObject", thisShouldWork);

        List<OutputPair> jpArr = new ArrayList<>();
        jpArr.add(jsonObj);

        OutputDAO jdao = new JsonDAO();
        jdao.createFile(jpArr, Paths.get(System.getProperty("user.dir") + "/test.json"));
        JsonPairArray jsonArr = new JsonPairArray("jsonArray", jpArr);

        JSONParser parser = new JSONParser();

        try (FileReader file = new FileReader(System.getProperty("user.dir") + "/test.json"))
        {
            Object obj = parser.parse(file);

            JSONArray jsonObject = (JSONArray) obj;
            assertTrue(jsonObject.toJSONString().equals(jsonArr.getValue().toJSONString()));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + "/test.json"));
        }
    }

}
