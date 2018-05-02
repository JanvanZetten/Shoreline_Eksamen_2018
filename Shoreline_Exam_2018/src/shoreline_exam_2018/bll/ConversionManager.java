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
     * Creates a Thread (the one working) and a Task (the visual element the user can see).
     * @param taskName          = The name of the Conversion
     * @param selectedFilePath  = The path of the file attempted to convert
     * @param selectedProfile   = The selected profile for the conversion
     * @return                  = Returns the Task so that it can be set in the view.
     */
    public ConversionTask newConversion(String taskName, Path selectedFilePath, Profile selectedProfile) {
        
        ConversionThread cThread = new ConversionThread();
        ConversionTask cTask = new ConversionTask(taskName, cThread, selectedFilePath, selectedProfile);
        cThread.setTask(cTask);
        return cTask;
    }
}
