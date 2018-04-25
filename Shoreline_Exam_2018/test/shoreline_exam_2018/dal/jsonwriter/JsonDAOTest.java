/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.jsonwriter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
     * Test of writeJsonToFile method, of class JsonDAO.
     */
    @org.junit.Test
    public void testWriteJsonToFile() throws Exception
    {
        JsonDAO jdao = new JsonDAO();
        jdao.writeJsonToFile();
    }

}
