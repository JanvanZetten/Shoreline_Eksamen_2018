/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.jsonwriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.jsonwriter.jsonpair.JsonPair;
import shoreline_exam_2018.dal.jsonwriter.jsonpair.JsonPairArray;
import shoreline_exam_2018.dal.jsonwriter.jsonpair.JsonPairJson;
import shoreline_exam_2018.dal.jsonwriter.jsonpair.JsonPairString;

/**
 *
 * @author Asbamz
 */
public class JsonDAO
{
    /**
     * Testing JSON write
     * @throws DALException
     */
    public void writeJsonToFile() throws DALException
    {
        List<JsonPair> thisShouldWork = new ArrayList<>();
        thisShouldWork.add(new JsonPairString("siteName", ""));
        thisShouldWork.add(new JsonPairString("assetSerialNumber", "asset._id"));
        thisShouldWork.add(new JsonPairString("type", "SAP import field -> 'Order Type'"));
        thisShouldWork.add(new JsonPairString("externalWorkOrderId", "SAP import field -> 'Order'"));
        thisShouldWork.add(new JsonPairString("systemStatus", "SAP import field -> 'System status'"));
        thisShouldWork.add(new JsonPairString("userStatus", "SAP import field -> 'User status'"));
        thisShouldWork.add(new JsonPairString("createdOn", "Datetime Object (Date now)"));
        thisShouldWork.add(new JsonPairString("createdBy", "SAP"));
        thisShouldWork.add(new JsonPairString("name", "SAP import field -> 'Opr. short text' if empty then  'Description2'"));
        thisShouldWork.add(new JsonPairString("priority", "SAP import field -> 'priority' if set else 'Low'"));
        thisShouldWork.add(new JsonPairString("status", "NEW"));

        List<JsonPair> oioArr = new ArrayList<>();
        oioArr.add(new JsonPairString("latestFinishDate", "Datetime Object"));
        oioArr.add(new JsonPairString("earliestStartDate", "Datetime Object"));
        oioArr.add(new JsonPairString("latestStartDate", "Datetime Object"));
        oioArr.add(new JsonPairString("estimatedTime", ""));
        JsonPair oio = new JsonPairJson("planning", oioArr);
        thisShouldWork.add(oio);

        JsonPairJson jsonObj = new JsonPairJson("jsonObject", thisShouldWork);

        List<JsonPair> jpArr = new ArrayList<>();
        jpArr.add(jsonObj);

        JsonPairArray jsonArr = new JsonPairArray("jsonArray", jpArr);

        try (FileWriter file = new FileWriter("e:\\test.json"))
        {

            file.write(jsonArr.getValue().toJSONString());
            file.flush();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
