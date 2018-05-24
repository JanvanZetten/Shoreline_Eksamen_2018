package shoreline_exam_2018.bll;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author alexl
 */
public class BLLManager implements BLLFacade
{

    private ConversionManager cMan;
    private DALFacade dal;
    private LogManager logMng;
    private static final BLLManager INSTANCE = new BLLManager();
    private DirectoryListenerManager dirListenerMan;

    private BLLManager()
    {
        cMan = new ConversionManager();
        dal = new DALManager();
        logMng = new LogManager();
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
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws BLLException
    {
        try
        {
            return dal.addProfile(name, structure, createdBy);
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
    public ConversionJobSingle startSingleConversion(String taskName, Path inputFile, Path outputFile, Profile profile, ListView<ConversionJobs> listJobs, ConversionJobMulti cMultiJob) throws BLLException
    {
        Path outputfileChecked = checkForExisting(outputFile);

        return cMan.newConversion(taskName, inputFile, outputfileChecked, profile, listJobs, cMultiJob);
    }

    @Override
    public ConversionJobMulti startMultiConversion(Profile currentProfile, ListView<ConversionJobs> list) throws BLLException
    {
        return cMan.newMultiConversion(currentProfile, list);
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
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(base.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++)
            {

                String hex = Integer.toHexString(0xff & hash[i]);

                if (hex.length() == 1)
                {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<Log> getAllLogs() throws BLLException
    {
        return logMng.getAllLogs();
    }

    @Override
    public Log addLog(LogType type, String message, User creator) throws BLLException
    {
        return logMng.addLog(type, message, creator);
    }

    @Override
    public User getcurrentUser()
    {
        return dal.getCurrentUser();
    }

    @Override
    public void createChangeListener(AutoUpdater aThis)
    {
        new ChangeChecker().addObserver(aThis);
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
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
        catch (IOException ex)
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
    public String[] getDefaultDirectories()
    {
        return dal.getDefaultDirectories();
    }

    /**
     * Checks for existing and adds number to it to get a unique filename
     *
     * @param outputFile
     * @return
     */
    private Path checkForExisting(Path outputFile)
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
    public void addDirectoryListener(ConversionJobMulti MultiConversion, Path inputPath, Path outputPath) throws BLLException
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

                    String output = outputPath + File.separator + name + ".json";

                    Platform.runLater(() ->
                    {
                        try
                        {
                            MultiConversion.addJob(startSingleConversion(name, path, Paths.get(output), MultiConversion.getProfile(), MultiConversion.getListJobs(), MultiConversion));
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
