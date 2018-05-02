/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

/**
 *
 * @author alexl
 */
public class ConversionThread {

    private Task task;
    private Thread thread;

    private boolean isCanceled = false;
    private boolean isOperating = true;

    private ConversionTask cTask;

    /**
     * Creates listeners for a progressbar for a task and runs the task on a
     * separate thread.
     */
    public ConversionThread() {
        task = runConversion();
        task.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
            }
        });
        thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Runs the convert function on a separate thread.
     */
    private Task runConversion() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 100000000000000l; i++) {
                    System.out.println("SPAM " + i); //NEEDS TO HAVE CONVERSION METHOD HERE
                    if (isOperating == true) {
                        if (isCanceled == true) {
                            return true;
                        }
                    }
                    
                }
                return true;
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

    void setTask(ConversionTask cTask) {
        this.cTask = cTask;
    }
}
