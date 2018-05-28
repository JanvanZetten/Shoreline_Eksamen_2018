package shoreline_exam_2018.gui.model.conversion;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.LoggingHelper;
import shoreline_exam_2018.bll.Utilities.FileUtils;
import shoreline_exam_2018.gui.model.AlertFactory;

/**
 *
 * @author alexl
 */
public class BoxMaker
{

    private final ConversionBoxManager cManager;
    private final BLLFacade bll;

    public BoxMaker()
    {
        cManager = new ConversionBoxManager();
        bll = BLLManager.getInstance();
    }

    /**
     * Start a conversion with the given input and output files
     *
     * @param currentProfile = The profile initiating the conversion
     * @param listBoxes = The list that contains this single box.
     * @param selectedFile = The file that has been selected.
     * @param outputFile
     * @return the conversion job that is started
     */
    public ConversionBoxSingle createSingleBox(Profile currentProfile,
            ListView<ConversionBoxInterface> listBoxes,
            File selectedFile,
            File outputFile)
    {
        ConversionBoxSingle conversionBoxSingle = null;
        if (selectedFile != null && outputFile != null && currentProfile != null)
        {
            String name;
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] split = selectedFile.getAbsolutePath().split(pattern);

            name = split[split.length - 1];

            try
            {
                String output = outputFile.toPath() + File.separator + FileUtils.removeExtension(name) + ".json";
                File correctOutputFile = new File(output);
                conversionBoxSingle = cManager.newConversion(name, selectedFile.toPath(), correctOutputFile.toPath(), currentProfile, listBoxes, null);
            }
            catch (BLLException ex)
            {
                LoggingHelper.logException(ex);
                AlertFactory.showError("Could not convert.", "Error: " + ex.getMessage());
            }

            return conversionBoxSingle;
        }
        else
        {
            AlertFactory.showError("Missing fields", "Please select a profile and an input and output file.");
            return null;
        }
    }

    /**
     * Creates a new ConversionBoxMulti and adds all files in a folder into this
     * object as ConversionBoxSingle. Also checks for file-types.
     * @param currentProfile
     * @param listJobs
     * @param selectedFile
     * @param outputFile
     * @return
     */
    public ConversionBoxMulti createMultiBox(Profile currentProfile,
            ListView<ConversionBoxInterface> listJobs,
            File selectedFile,
            File outputFile)
    {
        ConversionBoxMulti conversionBoxMulti = null;
        ListView<ConversionBoxInterface> list = new ListView<>();

        if (selectedFile != null && outputFile != null && currentProfile != null)
        {
            File[] directory = selectedFile.listFiles();

            conversionBoxMulti = cManager.newMultiConversion(currentProfile, list);

            for (File file : directory)
            {

                String extension = "";

                int i = file.toString().lastIndexOf('.');

                extension = file.toString().substring(i + 1);

                if (extension.equals("xlsx") || extension.equals("csv"))
                {
                    try
                    {
                        String output = outputFile.toPath() + File.separator + FileUtils.removeExtension(file.getName()) + ".json";
                        File correctOutputFile = new File(output);
                        conversionBoxMulti.addBox(cManager.newConversion(file.getName(), file.toPath(), correctOutputFile.toPath(), currentProfile, list, conversionBoxMulti));
                    }
                    catch (BLLException ex)
                    {
                        LoggingHelper.logException(ex);
                        AlertFactory.showError("Could not convert.", "Error: " + ex.getMessage());
                    }
                }
            }
            addFolderListener(conversionBoxMulti, selectedFile.getPath(), cManager, outputFile);
            return conversionBoxMulti;
        }
        else
        {
            AlertFactory.showError("Missing fields", "Please select a profile and an input and output file.");
            return null;
        }
    }

    /**
     * Creates a listener to a created ConversionBoxMulti.
     * @param conversionBoxMulti = The ConversionBoxMulti to have the listener.
     * @param path = The input path of the folder.
     * @param cManager = The ConversionBoxManager that connects task and box
     * creation.
     * @param outputFile
     */
    private void addFolderListener(ConversionBoxMulti conversionBoxMulti, String path, ConversionBoxManager cManager, File outputFile)
    {
        try
        {
            bll.addDirectoryListener(conversionBoxMulti, Paths.get(path), outputFile.toPath(), cManager);
        }
        catch (BLLException ex)
        {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Could not listen on directory", ex.getMessage());
        }

    }
}
