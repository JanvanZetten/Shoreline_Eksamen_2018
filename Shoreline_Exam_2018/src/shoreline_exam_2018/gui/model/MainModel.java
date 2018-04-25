/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author janvanzetten
 */
public class MainModel {
    
    private File selectedFile;
    private Path to;
    private Path from;

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
}
