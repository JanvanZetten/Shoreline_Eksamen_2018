/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.entry.*;


/**
 *
 * @author janvanzetten
 */
public class ConvertXlsxToJsonTest {
    
    private static ConvertXlsxToJson converter;
    
    public ConvertXlsxToJsonTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        converter = new ConvertXlsxToJson();
    }
    
    @AfterClass
    public static void tearDownClass() {
        converter = null;
    }

    /**
     * Test of convertFile method, of class ConvertXlsxToJson.
     */
    @Test
    public void testConvertFile() throws Exception {
        List<StructEntityInterface> listOfEntries = new ArrayList<>();
        listOfEntries.add(new StructEntityInteger("first", 0));
        listOfEntries.add(new StructEntityInteger("second", 1));
        listOfEntries.add(new StructEntityInteger("last", 4));

        StructEntityObject structObject = new StructEntityObject("", listOfEntries);
        Profile profile = new Profile(0, "test", structObject, "TestClass");
        converter.convertFile(profile, Paths.get("test/shoreline_exam_2018/MockTilJunitTest.xlsx"), Paths.get("test/shoreline_exam_2018/TestJson.json"));
        
        System.out.println("Check the output file: TestJson.json if it contains the data: \n"
                + "[{\"last\":6,\"first\":1,\"second\":2},{\"last\":6,\"first\":1,\"second\":2}]\n"
                + "Or something similar");
        
    }
    
}
