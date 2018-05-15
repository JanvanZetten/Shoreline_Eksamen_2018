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
     * Test of getNextRow method, of class
     * XLSX_horisontal_Reader_for_Big_Documents.
     */
    @Test
    public void testGetNextRow() throws Exception {
        reader = new XLSX_horisontal_Reader_for_Big_Documents("test/shoreline_exam_2018/MockTilJunitTest.xlsx");
        for (int i = 0; i < 2; i++) {
            Row currentrow = reader.getNextRow();
            assertEquals(1, currentrow.getCell(0).getNumericCellValue(), 0.01);
            assertEquals(2, currentrow.getCell(1).getNumericCellValue(), 0.01);
            assertEquals(null, currentrow.getCell(2));
            assertEquals(5, currentrow.getCell(3).getNumericCellValue(), 0.01);
            assertEquals(6, currentrow.getCell(4).getNumericCellValue(), 0.01);
        }
    }
    
    @Test
    public void timeOutTest() throws Exception{
        reader = new XLSX_horisontal_Reader_for_Big_Documents("test/shoreline_exam_2018/MockTilJuintMedForskelligData.xlsx");
        Row currentrow = reader.getNextRow();
        assertEquals(1, currentrow.getCell(0).getNumericCellValue(), 0.01);
        assertEquals(2, currentrow.getCell(1).getNumericCellValue(), 0.01);
        assertEquals(3, currentrow.getCell(2).getNumericCellValue(), 0.01);
        assertEquals(4, currentrow.getCell(3).getNumericCellValue(), 0.01);
        assertEquals(5, currentrow.getCell(4).getNumericCellValue(), 0.01);
        Thread.sleep(11000);
        assertEquals(6, currentrow.getCell(0).getNumericCellValue(), 0.01);
        assertEquals(7, currentrow.getCell(1).getNumericCellValue(), 0.01);
        assertEquals(8, currentrow.getCell(2).getNumericCellValue(), 0.01);
        assertEquals(9, currentrow.getCell(3).getNumericCellValue(), 0.01);
        assertEquals(10, currentrow.getCell(4).getNumericCellValue(), 0.01);
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
