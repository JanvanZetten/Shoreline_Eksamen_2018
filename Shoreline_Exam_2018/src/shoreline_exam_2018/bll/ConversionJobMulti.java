/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author Alex
 */
public class ConversionJobMulti extends TitledPane implements ConversionJobs {
    
    private AnchorPane pane = new AnchorPane();
    private ListView<ConversionJobs> listJobs;
    private double paneSize = 44;
    private Profile selectedProfile;
    private ArrayList<ConversionJobSingle> jobs;
    
    private int JOB_SIZE = 56;

    public ConversionJobMulti(Profile selectedProfile, ListView<ConversionJobs> listView) {
        super();
        
        this.listJobs = listView;
        this.selectedProfile = selectedProfile;
        
        pane.setBottomAnchor(listJobs, 0.0);
        pane.setTopAnchor(listJobs, 0.0);
        pane.setRightAnchor(listJobs, 0.0);
        pane.setLeftAnchor(listJobs, 0.0);
        pane.getChildren().add(listJobs);
        
        listJobs.setStyle("-fx-padding: 0px;");
        
        this.setContent(pane);
    }
    
    public void setupPane(ArrayList<ConversionJobSingle> jobs) {
        this.jobs = jobs;
        
        pane.setMinHeight(0);
        pane.setPrefHeight(paneSize(jobs));
       
        setupJobs(jobs);
        
        this.setText(jobs.size() + " files with the " + selectedProfile.getName() + " profile");
        this.setPressed(true);
    }

    private void setupJobs(ArrayList<ConversionJobSingle> jobs) {
        for (ConversionJobSingle job : jobs) {
            listJobs.getItems().add(job);
        }
    }

    private double paneSize(ArrayList<ConversionJobSingle> jobs) {
        for (ConversionJobSingle job : jobs) {
            paneSize = paneSize + JOB_SIZE;
        }
        return paneSize;
    }

    public ListView<ConversionJobs> getListJobs() {
        return listJobs;
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
        
        listJobs.getItems().add(conversion);
    }
    
    public void notifyDeletedJob(ConversionJobSingle job) {
        jobs.remove(job);
        this.setText(jobs.size() + " files with the " + selectedProfile.getName() + " profile");
        
        paneSize = paneSize - JOB_SIZE;
        
        pane.setPrefHeight(paneSize);
    }
}
