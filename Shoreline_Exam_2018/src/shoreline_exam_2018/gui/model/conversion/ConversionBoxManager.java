package shoreline_exam_2018.gui.model.conversion;

import shoreline_exam_2018.bll.converters.ConversionTaskManager;
import java.nio.file.Path;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
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
     * @param taskName        = The name of the Conversion
     * @param inputPath       = The input file
     * @param outputPath      = The output file
     * @param selectedProfile = The selected profile for the conversion
     * @param listBoxes       = The list that contains the boxes
     * @param cMultiJob       = The multi box. If no multi box exists, null is entered as the parameter
     * @return the Task so that it can be set in the view.
     * @throws shoreline_exam_2018.bll.BLLException
     */
    public ConversionBoxSingle newConversion(String taskName, Path inputPath, Path outputPath, Profile selectedProfile, ListView<ConversionBoxInterface> listBoxes, ConversionBoxMulti cMultiJob) throws BLLException {

        String inputExtension;
        String outputExtension;
        Path outputfileChecked = bll.checkForExisting(outputPath);

        inputExtension = FileUtils.getFiletype(inputPath);
        outputExtension = FileUtils.getFiletype(outputfileChecked);
        
        ConversionTaskManager cTaskMan = null;

        if ((inputExtension.equalsIgnoreCase("XLSX") || inputExtension.equalsIgnoreCase("CSV")) && outputExtension.equalsIgnoreCase("json")) {
            cTaskMan = new ConversionTaskManager(taskName, inputPath, outputfileChecked, selectedProfile);
        }

        if (cTaskMan != null) {
            ConversionBoxSingle cBox = new ConversionBoxSingle(taskName, cTaskMan, outputfileChecked, selectedProfile, listBoxes, cMultiJob);
            cTaskMan.giveBox(cBox);
            return cBox;
        } else {
            throw new BLLException("one of the file types might not be supported");
        }
    }

    /**
     * Creates a ConversionBoxMulti that contains COnversionJobSingles.
     * 
     * Used ONLY for MULTI conversion.
     * 
     * @param selectedProfile = The profile that created the conversion
     * @param list            = The list that exists inside the multi conversion
     * @return the ConversionBoxMulti that has been created.
     */
    public ConversionBoxMulti newMultiConversion(Profile selectedProfile, ListView<ConversionBoxInterface> list) {
        ConversionBoxMulti cMultiJob = new ConversionBoxMulti(selectedProfile, list);
        return cMultiJob;
    }
}
