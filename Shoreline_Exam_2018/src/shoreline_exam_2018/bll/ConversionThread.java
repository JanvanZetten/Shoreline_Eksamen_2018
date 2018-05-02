/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author alexl
 */
public class ConversionThread {

    private Task task;
    private Thread thread;
    private ConversionInterface converter;

    private boolean isCanceled = false;
    private boolean isOperating = true;
    private final Profile coversionProfile;
    

    /**
     * Creates listeners for a progressbar for a task and runs the task on a
     * separate thread.
     * @param converter
     * @param inputFile
     * @param outputfile
     * @param coversionProfile
     */
    public ConversionThread(ConversionInterface converter, Path inputFile, Path outputfile, Profile coversionProfile) {
        this.converter = converter;
        task = runConversion(inputFile, outputfile, coversionProfile);

        thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        this.coversionProfile = coversionProfile;
    }

    /**
     * Runs the convert function on a separate thread.
     */
    private Task runConversion(Path inputFile, Path outputfile, Profile coversionProfile) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                
                converter.convertFile(coversionProfile, inputFile, outputfile);
                
                return null;
            }
        };
    }

    /**
     * Pauses the task.
     */
    public void pauseTask() {
        isOperating = false;
    }

    /**
     * Resumes the task after it has been paused. RESUME DOES NOT WORK
     * CURRENTLY. NEEDS IMPLEMENTATION.
     */
    public void resumeTask() {
        isOperating = true;
    }

    /**
     * Interrupts and cancels a conversion.
     */
    public void cancelTask() {
        isCanceled = true;
        //STILL NEEDS TO BE ABLE TO DELETE THE ATTEMPTED CREATED JSON FILE
    }

    public Task getTask() {
        return task;
    }

}
