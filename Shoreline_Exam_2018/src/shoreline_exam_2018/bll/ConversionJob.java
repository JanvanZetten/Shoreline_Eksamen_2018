/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author alexl
 */
public class ConversionJob extends HBox {
    
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
    public ConversionJob(String conversionName, ConversionThread cThread, Path selectedFilePath, Profile selectedProfile) {
        super();

        // Creates all elements
        lblConversionName = new Label();
        progress = new ProgressBar();
        btnPause = new Button();
        btnCancel = new Button();
        
        this.setStyle("-fx-background: #FFFFFF;");
        
        setLabelInfo(conversionName);
        setProgressBarInfo(cThread);
        setPauseButtonInfo(cThread);
        setCancelButtonInfo(cThread);

        this.getChildren().addAll(lblConversionName, progress, btnPause, btnCancel);
    }
    
    /**
     * Sets all the information of the label.
     * @param conversionName 
     */
    private void setLabelInfo(String conversionName) {
        lblConversionName.setText(conversionName);
    }
    
    /**
     * Sets all the information of the progress bar.
     * @param cThread 
     */
    private void setProgressBarInfo(ConversionThread cThread) {
        progress.setProgress(0);
        progress.progressProperty().unbind();
        progress.progressProperty().bind(cThread.getTask().progressProperty());
    }
    
    /**
     * Sets all the information of the pause button. Sets itself to become a 
     * resume button when pressed, and vice versa.
     * @param cThread 
     */
    private void setPauseButtonInfo(ConversionThread cThread) {
        Image imagePause = new Image("shoreline_exam_2018/resources/pause.png", 36, 36, true, true);
        ImageView imageViewPause = new ImageView(imagePause);
        btnPause.setGraphic(imageViewPause);
        
        Image imageResume = new Image("shoreline_exam_2018/resources/resume.png", 36, 36, true, true);
        ImageView imageViewResume = new ImageView(imageResume);
        
        //NOT WORKING.
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cThread.isOperating() == true) {
                    cThread.pauseTask();
                    btnPause.setGraphic(imageViewResume);
                }
                else if (cThread.isOperating() == false) {
                    cThread.resumeTask();
                    btnPause.setGraphic(imageViewPause);
                }
            }
        });
    }

    /**
     * Sets all the information of the cancel button.
     * @param cThread 
     */
    private void setCancelButtonInfo(ConversionThread cThread) {
        Image image = new Image("shoreline_exam_2018/resources/stop.png", 36, 36, true, true);
        ImageView imageView = new ImageView(image);
        btnCancel.setGraphic(imageView);
        
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cThread.cancelTask();
            }
        });
    }
}
