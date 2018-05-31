package shoreline_exam_2018.bll;

import shoreline_exam_2018.bll.Utilities.Encrypter;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxManager;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxMulti;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.be.output.structure.entity.StructEntityObject;
import shoreline_exam_2018.be.runnable.RunnableWithPath;
import shoreline_exam_2018.bll.Utilities.FileUtils;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.DALFacade;
import shoreline_exam_2018.dal.DALManager;
import shoreline_exam_2018.dal.directorylistener.DirectoryListenerManager;
import shoreline_exam_2018.gui.model.AlertFactory;
import shoreline_exam_2018.gui.model.AutoUpdater;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxInterface;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxSingle;

/**
 *
 * @author alexl
 */
public class BLLManager implements BLLFacade
{

    private final DALFacade dal;
    private final DirectoryListenerManager dirListenerMan;
    private static final BLLManager INSTANCE = new BLLManager();

    private BLLManager()
    {
        dal = new DALManager();
        dirListenerMan = new DirectoryListenerManager();
    }

    /**
     * Singleton method. Guarantees that BLLManager exists only once, and as a
     * consequence, ensures that other class made by BLLManager also only exist
     * once.
     *
     * @return
     */
    public static BLLManager getInstance()
    {
        return INSTANCE;
    }

    @Override
    public Profile addProfile(String name, StructEntityObject structure, HashMap<String, Map.Entry<Integer, String>> headersIndexAndExamples) throws BLLException
    {
        try
        {
            return dal.addProfile(name, structure, headersIndexAndExamples);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<Profile> getAllProfiles() throws BLLException
    {
        try
        {
            return dal.getAllProfiles();
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public StructEntityObject addStructure(String name, StructEntityObject structure, int createdBy) throws BLLException
    {
        try
        {
            return dal.addStructure(name, structure, createdBy);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<StructEntityObject> getAllStructures() throws BLLException
    {
        try
        {
            return dal.getAllStructures();
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path) throws BLLException
    {
        try
        {
            return dal.getHeadersAndExamplesFromFile(path, FileUtils.getFiletype(path));
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public void deleteProfile(Profile profile) throws BLLException
    {
        try
        {
            dal.deleteProfile(profile);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public Profile updateProfile(Profile profile) throws BLLException
    {
        try
        {
            return dal.updateProfile(profile);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override

    public User login(String username, String password) throws BLLException
    {
        try
        {
            User currentUser = dal.userLogin(username, encrypt(password));
            return currentUser;
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public String encrypt(String base) throws BLLException
    {
        return Encrypter.encrypt(base);
    }

    @Override
    public List<Log> getAllLogs() throws BLLException
    {
        try
        {
            return dal.getAllLogs();
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public Log addLog(LogType type, String message) throws BLLException
    {
        try
        {
            return dal.addLog(type, message);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public User getcurrentUser()
    {
        return dal.getCurrentUser();
    }

    @Override
    public void createChangeListener(AutoUpdater aThis)
    {
        new LogChangeChecker().addObserver(aThis);
    }

    @Override
    public int getNewestLog() throws BLLException
    {
        try
        {
            return dal.getNewestLog();
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public void updateDefaultDirectory(String[] directory, String input, String output) throws BLLException
    {
        try
        {
            dal.updateDefaultDirectory(directory, input, output);
        }
        catch (DALException | IOException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public void addDefaultOutput(String outputValue)
    {
        dal.addDefaultOutput(outputValue);
    }

    @Override
    public void addDefaultInput(String inputValue)
    {
        dal.addDefaultInput(inputValue);
    }

    @Override
    public void addDefaultProfile(String profile) throws BLLException
    {
        dal.addDefaultProfile(profile);
    }

    @Override
    public void updateDefaultProfile(String[] profile) throws BLLException
    {
        try
        {
            dal.updateDefaultProfile(profile);
        }
        catch (DALException | IOException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public String[] getDefaultDirectories()
    {
        return dal.getDefaultDirectories();
    }

    @Override
    public int getDefaultProfile()
    {
        int i = Integer.parseInt(dal.getDefaultProfile());
        return i;
    }

    @Override
    public Path checkForExisting(Path outputFile)
    {
        int number = 1;
        if (dal.doesFileExist(outputFile))
        {
            while (dal.doesFileExist(addNumberToPath(outputFile, number)))
            {
                number++;
            }
            return addNumberToPath(outputFile, number);

        }
        else
        {
            return outputFile;
        }
    }

    /**
     * Adds the given number to the filename of the path
     *
     * @param outputFile
     * @param number
     * @return
     */
    private Path addNumberToPath(Path outputFile, int number)
    {
        String asString = outputFile.toString();
        String result;

        String[] split = asString.split("\\.");

        split[split.length - 2] = split[split.length - 2] + number;

        result = split[0];

        for (int i = 1; i < split.length; i++)
        {
            result = result + "." + split[i];
        }

        return Paths.get(result);
    }

    @Override
    public void addDirectoryListener(ConversionBoxMulti conversionBoxMulti, Path inputPath, Path outputPath, ConversionBoxManager cManager) throws BLLException
    {
        try
        {
            dirListenerMan.GetDirectoryListener(inputPath, new RunnableWithPath()
            {
                @Override
                public void run()
                {
                    String pattern = Pattern.quote(System.getProperty("file.separator"));
                    String[] split = path.toString().split(pattern);

                    String name = split[split.length - 1];

                    String output = outputPath + File.separator + FileUtils.removeExtension(name) + ".json";

                    Platform.runLater(()
                            ->
                    {
                        try
                        {
                            conversionBoxMulti.addBox(cManager.newConversion(name, path, Paths.get(output), conversionBoxMulti.getProfile(), conversionBoxMulti.getListJobs(), conversionBoxMulti));
                        }
                        catch (BLLException ex)
                        {
                            AlertFactory.showError("Conversion Error", ex.getMessage());
                        }
                    });

                }
            }, new RunnableWithPath()
            {
                @Override
                public void run()
                {
                    System.out.println("Exception in Directory listener");
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }, new RunnableWithPath()
            {
                @Override
                public void run()
                {
                    System.out.println("Closing");
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }
}
