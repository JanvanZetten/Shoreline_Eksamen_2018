/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.filereader;

import com.monitorjbl.xlsx.StreamingReader;
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
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author janvanzetten
 */
public class XLSX_horisontal_Reader_for_Big_Documents implements InputFileReader {

    private static final long EXPIRATION_TIME = 10000; //in milliseconds
    private long timeouttime = System.currentTimeMillis() + EXPIRATION_TIME;
    private String FileName;
    private boolean open = false;
    private Workbook mainWorkbook;
    private Iterator<Row> iterator;
    private static final int SHEET_NUMBER = 0;

    /**
     * give it the filename as a path to where the file is located
     *
     * @param FileName ex "/tmp/MyFirstExcel.xlsx" this file has to be of the
     * xlsx type!!
     */
    public XLSX_horisontal_Reader_for_Big_Documents(String FileName) throws DALException {
        this.FileName = FileName;
        mainWorkbook = openStream();
        iterator = mainWorkbook.getSheetAt(SHEET_NUMBER).iterator();
        iterator.next(); // first line are the parameters and therefor not used;
    }

    /**
     * Makes string when cell type is String, Boolean, Numeric or formula else
     * nothing
     *
     * @return list with the parameter names as string and empty string if there
     * was no parameter
     * @throws shoreline_exam_2018.dal.DALException
     */
    @Override
    public List<String> getParameters() throws DALException {
        List<String> parameterList = new ArrayList<>();

        Workbook workbook = openStream();
        Iterator<Row> localiterator = workbook.getSheetAt(SHEET_NUMBER).iterator();

        int cellPointer = 0;

        if (localiterator.hasNext()) {
            Row currentRow = localiterator.next();
            while (checkForEndOfRow(currentRow, cellPointer, 10)) {
                Cell currentCell = currentRow.getCell(cellPointer);
                cellPointer++;
                if (currentCell == null) {
                    parameterList.add("");
                } else {
                    switch (currentCell.getCellTypeEnum()) {
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
        timeouttime = System.currentTimeMillis() + EXPIRATION_TIME;
        if (!open) {
            mainWorkbook = openStream();
            makeTimeout();
            open = true;
        }
        if (iterator.hasNext()) {
            return true;
        } else {
            closeMainStream();
            return false;
        }
    }

    @Override
    public Row getNextRow() throws DALException {
        timeouttime = System.currentTimeMillis() + EXPIRATION_TIME;
        if (!open) {
            mainWorkbook = openStream();

            open = true;
        }
        
        return iterator.next();
    }

    /**
     * opens a stream to the xlsx file
     *
     * @return a workbook object with this file
     * @throws DALException
     */
    private Workbook openStream() throws DALException {
        try {
            FileInputStream excelFile = new FileInputStream(new File(FileName));
            
            Workbook workbook = StreamingReader.builder().open(excelFile);
            
            return workbook;
        } catch (FileNotFoundException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * closes the mainWorkbook and sets the open field to false
     *
     * @throws DALException
     */
    private synchronized void closeMainStream() throws DALException {
        try {
            open = false;
            mainWorkbook.close();
        } catch (IOException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * makes a thread which cheks for timeout for the mainWorkbook and closes it
     * when it expires
     */
    private void makeTimeout() {
        Thread thread;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (System.currentTimeMillis() < timeouttime) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(XLSX_horisontal_Reader_for_Big_Documents.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    closeMainStream();
                } catch (DALException ex) {
                    Logger.getLogger(XLSX_horisontal_Reader_for_Big_Documents.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        thread.start();
    }

    private boolean checkForEndOfRow(Row currentRow, int cellPointer, int cellsTolookforward) {
        for (int j = cellPointer; j < cellPointer + cellsTolookforward; j++) {
            if (currentRow.getCell(j) != null) {
                return true;
            }

        }
        return false;
    }

    /**
     * Get the number of rows in the file
     * @return
     * @throws DALException 
     */
    @Override
    public int numberOfRows() throws DALException{
        Workbook thisWorkbook = openStream();
        
        int lastRowNumber = thisWorkbook.getSheetAt(SHEET_NUMBER).getLastRowNum();
        
        try {
            thisWorkbook.close();
        } catch (IOException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        
        return lastRowNumber;
    }
    
}
