/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import shoreline_exam_2018.bll.converters.XLSXtoJSONTask;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import shoreline_exam_2018.be.MutableBoolean;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author alexl
 */
public class ConversionThread {

    private Task task;
    private Thread thread;
    private MutableBoolean isCanceled = new MutableBoolean(false);
    private MutableBoolean isOperating = new MutableBoolean(true);
    private BooleanProperty isDone;
    private ConversionJob job = null;

    /**
     * Creates listeners for a progressbar for a task and runs the task on a
     * separate thread.
     *
     * @param inputFile
     * @param outputfile
     * @param coversionProfile
     */
    public ConversionThread(Path inputFile, Path outputfile, Profile coversionProfile) {
        this.isDone = new SimpleBooleanProperty(Boolean.FALSE);
        task = new XLSXtoJSONTask(coversionProfile, inputFile, outputfile, isCanceled, isOperating, isDone);

        isDone.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    while (true) {
                        if (job != null) {
                            Platform.runLater(() -> {
                                job.conversionDone();
                            });
                            break;
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
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
    public void pauseTask() {
        isOperating.setValue(false);
    }

    /**
     * Resumes the task after it has been paused. RESUME DOES NOT WORK
     * CURRENTLY. NEEDS IMPLEMENTATION.
     */
    public void resumeTask() {
        isOperating.setValue(true);
    }

    /**
     * Interrupts and cancels a conversion.
     */
    public void cancelTask() {
        isCanceled.setValue(true);
        job.conversionDone();
    }

    public Task getTask() {
        return task;
    }

    private void startThread(Task task) {
        thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public boolean isOperating() {
        return isOperating.getValue();
    }

    void giveJob(ConversionJob cJob) {
        job = cJob;
    }
}
