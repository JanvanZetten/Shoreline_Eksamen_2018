/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
    private ListView<ConversionJob> listJobs;

    private int BUTTON_SIZE = 36;

    /**
     * Creates a visual task that the user is able to see. Shows progress of a
     * task currently running.
     *
     * @param conversionName
     * @param cThread
     */
    public ConversionJob(
            String conversionName,
            ConversionThread cThread,
            Path outputPath,
            Profile selectedProfile,
            ListView<ConversionJob> listJobs) {
        super();

        this.listJobs = listJobs;

        // Creates all elements
        lblConversionName = new Label();
        progress = new ProgressBar();
        btnPause = new Button();
        btnCancel = new Button();
        GridPane grid = new GridPane();

        this.setStyle("-fx-background-color: #737f8c;"
                + "-fx-background-radius: 10;");

        setLabelInfo(conversionName);
        setProgressBarInfo(cThread);
        setPauseButtonInfo(cThread);
        setCancelButtonInfo(cThread, listJobs, selectedProfile, outputPath);
        setGridInfo(grid);

        this.getChildren().addAll(grid);
    }

    /**
     * Sets all the information of the label.
     *
     * @param conversionName
     */
    private void setLabelInfo(String conversionName) {
        lblConversionName.setText(conversionName);
        lblConversionName.setId("WHITE");
        lblConversionName.setStyle("-fx-font-size: 20px");
    }

    /**
     * Sets all the information of the progress bar.
     *
     * @param cThread
     */
    private void setProgressBarInfo(ConversionThread cThread) {
        progress.setProgress(0);
        progress.progressProperty().unbind();
        progress.progressProperty().bind(cThread.getTask().progressProperty());
        progress.setMaxWidth(Double.MAX_VALUE);
    }

    /**
     * Sets all the information of the pause button. Sets itself to become a
     * resume button when pressed, and vice versa.
     *
     * @param cThread
     */
    private void setPauseButtonInfo(ConversionThread cThread) {
        btnPause.setStyle("-fx-background-color: transparent;");
        Image imagePause = new Image("shoreline_exam_2018/resources/pause.png", BUTTON_SIZE, BUTTON_SIZE, true, true);
        ImageView imageViewPause = new ImageView(imagePause);
        btnPause.setGraphic(imageViewPause);

        Image imageResume = new Image("shoreline_exam_2018/resources/resume.png", BUTTON_SIZE, BUTTON_SIZE, true, true);
        ImageView imageViewResume = new ImageView(imageResume);

        //NOT WORKING.
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (cThread.isOperating() == true) {
                    cThread.pauseTask();
                    btnPause.setGraphic(imageViewResume);
                } else if (cThread.isOperating() == false) {
                    cThread.resumeTask();
                    btnPause.setGraphic(imageViewPause);
                }
            }
        });
    }

    /**
     * Sets all the information of the cancel button.
     *
     * @param cThread
     */
    private void setCancelButtonInfo(ConversionThread cThread, ListView<ConversionJob> listJobs, Profile selectedProfile, Path outputPath) {
        btnCancel.setStyle("-fx-background-color: transparent;");
        Image image = new Image("shoreline_exam_2018/resources/stop.png", BUTTON_SIZE, BUTTON_SIZE, true, true);
        ImageView imageView = new ImageView(image);
        btnCancel.setGraphic(imageView);

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Cancel confirmation");
                alert.setHeaderText("Cancelling conversion");
                alert.setContentText("Are you sure you want to cancel the conversion of " + lblConversionName.getText() + " using the " + selectedProfile.getName() + " profile?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    try {
                        cThread.cancelTask();
                        listJobs.getItems().remove(ConversionJob.this);
                        //Deletes the output file from the outputPath.
                        Files.delete(outputPath);
                    } catch (IOException ex) {
                        //DO NOTHING
                    }
                } 
            }
        });
    }

    /**
     * Sets all information inside the GridPane and scales whatever needs
     * scaling.
     *
     * @param grid
     */
    private void setGridInfo(GridPane grid) {
        this.setHgrow(grid, Priority.ALWAYS);

        Region filler1 = new Region();
        filler1.setMaxWidth(100);
        filler1.setMinWidth(100);
        
        Region filler2 = new Region();
        filler2.setMaxWidth(100);
        filler2.setMinWidth(100);

        grid.addColumn(0, filler1);
        grid.add(lblConversionName, 1, 0);
        grid.add(progress, 2, 0);
        grid.addColumn(3, filler2);
        grid.add(btnPause, 4, 0);
        grid.add(btnCancel, 5, 0);

        ColumnConstraints neverGrow = new ColumnConstraints();
        neverGrow.setHgrow(Priority.NEVER);

        ColumnConstraints alwaysGrow = new ColumnConstraints();
        alwaysGrow.setHgrow(Priority.ALWAYS);
        
        grid.getColumnConstraints().addAll(
                neverGrow,
                alwaysGrow,
                alwaysGrow,
                neverGrow,
                neverGrow,
                neverGrow);
    }
    
}
