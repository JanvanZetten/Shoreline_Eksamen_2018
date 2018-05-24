package shoreline_exam_2018.bll;

import java.util.ArrayList;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author Alex
 */
public class ConversionJobMulti extends TitledPane implements ConversionJobs {
    
    private final AnchorPane pane = new AnchorPane();
    private final ListView<ConversionJobs> multiList;
    private final Profile selectedProfile;
    private ArrayList<ConversionJobSingle> jobs;
    
    // Default size of ConversionJobSingle inside a Multi.
    private final int JOB_SIZE = 56;
    // Default border size on COnversionJobMulti.
    private double paneSize = 44;
    
    /**
     * Creates a ConversionJobMulti, which is the container of multiple 
     * ConversionJobSingles if a folder is selected to be converted from.
     * @param selectedProfile = The currently selected profile
     * @param list
     */
    public ConversionJobMulti(Profile selectedProfile, ListView<ConversionJobs> list) {
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
    public void setupPane(ArrayList<ConversionJobSingle> jobs) {
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
    private double paneSize(ArrayList<ConversionJobSingle> jobs) {
        for (ConversionJobSingle job : jobs) {
            multiList.getItems().add(job);
            paneSize = paneSize + JOB_SIZE;
        }
        return paneSize;
    }

    /**
     * Gets the ListView.
     * @return 
     */
    public ListView<ConversionJobs> getListJobs() {
        return multiList;
    }
    
    /**
     * Add job to the coversionjobs
     * @param conversion 
     */
    public void addJob(ConversionJobSingle conversion){
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
    public void notifyDeletedJob(ConversionJobSingle job) {
        jobs.remove(job);
        this.setText(jobs.size() + " files with the " + selectedProfile.getName() + " profile");
        
        paneSize = paneSize - JOB_SIZE;
        
        pane.setPrefHeight(paneSize);
        
    }

    public Profile getProfile() {
        return selectedProfile;
    }
}
