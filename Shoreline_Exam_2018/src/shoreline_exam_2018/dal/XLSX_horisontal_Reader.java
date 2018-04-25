/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author janvanzetten
 */
public class XLSX_horisontal_Reader implements InputFileReader{

    private String FileName;
    /**
     * give it the filename as a path to where the file is located
     * @param FileName ex "/tmp/MyFirstExcel.xlsx"
     */
    public XLSX_horisontal_Reader(String FileName) {
        this.FileName = FileName;
        
    }
    
    /**
     * Makes string when cell type is String, Boolean, Numeric or formula else nothing
     * @return list with the parameter names as string and empty string if there was no parameter
     */
    @Override
    public List<String> getParameters() {
        List<String> parameterList = new ArrayList<>();
        try {

            FileInputStream excelFile = new FileInputStream(new File(FileName));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            
            if (iterator.hasNext()){
                Row currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    switch(currentCell.getCellTypeEnum()){
                        case STRING:
                            parameterList.add(currentCell.getStringCellValue());
                            break;
                        case BOOLEAN:
                            parameterList.add(Boolean.toString(currentCell.getBooleanCellValue()));
                            break;
                        case FORMULA:
                            parameterList.add(currentCell.getCellFormula());
                            break;
                        case NUMERIC:
                            parameterList.add(Double.toString(currentCell.getNumericCellValue()));
                            break;
                        default:
                            parameterList.add("");
                            break;
                                    
                    }
                    
                }
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return parameterList;
    }

    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Row getNextRow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
