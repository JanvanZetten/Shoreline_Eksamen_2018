/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

/**
 *
 * @author alexl
 */
public class ConversionTask extends HBox {
    
    private Label lblConversionName;
    private ProgressBar progress;
    private Button btnPause;
    private Button btnCancel;

    /**
     * Creates a visual task that the user is able to see. Shows progress of a
     * task currently running.
     * @param conversionName
     * @param cThread 
     */
    public ConversionTask(String conversionName, ConversionThread cThread) {
        super();

        lblConversionName = new Label();
        progress = new ProgressBar();
        btnPause = new Button();
        btnCancel = new Button();
        
        lblConversionName.setText(conversionName);
        
        progress.setProgress(0);
        progress.progressProperty().unbind();
        progress.progressProperty().bind(cThread.getTask().progressProperty());
        
        btnPause.setText("PAUSE");
        
        //Pauses the running thread. NOT WORKING.
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cThread.pauseTask();
                //NEEDS TO SET RESUME BUTTON TOO
            }
        });
        
        btnCancel.setText("CANCEL");
        
        //Cancels the running thread.
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cThread.cancelTask();
            }
        });

        this.getChildren().addAll(lblConversionName, progress, btnPause, btnCancel);
    }
}
