/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import javafx.application.Platform;
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
    private ConversionInterface converter;

    private MutableBoolean isCanceled = new MutableBoolean(false);
    private MutableBoolean isOperating = new MutableBoolean(true);
    private ConversionJob job = null;
    

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
        
        startThread(task);
    }

    /**
     * Runs the convert function on a separate thread.
     */
    private Task runConversion(Path inputFile, Path outputfile, Profile coversionProfile) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                
                try{
                converter.convertFile(coversionProfile, inputFile, outputfile, isCanceled, isOperating);
                
                while(true){
                    if (job != null){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                job.conversionDone();
                            }
                        });
                        break;
                    }
                    else {
                        Thread.sleep(10);
                    }
                    
                }
                
                }
                catch(BLLExeption ex){
                    ex.printStackTrace();
                }
                
                return null;
            }
        };
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
        this.job = cJob;
    }
}
