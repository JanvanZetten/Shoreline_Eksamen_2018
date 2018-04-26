/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output.json;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.output.OutputDAO;
import shoreline_exam_2018.be.output.jsonpair.JsonPairArray;
import shoreline_exam_2018.be.output.jsonpair.JsonPairJson;
import shoreline_exam_2018.be.output.jsonpair.JsonPairString;
import shoreline_exam_2018.be.output.OutputPair;

/**
 *
 * @author Asbamz
 */
public class JsonDAO implements OutputDAO
{
    @Override
    public void createFile(List<OutputPair> entities, Path outputPath) throws DALException
    {
        JsonPairArray jsonArr = new JsonPairArray("jsonArray", entities);

        try (FileWriter file = new FileWriter(outputPath.toFile()))
        {
            file.write(jsonArr.getValue().toJSONString());
            file.flush();
        }
        catch (IOException e)
        {
            throw new DALException(e.getMessage(), e.getCause());
        }
    }
}
