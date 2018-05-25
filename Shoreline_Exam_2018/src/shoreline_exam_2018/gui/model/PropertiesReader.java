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
import java.util.Properties;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.LoggingHelper;

/**
 *
 * @author alexl
 */
public class PropertiesReader {

    BLLFacade bll;
    // Valuelist contains the property value.
    ArrayList<String[]> valueList = new ArrayList<>();

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
                String[] props = new String[2];
                props[0] = (String) enuKeys.nextElement();
                props[1] = properties.getProperty(props[0]);
                valueList.add(props);
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
        for (String[] strings : valueList) {
            if (strings[0].equals("inputDir")) {
                bll.addDefaultInput(strings[1]);
            }
            
            if (strings[0].equals("outputDir")) {
                bll.addDefaultOutput(strings[1]);
            }
        }
    }
    
    private
}
