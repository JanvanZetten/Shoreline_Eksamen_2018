/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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
import shoreline_exam_2018.dal.database.UserDAO;
import shoreline_exam_2018.dal.filereader.Reader;
import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader_for_Big_Documents;

/**
 *
 * @author alexl
 */
public class DALManager implements DALFacade
{

    private ProfileDAO profileDAO;
    private UserDAO userDAO;
    private LogDAO logDAO;
    private DBChangeDAO changeDAO;
    private Reader reader;

    private static User currentUser;

    public DALManager()
    {
        this.profileDAO = new ProfileDAO();
        this.userDAO = new UserDAO();
        this.logDAO = new LogDAO();
        this.changeDAO = new DBChangeDAO();
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
    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path) throws DALException
    {
        reader = new XLSX_horisontal_Reader_for_Big_Documents(path.toString());
        HashMap<String, Entry<Integer, String>> hae = new HashMap();
        List<String> headers = reader.getParameters();
        InputObject inputObject = null;
        if (reader.hasNext())
        {
            inputObject = reader.getNext();
        }

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
                hae.put(headers.get(i), new SimpleImmutableEntry<>(i, str));
            }
            else
            {
                hae.put(headers.get(i), new SimpleImmutableEntry<>(i, str));
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
    public User getCurrentUser() {
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
    public int getNewestLog() throws DALException {
        return changeDAO.getNewestLog();
    }
}
