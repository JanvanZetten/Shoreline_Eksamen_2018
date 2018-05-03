/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.io.File;
import java.nio.file.Path;
import javafx.scene.layout.FlowPane;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author alexl
 */
public class ConversionManager {

    /**
     * Creates a Thread (the one working) and a Task (the visual element the
     * user can see).
     *
     * @param taskName = The name of the Conversion
     * @param inputPath the input file
     * @param outputPath the output file
     * @param selectedProfile = The selected profile for the conversion
     * @return = Returns the Task so that it can be set in the view.
     */
    public ConversionJob newConversion(String taskName, Path inputPath, Path outputPath, Profile selectedProfile, FlowPane paneJobs) throws BLLExeption {

        String inputExtension;
        String outputExtension;
        
        String inputString = inputPath.toString();
        String outptuString = outputPath.toString();

        String[] inputSplit = inputString.split("\\.");
        String[] outputSplit = outptuString.split("\\.");
        
        System.out.println(inputPath.toString());
        
        inputExtension = inputSplit[inputSplit.length - 1];
        outputExtension = outputSplit[outputSplit.length - 1];

        ConversionThread cThread = null;

        if (inputExtension.equalsIgnoreCase("XLSX") && outputExtension.equalsIgnoreCase("json")) {
            cThread = new ConversionThread(new ConvertXlsxToJson(), inputPath, outputPath, selectedProfile);
        }

        if (cThread != null) {

            ConversionJob cJob = new ConversionJob(taskName, cThread, inputPath, selectedProfile, paneJobs);
            cThread.giveJob(cJob);
            return cJob;
        }
        else 
        {
            throw new BLLExeption("one of the file types might not be supported");
        }

    }
}
