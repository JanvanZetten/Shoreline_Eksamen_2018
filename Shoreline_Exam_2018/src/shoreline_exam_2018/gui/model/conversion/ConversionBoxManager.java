package shoreline_exam_2018.gui.model.conversion;

import java.nio.file.Path;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.ConversionThread;
import shoreline_exam_2018.bll.Utilities.FileUtils;

/**
 *
 * @author alexl
 */
public class ConversionBoxManager {
    
    private BLLFacade bll;
    
    public ConversionBoxManager() {
        bll = BLLManager.getInstance();
    }

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
    public ConversionBoxSingle newConversion(String taskName, Path inputPath, Path outputPath, Profile selectedProfile, ListView<ConversionBoxInterface> listJobs, ConversionBoxMulti cMultiJob) throws BLLException {

        String inputExtension;
        String outputExtension;
        Path outputfileChecked = bll.checkForExisting(outputPath);

        inputExtension = FileUtils.getFiletype(inputPath);
        outputExtension = FileUtils.getFiletype(outputfileChecked);
        
        ConversionThread cThread = null;

        if ((inputExtension.equalsIgnoreCase("XLSX") || inputExtension.equalsIgnoreCase("CSV")) && outputExtension.equalsIgnoreCase("json")) {
            cThread = new ConversionThread(taskName, inputPath, outputfileChecked, selectedProfile);
        }

        if (cThread != null) {
            ConversionBoxSingle cBox = new ConversionBoxSingle(taskName, cThread, outputfileChecked, selectedProfile, listJobs, cMultiJob);
            cThread.giveBox(cBox);
            return cBox;
        } else {
            throw new BLLException("one of the file types might not be supported");
        }
    }

    /**
     * Creates a ConversionBoxMulti that contains COnversionJobSingles.
     * 
     * Used for ONLY MULTI conversion.
     * 
     * @param selectedProfile
     * @param listJobs
     * @return 
     */
    public ConversionBoxMulti newMultiConversion(Profile selectedProfile, ListView<ConversionBoxInterface> list) {
        ConversionBoxMulti cMultiJob = new ConversionBoxMulti(selectedProfile, list);
        return cMultiJob;
    }
}
