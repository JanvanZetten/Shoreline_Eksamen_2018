/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.bll.BLLExeption;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author janvanzetten
 */
public class LogModel
{

    ObservableList<Log> logList;
    BLLFacade bll = BLLManager.getInstance();
    boolean run = true;

    public LogModel()
    {
        logList = FXCollections.observableArrayList();
    }

    public void loadLogItems()
    {
        logList.clear();
        try
        {
            logList.addAll(bll.getAllLogs());
        }
        catch (BLLExeption ex)
        {
            AlertFactory.showError("Could not load Log items", ex.getMessage());
        }
    }

    public ObservableList<Log> getLogItems()
    {
        return logList;
    }

}
