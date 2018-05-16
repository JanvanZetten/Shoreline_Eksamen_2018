/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.dal.database.LogDAO;
import shoreline_exam_2018.dal.database.ProfileDAO;
import shoreline_exam_2018.dal.database.UserDAO;

/**
 *
 * @author alexl
 */
public class DALManager implements DALFacade
{

    private ProfileDAO profileDAO;
    private UserDAO userDAO;
    private LogDAO logDAO;
    private XLSX_horisontal_Reader xhr;

    private User currentUser;

    public DALManager()
    {
        this.profileDAO = new ProfileDAO();
        this.userDAO = new UserDAO();
        this.logDAO = new LogDAO();
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
        xhr = new XLSX_horisontal_Reader(path.toString());
        HashMap<String, Entry<Integer, String>> hae = new HashMap();
        List<String> headers = xhr.getParameters();
        Row row = null;
        if (xhr.hasNext())
        {
            row = xhr.getNextRow();
        }

        for (int i = 0; i < headers.size(); i++)
        {
            String str = "";
            if (row != null)
            {
                Cell currentCell = row.getCell(i);
                if (currentCell != null)
                {
                    switch (currentCell.getCellTypeEnum())
                    {
                        case STRING:
                            str = currentCell.getStringCellValue();
                            break;
                        case BOOLEAN:
                            str = Boolean.toString(currentCell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            str = currentCell.getCellFormula();
                            break;
                        case NUMERIC:
                            str = Double.toString(currentCell.getNumericCellValue());
                            break;
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
    public List<Log> getAllLogs() throws DALException
    {
        return logDAO.getAllLogs();
    }

    @Override
    public Log addLog(LogType type, String message, User creator) throws DALException
    {
        return logDAO.addLog(type, message, creator);
    }
}
