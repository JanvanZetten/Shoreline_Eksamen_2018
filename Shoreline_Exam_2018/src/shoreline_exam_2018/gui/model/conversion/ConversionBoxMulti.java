package shoreline_exam_2018.gui.model.conversion;

import shoreline_exam_2018.gui.model.conversion.ConversionBoxSingle;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxInterface;

/**
 *
 * @author Alex
 */
public class ConversionBoxMulti extends TitledPane implements ConversionBoxInterface {
    
    private final AnchorPane pane = new AnchorPane();
    private final ListView<ConversionBoxInterface> multiList;
    private final Profile selectedProfile;
    private ArrayList<ConversionBoxSingle> jobs;
    
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
    public ConversionBoxMulti(Profile selectedProfile, ListView<ConversionBoxInterface> list) {
        super();
        
        this.multiList = list;
        this.selectedProfile = selectedProfile;
        
        pane.setBottomAnchor(multiList, 0.0);
        pane.setTopAnchor(multiList, 0.0);
        pane.setRightAnchor(multiList, 0.0);
        pane.setLeftAnchor(multiList, 0.0);
        pane.getChildren().add(this.multiList);
        
        this.multiList.setStyle("-fx-padding: 0px;");
        
        this.setContent(pane);
    }
    
    /**
     * Sets up the pane and Array of jobs inside this object.
     * @param jobs 
     */
    public void setupPane(ArrayList<ConversionBoxSingle> jobs) {
        this.jobs = jobs;
        
        pane.setMinHeight(0);
        pane.setPrefHeight(paneSize(jobs));
        
        this.setText(jobs.size() + " files with the " + selectedProfile.getName() + " profile");
        this.setPressed(true);
    }
    
    /**
     * Retrieves the amount of jobs present in the Array and sets them.
     * @param jobs
     * @return 
     */
    private double paneSize(ArrayList<ConversionBoxSingle> jobs) {
        for (ConversionBoxSingle job : jobs) {
            multiList.getItems().add(job);
            paneSize = paneSize + JOB_SIZE;
        }
        return paneSize;
    }

    /**
     * Gets the ListView.
     * @return 
     */
    public ListView<ConversionBoxInterface> getListJobs() {
        return multiList;
    }
    
    /**
     * Add job to the coversionjobs
     * @param conversion 
     */
    public void addJob(ConversionBoxSingle conversion){
        jobs.add(conversion);
        
        this.setText(jobs.size() + " files with the " + selectedProfile.getName() + " profile");
        
        paneSize = paneSize + JOB_SIZE;
        
        pane.setPrefHeight(paneSize);
        
        multiList.getItems().add(conversion);
    }
    
    /**
     * Is called whenever a job has finished inside this object. Resizes the
     * AnchorPane dynamically as jobs are deleted from the list.
     * @param job 
     */
    public void notifyDeletedJob(ConversionBoxSingle job) {
        jobs.remove(job);
        this.setText(jobs.size() + " files with the " + selectedProfile.getName() + " profile");
        
        paneSize = paneSize - JOB_SIZE;
        
        pane.setPrefHeight(paneSize);
        
    }

    public Profile getProfile() {
        return selectedProfile;
    }
}
