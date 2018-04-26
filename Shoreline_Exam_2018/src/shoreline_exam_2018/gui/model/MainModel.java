/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import shoreline_exam_2018.bll.ConversionTask;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author janvanzetten
 */
public class MainModel {

    private File selectedFile;
    private Path to;
    private Path from;
    private BLLFacade bll;
    
    private List<ConversionTask> tblTasks;
    private ObservableList<ConversionTask> olTasks;

    public MainModel() {
        bll = new BLLManager();
    }

    /**
     * Sets array and observable lists for future use to place tasks into view.
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
        System.out.println(selectedFile);

//        if (selectedFile != null) {
//            from = Paths.get(selectedFile.toURI());
//            to = Paths.get(dir + "/music/" + selectedFile.getName());
//    }
    }

    public void setTask(ListView<ConversionTask> tblTasks) {
        this.tblTasks.add(bll.setNewTask());
        olTasks.addAll(this.tblTasks);
        tblTasks.setItems(olTasks);
    }
}
