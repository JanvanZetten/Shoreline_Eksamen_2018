/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.DALFacade;
import shoreline_exam_2018.dal.DALManager;

/**
 *
 * @author alexl
 */
public class BLLManager implements BLLFacade
{

    private ConversionManager cMan;
    private DALFacade dal;
    private LogManager logMng;

    public BLLManager()
    {
        cMan = new ConversionManager();
        dal = new DALManager();
        logMng = new LogManager();
    }

    @Override
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws BLLExeption
    {
        try
        {
            return dal.addProfile(name, structure, createdBy);
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<Profile> getAllProfiles() throws BLLExeption
    {
        try
        {
            return dal.getAllProfiles();
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path) throws BLLExeption
    {
        try
        {
            return dal.getHeadersAndExamplesFromFile(path);
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }
    
    @Override
    public ConversionJob startConversion(String taskName, Path inputFile, Path outputFile, Profile profile, ListView<ConversionJob> listJobs) throws BLLExeption
    {
        return cMan.newConversion(taskName, inputFile, outputFile, profile, listJobs);
    }
    
    @Override
    public User login(String username, String password) throws BLLExeption
    {
        try
        {
            User currentUser = dal.userLogin(username, encrypt(password));
            return currentUser;
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public String encrypt(String base) throws BLLExeption
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
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public ObservableList<Log> getObsLogList()
    {
        return logMng.getObsLogList();
    }

    @Override
    public void getAllLogs() throws BLLExeption
    {
        logMng.getAllLogs();
    }

    @Override
    public void addLog(LogType type, String message, User creator) throws BLLExeption
    {
        logMng.addLog(type, message, creator);
    }
    
    @Override
    public User getcurrentUser() {
        return dal.getCurrentUser();
    }

}
