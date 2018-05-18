/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.Utilities.FileUtils;

/**
 *
 * @author alexl
 */
public class ConversionManager
{

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
    public ConversionJob newConversion(String taskName, Path inputPath, Path outputPath, Profile selectedProfile, ListView<ConversionJob> listJobs) throws BLLException
    {

        String inputExtension;
        String outputExtension;

        inputExtension = FileUtils.getFiletype(inputPath);
        outputExtension = FileUtils.getFiletype(outputPath);

        ConversionThread cThread = null;

        if ((inputExtension.equalsIgnoreCase("XLSX") || inputExtension.equalsIgnoreCase("CSV")) && outputExtension.equalsIgnoreCase("json"))
        {
            cThread = new ConversionThread(taskName, inputPath, outputPath, selectedProfile);
        }
        

        if (cThread != null)
        {

            ConversionJob cJob = new ConversionJob(taskName, cThread, outputPath, selectedProfile, listJobs);
            cThread.giveJob(cJob);
            return cJob;
        }
        else
        {
            throw new BLLException("one of the file types might not be supported");
        }

    }
}
