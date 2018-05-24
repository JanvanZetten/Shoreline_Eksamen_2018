/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.LoggingHelper;

/**
 *
 * @author alexl
 */
public class PropertiesReader {

    BLLFacade bll;
    List<String> valueList = new ArrayList<String>();

    public PropertiesReader() {
        bll = BLLManager.getInstance();
        
        readProperties();
        setDefaultDirectories();
    }
    
    /**
     * Reads the properties file
     */
    private void readProperties() {
        try {
            FileInputStream fileInput = new FileInputStream("properties.properties");
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();

            Enumeration enuKeys = properties.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = properties.getProperty(key);
                valueList.add(value);
            }

        } catch (FileNotFoundException ex) {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Cannot read properties", "Error: " + ex.getMessage());
        } catch (IOException ex) {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Cannot read properties", "Error: " + ex.getMessage());
        }
    }

    private void setDefaultDirectories() {
        bll.addDefaultOutput(valueList.get(0));
        bll.addDefaultInput(valueList.get(1));
    }
}
