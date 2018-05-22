package shoreline_exam_2018.bll;

import java.nio.file.Path;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.Utilities.FileUtils;

/**
 *
 * @author alexl
 */
public class ConversionManager {

    /**
     * Creates a Thread (the one working) and a Task (the visual element the
     * user can see).
     * 
     * Used for both MULTI conversion and SINGLE conversion.
     *
     * @param taskName = The name of the Conversion
     * @param inputPath the input file
     * @param outputPath the output file
     * @param selectedProfile = The selected profile for the conversion
     * @return = Returns the Task so that it can be set in the view.
     */
    public ConversionJobSingle newConversion(String taskName, Path inputPath, Path outputPath, Profile selectedProfile, ListView<ConversionJobs> listJobs, ConversionJobMulti cMultiJob) throws BLLException {

        String inputExtension;
        String outputExtension;

        inputExtension = FileUtils.getFiletype(inputPath);
        outputExtension = FileUtils.getFiletype(outputPath);

        ConversionThread cThread = null;

        if ((inputExtension.equalsIgnoreCase("XLSX") || inputExtension.equalsIgnoreCase("CSV")) && outputExtension.equalsIgnoreCase("json")) {
            cThread = new ConversionThread(taskName, inputPath, outputPath, selectedProfile);
        }

        if (cThread != null) {
            ConversionJobSingle cJob = new ConversionJobSingle(taskName, cThread, outputPath, selectedProfile, listJobs, cMultiJob);
            cThread.giveJob(cJob);
            return cJob;
        } else {
            throw new BLLException("one of the file types might not be supported");
        }
    }

    /**
     * Creates a ConversionJobMulti that contains COnversionJobSingles.
     * 
     * Used for ONLY MULTI conversion.
     * 
     * @param selectedProfile
     * @param listJobs
     * @return 
     */
    ConversionJobMulti newMultiConversion(Profile selectedProfile, ListView<ConversionJobs> list) {
        ConversionJobMulti cMultiJob = new ConversionJobMulti(selectedProfile, list);
        return cMultiJob;
    }
}
