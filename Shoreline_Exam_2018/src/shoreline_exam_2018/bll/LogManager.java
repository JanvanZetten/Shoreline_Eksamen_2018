/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    ObservableList<Log> logList;

    public LogManager()
    {
        this.dal = new DALManager();
        logList = FXCollections.observableArrayList();
    }

    /**
     * Returns reference to observable list with logs.
     * @return
     */
    public ObservableList<Log> getObsLogList()
    {
        return logList;
    }

    /**
     * Get all logs from data layer and saves it in an observable list.
     * @throws BLLExeption
     */
    public void getAllLogs() throws BLLExeption
    {
        try
        {
            logList.addAll(dal.getAllLogs());
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Add log to data layer.
     * @param type
     * @param message
     * @param creator
     * @return
     * @throws BLLExeption
     */
    public Log addLog(LogType type, String message, User creator) throws BLLExeption
    {
        try
        {
            Log log = dal.addLog(type, message, creator);
            logList.add(log);
            return log;
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }
}
