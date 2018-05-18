/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.Utilities;

import java.nio.file.Path;

/**
 *
 * @author janvanzetten
 */
public class FileUtils {
    
    /**
     * Get the filetype from the the files path
     *
     * @param inputFile
     * @return
     */
    public static String getFiletype(Path inputFile) {
        String inputString = inputFile.toString();
        String[] inputSplit = inputString.split("\\.");
        return inputSplit[inputSplit.length - 1];
    }
    
}
