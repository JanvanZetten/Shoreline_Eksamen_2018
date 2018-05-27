/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.filereader;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.be.input.InputObject;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author janvanzetten
 */
public class XLSX_horisontal_Reader_for_Big_DocumentsTest {

    private static XLSX_horisontal_Reader_for_Big_Documents reader;

    public XLSX_horisontal_Reader_for_Big_DocumentsTest() {

    }

    @BeforeClass
    public static void setUpClass() {
        try {
            reader = new XLSX_horisontal_Reader_for_Big_Documents("test/shoreline_exam_2018/MockTilJunitTest.xlsx");
        } catch (DALException ex) {
            Logger.getLogger(XLSX_horisontal_Reader_for_Big_DocumentsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of getParameters method, of class
     * XLSX_horisontal_Reader_for_Big_Documents.
     */
    @Test
    public void testGetParameters() throws Exception {
        reader = new XLSX_horisontal_Reader_for_Big_Documents("test/shoreline_exam_2018/MockTilJunitTest.xlsx");
        List<String> strings = reader.getParameters();

        assertEquals("Test", strings.get(0));
        assertEquals("1.0", strings.get(1));
        assertEquals("1+2", strings.get(2));
        assertEquals("", strings.get(3));
        assertEquals("Det virker", strings.get(4));
    }

    /**
     * Test of hasNext method, of class
     * XLSX_horisontal_Reader_for_Big_Documents.
     */
    @Test
    public void testHasNext() throws Exception {
        reader = new XLSX_horisontal_Reader_for_Big_Documents("test/shoreline_exam_2018/MockTilJunitTest.xlsx");
        assertEquals(true, reader.hasNext());
    }

    /**
     * Test of getNext method, of class
 XLSX_horisontal_Reader_for_Big_Documents.
     */
    @Test
    public void testGetNextRow() throws Exception {
        reader = new XLSX_horisontal_Reader_for_Big_Documents("test/shoreline_exam_2018/MockTilJunitTest.xlsx");
        for (int i = 0; i < 2; i++) {
            InputObject currentObject = reader.getNext();
            assertEquals(1, currentObject.getField(0).getDoubleValue(), 0.01);
            assertEquals(2, currentObject.getField(1).getDoubleValue(), 0.01);
            assertEquals(null, currentObject.getField(2).getValue());
            assertEquals(5, currentObject.getField(3).getDoubleValue(), 0.01);
            assertEquals(6, currentObject.getField(4).getDoubleValue(), 0.01);
        }
    }
    
    @Test
    public void timeOutTest() throws Exception{
        reader = new XLSX_horisontal_Reader_for_Big_Documents("test/shoreline_exam_2018/MockTilJuintMedForskelligData.xlsx");
        InputObject currentrow = reader.getNext();
        assertEquals(1, currentrow.getField(0).getDoubleValue(), 0.01);
        assertEquals(2, currentrow.getField(1).getDoubleValue(), 0.01);
        assertEquals(3, currentrow.getField(2).getDoubleValue(), 0.01);
        assertEquals(4, currentrow.getField(3).getDoubleValue(), 0.01);
        assertEquals(5, currentrow.getField(4).getDoubleValue(), 0.01);
        Thread.sleep(11000);
        currentrow = reader.getNext();
        assertEquals(6, currentrow.getField(0).getDoubleValue(), 0.01);
        assertEquals(7, currentrow.getField(1).getDoubleValue(), 0.01);
        assertEquals(8, currentrow.getField(2).getDoubleValue(), 0.01);
        assertEquals(9, currentrow.getField(3).getDoubleValue(), 0.01);
        assertEquals(10, currentrow.getField(4).getDoubleValue(), 0.01);
    }

    /**
     * Test of numberOfRows method, of class
     * XLSX_horisontal_Reader_for_Big_Documents.
     */
    @Test
    public void testNumberOfRows() throws Exception {
        int numberOfRows = reader.numberOfRows();
        assertEquals(2, numberOfRows);
    }

}
