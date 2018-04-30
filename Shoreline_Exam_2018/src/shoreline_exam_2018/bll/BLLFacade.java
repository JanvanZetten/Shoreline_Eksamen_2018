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
 * @author janvanzetten
 */
public interface BLLFacade {

    /**
     * Creates a conversion that is based on the file path of a file and a profile.
     * @param taskName
     * @param selectedFilePath
     * @param selectedProfile
     * @return 
     */
    public ConversionTask setConversionFilePath(String taskName, Path selectedFilePath, Profile selectedProfile);
    
}
