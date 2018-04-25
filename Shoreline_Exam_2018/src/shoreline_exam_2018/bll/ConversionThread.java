/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexl
 */
public class ConversionThread {

    private Thread task;

    public ConversionThread() {
        runConversion();
    }
    
    public void runConversion() {
        task = new Thread(convert);
        task.start();
        try {
            task.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ConversionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Runnable convert = new Runnable() {
        @Override
        public void run() {
            //TODO converting
        }
    };
}

