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
import shoreline_exam_2018.be.InputObject;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author janvanzetten
 */
public class CSV_Horisontal_Reader extends autoCloseableReader{

    CSVReader mainReader;
    Iterator<String[]> iterator;
    String fileName;
    private static final char SEPERATOR = ';';
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
     * closes the file stream
     * @throws DALException 
     */
    @Override
    synchronized void closeMainStream() throws DALException {
        try {
            mainReader.close();
        } catch (IOException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }
}
