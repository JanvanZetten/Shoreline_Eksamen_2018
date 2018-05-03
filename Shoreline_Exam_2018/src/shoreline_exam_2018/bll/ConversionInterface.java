/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import shoreline_exam_2018.be.MutableBoolean;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author alexl
 */
public interface ConversionInterface {    
   
    /**
     * Converts the file.
     * @param selectedProfile the profile for converting the inputfile to the output file
     * @param inputFile the input file from which to convert from
     * @param outputFile the placement and naming of the output file 
     * @param isCanceld Set to true if it should be canceled
     * @param isOperating set to false when it is wished to pause it
     */
    public void convertFile(Profile selectedProfile, Path inputFile, Path outputFile, MutableBoolean isCanceld, MutableBoolean isOperating) throws BLLExeption;
    
    
}
