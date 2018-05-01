/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.bll.ConversionTask;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author janvanzetten
 */
public class MainModel {

    private File selectedFile;
    private Path selectedFilePath;
    private Profile selectedProfile;
    private String taskName;

    private BLLFacade bll;

    private List<ConversionTask> tblTasks;
    private ObservableList<ConversionTask> olTasks;

    public MainModel() {
        bll = new BLLManager();
    }

    /**
     * Sets array and observable lists for future use to place tasks into view.
     *
     * @param tblTasks
     */
    public void prepareTasks() {
        tblTasks = new ArrayList<>();
        olTasks = FXCollections.observableArrayList();
    }

    /**
     * Opens a file chooser and sets a File object to be the selected file.
     */
    public void chooseFile() {
        ExtensionFilter filter = new ExtensionFilter("XLSX Files", "*.xlsx");
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().add(filter);
        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        fc.setInitialDirectory(dir);
        fc.setTitle("Attach a file");

        selectedFile = fc.showOpenDialog(null);
        //CODE BELOW NEEDS TO BE CHANGED AS IT CURRENTLY STARTS A TASK THE INSTANT 
        //YOU SELECT A FILE. THERE NEEDS TO BE A BUTTON THAT SAYS "START" INSTEAD
        if (selectedFile != null) {
            selectedFilePath = Paths.get(selectedFile.toURI());
        }
    }

    public void convertTest(ListView<ConversionTask> tblTasks) {
        this.tblTasks.add(bll.setConversionFilePath(taskName, selectedFilePath, selectedProfile));
        olTasks.addAll(this.tblTasks);
        tblTasks.setItems(olTasks);
    }

    /**
     * Sets the tabs in all the tabs of the MainView.
     *
     * @param paneConvert
     * @param paneProfiles
     * @param paneLog
     * @param paneSettings
     */
    public void setupTabs(AnchorPane paneConvert, AnchorPane paneProfiles, AnchorPane paneLog, AnchorPane paneSettings) {
        setPane("Convert", paneConvert);
        setPane("Profiles", paneProfiles);
        setPane("Log", paneLog);
        setPane("Settings", paneSettings);
    }

    private void setPane(String PANE_NAME, AnchorPane PANE) {
        try {
            URL url = new File(System.getProperty("user.dir") + "\\src\\shoreline_exam_2018\\gui\\view\\" + PANE_NAME + "View.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);

            Node node = (Node) loader.load();
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            PANE.getChildren().setAll(node);
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
