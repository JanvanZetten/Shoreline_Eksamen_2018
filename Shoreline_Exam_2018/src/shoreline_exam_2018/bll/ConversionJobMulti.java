/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author Alex
 */
public class ConversionJobMulti extends TitledPane implements ConversionJobs {
    
    private ListView<ConversionJobs> listJobs = new ListView<ConversionJobs>();

    public ConversionJobMulti(Profile selectedProfile, Node content, ArrayList<ConversionJobSingle> jobs) {
        super();
        
        AnchorPane pane = new AnchorPane();
        
        pane.setBottomAnchor(listJobs, 0.0);
        pane.setTopAnchor(listJobs, 0.0);
        pane.setRightAnchor(listJobs, 0.0);
        pane.setLeftAnchor(listJobs, 0.0);
        pane.getChildren().add(listJobs);
        
        this.setStyle("-fx-background-color: #737f8c;"
                + "-fx-background-radius: 10;");
        
        setupJobs(jobs);
        
        this.setText(jobs.size() + " files with the " + selectedProfile.getName() + " profile");
        this.setPressed(true);
        this.getChildren().add(pane);
    }

    private void setupJobs(ArrayList<ConversionJobSingle> jobs) {
        for (ConversionJobSingle job : jobs) {
            listJobs.getItems().add(job);
        }
    }
    
    
    
}
