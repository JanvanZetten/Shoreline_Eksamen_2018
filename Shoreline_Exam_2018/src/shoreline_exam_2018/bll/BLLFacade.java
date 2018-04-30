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

    public ConversionTask setConversionFilePath(Path selectedFilePath, Profile selectedProfile);
    
}
