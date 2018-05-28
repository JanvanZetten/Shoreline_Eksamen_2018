/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.converters;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.LoggingHelper;
import shoreline_exam_2018.gui.model.AlertFactory;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxSingle;

/**
 *
 * @author alexl
 */
public class ConversionTaskManager
{
    private String taskName;
    private ConverterTask task;
    private Thread thread;
    private MutableBoolean isCanceled = new MutableBoolean(false);
    private MutableBoolean isOperating = new MutableBoolean(true);
    private BooleanProperty isDone;
    private ConversionBoxSingle cBox = null;
    private BLLFacade bll;

    /**
     * Creates listeners for a progressbar for a task and runs the task on a
     * separate thread.
     *
     * @param taskName
     * @param inputFile
     * @param outputfile
     * @param coversionProfile
     * @throws shoreline_exam_2018.bll.BLLException
     */
    public ConversionTaskManager(String taskName, Path inputFile, Path outputfile, Profile coversionProfile) throws BLLException
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
                        if (cBox != null)
                        {
                            Platform.runLater(() ->
                            {
                                cBox.removeFromList();
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
                                Logger.getLogger(ConversionTaskManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                    task = null;
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
     * Resumes the task after it has been paused.
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
                    cBox.removeFromList();
                }
                else
                {
                    System.out.println("Unhandled exception: " + ex.getClass() + " " + ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause());

                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    String st = "";
                    st = sw.toString(); // stack trace as a string
                    System.out.println("Exception: " + ex + " stacktrace: " + st);
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

    public void giveBox(ConversionBoxSingle cJob)
    {
        cBox = cJob;
    }
}
