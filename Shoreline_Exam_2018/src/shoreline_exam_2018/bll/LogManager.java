/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.util.List;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.DALFacade;
import shoreline_exam_2018.dal.DALManager;

/**
 * Manages Log data.
 * @author Asbamz
 */
public class LogManager
{
    DALFacade dal;

    public LogManager()
    {
        this.dal = new DALManager();
    }

    /**
     * Return all logs from data layer.
     * @return logs
     * @throws BLLException
     */
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

    /**
     * Add log to data layer.
     * @param type
     * @param message
     * @param creator
     * @return
     * @throws BLLException
     */
    public Log addLog(LogType type, String message, User creator) throws BLLException
    {
        try
        {
            return dal.addLog(type, message, creator);
        }
        catch (DALException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }
}
