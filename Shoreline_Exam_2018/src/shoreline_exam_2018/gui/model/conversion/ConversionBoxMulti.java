package shoreline_exam_2018.gui.model.conversion;

import java.util.ArrayList;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author Alex
 */
public class ConversionBoxMulti extends TitledPane implements ConversionBoxInterface
{

    private final AnchorPane pane = new AnchorPane();
    private final ListView<ConversionBoxInterface> multiList;
    private final Profile selectedProfile;
    private ArrayList<ConversionBoxSingle> boxes;

    // Default size of ConversionBoxSingle inside a Multi.
    private final int JOB_SIZE = 56;
    // Default border size on COnversionJobMulti.
    private double paneSize = 44;

    /**
     * Creates a ConversionJobMulti, which is the container of multiple
     * ConversionJobSingles if a folder is selected to be converted from.
     * @param selectedProfile = The currently selected profile
     * @param list
     */
    public ConversionBoxMulti(Profile selectedProfile, ListView<ConversionBoxInterface> list)
    {
        super();

        this.multiList = list;
        this.selectedProfile = selectedProfile;

        AnchorPane.setBottomAnchor(multiList, 0.0);
        AnchorPane.setTopAnchor(multiList, 0.0);
        AnchorPane.setRightAnchor(multiList, 0.0);
        AnchorPane.setLeftAnchor(multiList, 0.0);
        pane.getChildren().add(this.multiList);

        this.multiList.setStyle("-fx-padding: 0px;");

        this.setContent(pane);

        this.boxes = new ArrayList<>();
        pane.setMinHeight(0);
    }

    /**
     * Retrieves the amount of boxes present in the Array and sets them.
     * @param jobs
     * @return
     */
    private double paneSize(ArrayList<ConversionBoxSingle> jobs)
    {
        for (ConversionBoxSingle job : jobs)
        {
            multiList.getItems().add(job);
            paneSize = paneSize + JOB_SIZE;
        }
        return paneSize;
    }

    /**
     * Gets the ListView.
     * @return
     */
    public ListView<ConversionBoxInterface> getListJobs()
    {
        return multiList;
    }

    /**
     * Add box to the multi conversion box
     * @param conversion
     */
    public void addBox(ConversionBoxSingle conversion)
    {
        boxes.add(conversion);

        this.setText(boxes.size() + " files with the " + selectedProfile.getName() + " profile");

        paneSize = paneSize + JOB_SIZE;

        pane.setPrefHeight(paneSize);

        multiList.getItems().add(conversion);
    }

    /**
     * Is called whenever a box has finished inside this object. Resizes the
     * AnchorPane dynamically as boxes are deleted from the list.
     * @param box
     */
    public void notifyDeletedJob(ConversionBoxSingle box)
    {
        boxes.remove(box);
        this.setText(boxes.size() + " files with the " + selectedProfile.getName() + " profile");

        paneSize = paneSize - JOB_SIZE;

        pane.setPrefHeight(paneSize);

    }

    public Profile getProfile()
    {
        return selectedProfile;
    }
}
