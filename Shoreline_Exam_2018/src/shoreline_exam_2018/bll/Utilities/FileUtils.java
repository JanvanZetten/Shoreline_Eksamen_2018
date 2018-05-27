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
    
    /**
     * Removes the last . and the text after that
     * @param name
     * @return 
     */
    public static String removeExtension(String name){
            String[] outputSplit = name.split("\\.");
            String outputName = outputSplit[0];
            for (int i = 1; i < (outputSplit.length - 1); i++) {
                outputName = outputName + "." +  outputSplit[i];
            }
            System.out.println(outputName);
            return outputName;
    }
}
