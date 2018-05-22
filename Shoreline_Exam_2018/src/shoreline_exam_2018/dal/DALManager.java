/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import shoreline_exam_2018.be.InputField;
import shoreline_exam_2018.be.InputObject;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.dal.database.DBChangeDAO;
import shoreline_exam_2018.dal.database.LogDAO;
import shoreline_exam_2018.dal.database.ProfileDAO;
import shoreline_exam_2018.dal.database.StructureDAO;
import shoreline_exam_2018.dal.database.UserDAO;
import shoreline_exam_2018.dal.filereader.CSV_Horisontal_Reader;
import shoreline_exam_2018.dal.filereader.Reader;
import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader_for_Big_Documents;

/**
 *
 * @author alexl
 */
public class DALManager implements DALFacade
{

    private ProfileDAO profileDAO;
    private StructureDAO structureDAO;
    private UserDAO userDAO;
    private LogDAO logDAO;
    private DBChangeDAO changeDAO;
    private Reader reader;
    private PropertiesWriter propWriter;

    private static User currentUser;
    private static String defaultInputDir;
    private static String defaultOutputDir;

    public DALManager()
    {
        this.profileDAO = new ProfileDAO();
        this.structureDAO = new StructureDAO();
        this.userDAO = new UserDAO();
        this.logDAO = new LogDAO();
        this.changeDAO = new DBChangeDAO();
        this.propWriter = new PropertiesWriter();
    }

    @Override
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws DALException
    {
        return profileDAO.addProfile(name, structure, createdBy);
    }

    @Override
    public List<Profile> getAllProfiles() throws DALException
    {
        return profileDAO.getAllProfiles();
    }

    @Override
    public StructEntityObject addStructure(String name, StructEntityObject structure, int createdBy) throws DALException
    {
        return structureDAO.addStructure(name, structure, createdBy);
    }

    @Override
    public List<StructEntityObject> getAllStructures() throws DALException
    {
        return structureDAO.getAllStructures();
    }

    @Override
    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path, String filetype) throws DALException
    {
        if (filetype.equalsIgnoreCase("xlsx"))
        {
            reader = new XLSX_horisontal_Reader_for_Big_Documents(path.toString());
        }
        else if (filetype.equalsIgnoreCase("csv"))
        {
            reader = new CSV_Horisontal_Reader(path.toString());
        }
        else
        {
            throw new DALException("The file type is not supported");
        }
        HashMap<String, Entry<Integer, String>> hae = new HashMap();
        List<String> headers = reader.getParameters();
        InputObject inputObject = null;
        if (reader.hasNext())
        {
            inputObject = reader.getNext();
        }

        StringRenamer sr = new StringRenamer();
        for (int i = 0; i < headers.size(); i++)
        {
            String str = "";
            if (inputObject != null)
            {
                InputField currentCell = inputObject.getField(i);
                if (currentCell != null)
                {
                    switch (currentCell.getType())
                    {
                        case STRING:
                            str = currentCell.getStringValue();
                            break;
                        case NUMERIC:
                            str = Double.toString(currentCell.getDoubleValue());
                            break;
                        case DATE:
                            str = currentCell.getDateValue().toString();
                        default:
                            break;
                    }
                }
                hae.put(sr.checkForDuplicate(headers.get(i)), new SimpleImmutableEntry<>(i, str));
            }
            else
            {
                hae.put(sr.checkForDuplicate(headers.get(i)), new SimpleImmutableEntry<>(i, str));
            }
        }
        return hae;
    }

    @Override
    public User userLogin(String user, String password) throws DALException
    {
        currentUser = userDAO.login(user, password);
        return currentUser;
    }

    @Override
    public User getCurrentUser()
    {
        return currentUser;
    }

    @Override
    public List<Log> getAllLogs() throws DALException
    {
        return logDAO.getAllLogs();
    }

    @Override
    public Log addLog(LogType type, String message, User creator) throws DALException
    {
        return logDAO.addLog(type, message, creator);
    }

    @Override
    public int getNewestLog() throws DALException
    {
        return changeDAO.getNewestLog();
    }

    @Override
    public void updateDefaultDirectory(String[] directory, String input, String output) throws DALException, IOException
    {
        propWriter.updateDefaultDirectory(directory);
        this.defaultInputDir = input;
        this.defaultOutputDir = output;
    }

    @Override
    public void addDefaultOutput(String outputValue)
    {
        this.defaultOutputDir = outputValue;
    }

    @Override
    public void addDefaultInput(String inputValue)
    {
        this.defaultInputDir = inputValue;
    }

    @Override
    public String[] getDefaultDirectories()
    {
        String[] directories = new String[2];
        directories[1] = defaultInputDir;
        directories[0] = defaultOutputDir;
        return directories;
    }

    @Override
    public boolean doesFileExist(Path file)
    {
        File realfile = file.toFile();
        return (realfile.exists() && !realfile.isDirectory());
    }
}
