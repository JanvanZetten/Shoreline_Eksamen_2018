/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.filereader;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_exam_2018.be.InputObject;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author janvanzetten
 */
public class CSV_Horisontal_Reader implements Reader {

    CSVReader mainReader;
    Iterator<String[]> iterator;
    String fileName;
    private static final long EXPIRATION_TIME = 10000; //in milliseconds
    private static final char SEPERATOR = ';';
    private long timeouttime = System.currentTimeMillis() + EXPIRATION_TIME;
    private boolean open = false;

    
    public CSV_Horisontal_Reader(String fileName) throws DALException {
        this.fileName = fileName;
        mainReader = openStream();
        iterator = mainReader.iterator();
        iterator.next();
    }

    @Override
    public List<String> getParameters() throws DALException {
        try {
            CSVReader csvReader = openStream();

            String[] firstRow = csvReader.readNext();

            return Arrays.asList(firstRow);

        } catch (FileNotFoundException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        } catch (IOException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }

    }

    @Override
    public boolean hasNext() throws DALException {
        timeouttime = System.currentTimeMillis() + EXPIRATION_TIME;
        if (!open) {
            mainReader = openStream();
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
    public InputObject getNext() throws DALException {
        timeouttime = System.currentTimeMillis() + EXPIRATION_TIME;
        
        if (!open) {
            mainReader = openStream();

            open = true;
        }
        
        String[] row = iterator.next();
        
        return InputObjectConverter.StringArrayToInputObject(row);
    }

    @Override
    public int numberOfRows() throws DALException {
        InputStream is = null;
        
        try {
            is = new BufferedInputStream(new FileInputStream(fileName));
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } catch (FileNotFoundException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        } catch (IOException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }

    }

    
    /**
     * Opens a Stream and returns a CSVReader with the stream
     * @return
     * @throws DALException 
     */
    private CSVReader openStream() throws DALException {
        try {
            return new CSVReaderBuilder(new FileReader(new File(fileName)))
                    .withCSVParser(new CSVParserBuilder().withSeparator(SEPERATOR).build())
                    .build();
        } catch (FileNotFoundException ex) {
           throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    
    /**
     * Make a task which checkes for timeout and closes the stream if it does
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

    /**
     * closes the file stream
     * @throws DALException 
     */
    private void closeMainStream() throws DALException {
        try {
            mainReader.close();
        } catch (IOException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }
}
