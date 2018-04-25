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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author janvanzetten
 */
public class XLSX_horisontal_Reader implements InputFileReader{

    private static final long EXPIRATION_TIME = 10000; //in milliseconds
    private long timeouttime = System.currentTimeMillis()+EXPIRATION_TIME;
    private String FileName;
    private boolean open = false;
    private Workbook mainWorkbook;
    private int pointer = 0;
    
    
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
     * @throws shoreline_exam_2018.dal.DALException
     */
    @Override
    public List<String> getParameters() throws DALException{
        List<String> parameterList = new ArrayList<>();
     
            Workbook workbook = openStream();
            Iterator<Row> iterator = workbook.getSheetAt(0).iterator();
            
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
            
        try {
            workbook.close();
        } catch (IOException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }

        return parameterList;
    }

    @Override
    public boolean hasNext() throws DALException {
        timeouttime = System.currentTimeMillis()+EXPIRATION_TIME;
        if (!open){
            mainWorkbook = openStream();
            makeTimeout();
            open = true;
        }
        if (mainWorkbook.getSheetAt(0).getRow(pointer) != null){
            return true;
        }
        else{
            closeMainStream();
            return false;
        }
    }

    @Override
    public Row getNextRow() throws DALException{
        timeouttime = System.currentTimeMillis() + EXPIRATION_TIME;
        if (!open){
            mainWorkbook = openStream();
            
            open = true;
        }
        pointer++;
        return mainWorkbook.getSheetAt(0).getRow(pointer);
    }

    private Workbook openStream() throws DALException {
        try {
            FileInputStream excelFile = new FileInputStream(new File(FileName));
            return new SXSSFWorkbook(new XSSFWorkbook(excelFile));
        } catch (FileNotFoundException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        } catch (IOException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }
    
    private synchronized void closeMainStream() throws DALException{
        try {
            open = false;
            mainWorkbook.close();
        } catch (IOException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    private void makeTimeout() {
        Thread thread;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(System.currentTimeMillis() < timeouttime){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(XLSX_horisontal_Reader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    closeMainStream();
                } catch (DALException ex) {
                    Logger.getLogger(XLSX_horisontal_Reader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        thread.start();
    }
    
    
    
}
