/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import shoreline_exam_2018.be.Profile;


/**
 *
 * @author alexl
 */
public class ConversionManager {
    
    /**
     * Creates
     * @return 
     */
    public ConversionTask newConversion(Path selectedFilePath, Profile selectedProfile) {
        
        ConversionThread cThread = new ConversionThread();
        ConversionTask cTask = new ConversionTask("A string that contains the name of the task", cThread, selectedFilePath, selectedProfile);
        return cTask;
    }
}
