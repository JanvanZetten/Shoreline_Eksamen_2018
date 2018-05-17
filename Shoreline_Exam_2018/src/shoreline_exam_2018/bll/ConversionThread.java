/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.MutableBoolean;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.converters.ConverterTask;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.gui.model.AlertFactory;

/**
 *
 * @author alexl
 */
public class ConversionThread
{
    private String taskName;
    private ConverterTask task;
    private Thread thread;
    private MutableBoolean isCanceled = new MutableBoolean(false);
    private MutableBoolean isOperating = new MutableBoolean(true);
    private BooleanProperty isDone;
    private ConversionJob job = null;
    private BLLFacade bll;

    /**
     * Creates listeners for a progressbar for a task and runs the task on a
     * separate thread.
     *
     * @param inputFile
     * @param outputfile
     * @param coversionProfile
     */
    public ConversionThread(String taskName, Path inputFile, Path outputfile, Profile coversionProfile) throws BLLException
    {
        this.taskName = taskName;
        bll = BLLManager.getInstance();
        this.isDone = new SimpleBooleanProperty(Boolean.FALSE);
        task = new ConverterTask(coversionProfile, inputFile, outputfile, isCanceled, isOperating, isDone);

        isDone.addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
            {
                if (newValue)
                {
                    while (true)
                    {
                        if (job != null)
                        {
                            Platform.runLater(() ->
                            {
                                job.removeFromList();
                            });
                            break;
                        }
                        else
                        {
                            try
                            {
                                Thread.sleep(10);
                            }
                            catch (InterruptedException ex)
                            {
                                Logger.getLogger(ConversionThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                    isDone.removeListener(this);
                    isDone = null;
                }
            }
        });

        startThread(task);
    }

    /**
     * Pauses the task.
     */
    public void pauseTask()
    {
        isOperating.setValue(false);
    }

    /**
     * Resumes the task after it has been paused. RESUME DOES NOT WORK
     * CURRENTLY. NEEDS IMPLEMENTATION.
     */
    public void resumeTask()
    {
        isOperating.setValue(true);
    }

    /**
     * Interrupts and cancels a conversion.
     */
    public void cancelTask()
    {
        isCanceled.setValue(true);
    }

    public Task getTask()
    {
        return task;
    }

    private void startThread(ConverterTask task)
    {
        thread = new Thread(task);
        thread.setDaemon(true);

        task.setOnFailed(e ->
        {
            Exception ex = (Exception) task.getException();

            if (ex != null)
            {
                if (ex instanceof BLLException)
                {
                    AlertFactory.showError("Conversion Error", ex.getMessage());
                    LoggingHelper.logException(ex);
                    
                    isCanceled.setValue(true);
                    job.removeFromList();
                }
            }
        });

        // On success.
        task.setOnSucceeded(e ->
        {
            isDone.setValue(Boolean.TRUE);

            try
            {
                bll.addLog(LogType.CONVERSION, "User " + bll.getcurrentUser().getName() + " has succesfully converted " + taskName, bll.getcurrentUser());
            }
            catch (BLLException ex)
            {
                AlertFactory.showError("Log Error", "Error logging to database.");
            }
        });
        thread.start();
    }

    public boolean isOperating()
    {
        return isOperating.getValue();
    }

    void giveJob(ConversionJob cJob)
    {
        job = cJob;
    }
}
