/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.bll.Utilities.FileUtils;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.DALFacade;
import shoreline_exam_2018.dal.DALManager;
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

    private BLLManager()
    {
        cMan = new ConversionManager();
        dal = new DALManager();
        logMng = new LogManager();
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
    public ConversionJob startConversion(String taskName, Path inputFile, Path outputFile, Profile profile, ListView<ConversionJob> listJobs) throws BLLException
    {
        return cMan.newConversion(taskName, inputFile, outputFile, profile, listJobs);
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
    public void updateDefaultDirectory(String[] directory, String input, String output) throws BLLException {
        try {
            dal.updateDefaultDirectory(directory, input, output);
        } catch (DALException ex) {
            throw new BLLException(ex.getMessage(), ex.getCause());
        } catch (IOException ex) {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public void addDefaultDirectories(String inputValue, String outputValue) {
        dal.addDefaultDirectories(inputValue, outputValue);
    }

    @Override
    public String[] getDefaultDirectories() {
        return dal.getDefaultDirectories();
    }
}
