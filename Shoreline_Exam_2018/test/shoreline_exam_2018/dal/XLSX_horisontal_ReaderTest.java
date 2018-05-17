/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.be.InputObject;

/**
 *
 * @author janvanzetten
 */
public class XLSX_horisontal_ReaderTest {
    
    private static XLSX_horisontal_Reader reader;
    
    public XLSX_horisontal_ReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        reader = new XLSX_horisontal_Reader("test/shoreline_exam_2018/MockTilJunitTest.xlsx");
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getParameters method, of class XLSX_horisontal_Reader.
     */
    @Test
    public void testGetParameters() throws Exception {
        List<String> strings = reader.getParameters();
        
        assertEquals("Test", strings.get(0));
        assertEquals("1.0", strings.get(1));
        assertEquals("1+2", strings.get(2));
        assertEquals("", strings.get(3));
        assertEquals("Det virker", strings.get(4));
    }

    /**
     * Test of hasNext method, of class XLSX_horisontal_Reader.
     */
    @Test
    public void testHasNext() throws Exception {
        reader = new XLSX_horisontal_Reader("test/shoreline_exam_2018/MockTilJunitTest.xlsx");
        assertEquals(true, reader.hasNext());
    }

    /**
     * Test of getNext method, of class XLSX_horisontal_Reader.
     */
    @Test
    public void testGetNextRow() throws Exception {
        for (int i = 0; i < 2; i++) {
        InputObject currentrow = reader.getNext();
        assertEquals(1, currentrow.getField(0).getDoubleValue(), 0.01);
        assertEquals(2, currentrow.getField(1).getDoubleValue(), 0.01);
        assertEquals(null, currentrow.getField(2).getValue());
        assertEquals(5, currentrow.getField(3).getDoubleValue(), 0.01);
        assertEquals(6, currentrow.getField(4).getDoubleValue(), 0.01);
        }
        
    }
    
}
