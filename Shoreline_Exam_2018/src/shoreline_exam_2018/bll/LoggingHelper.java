/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.DALFacade;
import shoreline_exam_2018.dal.DALManager;

/**
 *
 * @author janvanzetten
 */
public class LoggingHelper {
    
    private static DALFacade dal = new DALManager();
    
    
    /**
     * sends a message to the log with the message from the exception
     * @param ex 
     */
    public static void logException(Exception ex){
        logException(ex.getMessage());
    }
    
    /**
     * sends a message to the log with the message given
     * @param Message 
     */
    public static void logException(String Message){
        User currentUser = dal.getCurrentUser();
        try {
            dal.addLog(LogType.ERROR, currentUser + "had an error: " + Message, currentUser);
        } catch (DALException ex1) {
            Logger.getLogger(LoggingHelper.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }
}
