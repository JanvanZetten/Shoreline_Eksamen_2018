/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author alexl
 */
public class SettingsModel {
    
    private BLLFacade bll;
    
    public SettingsModel() {
        bll = BLLManager.getInstance();
    }

    public void newDefaultDir(String dirType, TextField txtfieldDir) {
        DirectoryChooser dc = new DirectoryChooser();
        String[] directory = new String[2];
        directory[0] = dirType;

        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        dc.setInitialDirectory(dir);
        dc.setTitle("Choose a directory");
        
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            try {
                directory[1] = selectedFile.toString();
                txtfieldDir.setText(directory[1]);
                bll.updateDefaultDirectory(directory);
            } catch (BLLException ex) {
                AlertFactory.showError("The config could not be updated", "Error: " + ex.getMessage());
            }
        }
    }

    public void setSettingsDefaultDirectories(TextField txtfieldInputDir, TextField txtfieldOutputDir) {
        String[] string = bll.getDefaultDirectories();
        txtfieldInputDir.setText(string[0]);
        txtfieldOutputDir.setText(string[1]);
    }
    
    
}
