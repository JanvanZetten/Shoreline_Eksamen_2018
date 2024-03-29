package shoreline_exam_2018.gui.model.conversion;

import shoreline_exam_2018.bll.converters.ConversionTaskManager;
import java.nio.file.Path;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import shoreline_exam_2018.gui.model.AlertFactory;

/**
 *
 * @author alexl
 */
public class ConversionBoxSingle extends HBox implements ConversionBoxInterface {

    private Label lblConversionName;
    private ProgressBar progress;
    private Button btnPause;
    private Button btnCancel;
    private ListView<ConversionBoxInterface> listJobs;
    private ConversionBoxMulti multi;

    private int BUTTON_SIZE = 36;

    /**
     * Creates a visual task that the user is able to see. Shows progress of a
     * task currently running.
     *
     * @param conversionName
     * @param cThread
     * @param outputPath
     * @param selectedProfile
     * @param listJobs
     * @param multi
     */
    public ConversionBoxSingle(
            String conversionName,
            ConversionTaskManager cThread,
            Path outputPath,
            Profile selectedProfile,
            ListView<ConversionBoxInterface> listJobs,
            ConversionBoxMulti multi) {
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
        setCancelButtonInfo(cThread, listJobs, selectedProfile);
        setGridInfo(grid);

        this.getChildren().addAll(grid);

        this.multi = multi;
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
        lblConversionName.setPrefWidth(300);
    }

    /**
     * Sets all the information of the progress bar.
     *
     * @param cThread
     */
    private void setProgressBarInfo(ConversionTaskManager cThread) {
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
    private void setPauseButtonInfo(ConversionTaskManager cThread) {
        btnPause.setStyle("-fx-background-color: transparent;");
        Image imagePause = new Image("shoreline_exam_2018/resources/pause.png", BUTTON_SIZE, BUTTON_SIZE, true, true);
        ImageView imageViewPause = new ImageView(imagePause);
        btnPause.setGraphic(imageViewPause);

        Image imageResume = new Image("shoreline_exam_2018/resources/resume.png", BUTTON_SIZE, BUTTON_SIZE, true, true);
        ImageView imageViewResume = new ImageView(imageResume);

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
    private void setCancelButtonInfo(ConversionTaskManager cThread, ListView<ConversionBoxInterface> listJobs, Profile selectedProfile) {
        btnCancel.setStyle("-fx-background-color: transparent;");
        Image image = new Image("shoreline_exam_2018/resources/stop.png", BUTTON_SIZE, BUTTON_SIZE, true, true);
        ImageView imageView = new ImageView(image);
        btnCancel.setGraphic(imageView);

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Optional<ButtonType> result = AlertFactory.showConfirmation("Cancelling confirmation", "Are you sure you want to cancel the conversion of " + lblConversionName.getText() + " using the " + selectedProfile.getName() + " profile?");
                if (result.get() == ButtonType.YES) {
                    cThread.cancelTask();
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
        filler1.setMaxWidth(50);
        filler1.setMinWidth(50);

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

    /**
     * Removes itself from the list given in the constructor.
     */
    public void removeFromList() {
        if (multi != null) {
            multi.notifyDeletedJob(this);
        }
        listJobs.getItems().remove(ConversionBoxSingle.this);
    }
}
