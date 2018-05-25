package shoreline_exam_2018.gui.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.LoggingHelper;
import shoreline_exam_2018.gui.controller.ConvertController;
import shoreline_exam_2018.gui.controller.ProfilesController;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author janvanzetten
 */
public class MainModel {

    private Tab tabConvert;
    private ConvertModel cm;
    private BLLFacade bll;

    public MainModel() {
        bll = BLLManager.getInstance();
        File f = new File("properties.properties");
        if (f.exists() && !f.isDirectory()) {
            PropertiesReader propRead = new PropertiesReader();
        } else {
            createNewProperties();
        }
    }

    /**
     * Creates a new properties file.
     */
    private void createNewProperties() {
        FileOutputStream fileOut = null;
        try {
            File file = new File("properties.properties");
            fileOut = new FileOutputStream(file);
            Properties props = new Properties();

            createDefaultOutputDir(props);
            createDefaultInputDir(props);
            createDefaultProfile(props);

            props.store(fileOut, null);
            fileOut.close();
        } catch (FileNotFoundException ex) {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Could not create properties", "The directory could not be found: " + ex.getMessage());
        } catch (IOException ex) {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Could not create properties", ex.getMessage());
        }
    }

    /**
     * Creates the default output directory when a new properties file is made.
     *
     * @param props
     */
    private void createDefaultOutputDir(Properties props) {
        String[] directory = new String[2];
        directory[0] = "outputDir";
        directory[1] = System.getProperty("user.dir") + File.separator;
        directory[1] = directory[1].substring(0, directory[1].length() - 1);
        props.setProperty(directory[0], directory[1]);
        bll.addDefaultOutput(directory[1]);
    }

    /**
     * Creates the default input directory when a new properties file is made.
     *
     * @param props
     */
    private void createDefaultInputDir(Properties props) {
        String[] directory = new String[2];
        directory[0] = "inputDir";
        directory[1] = System.getProperty("user.dir") + File.separator;
        directory[1] = directory[1].substring(0, directory[1].length() - 1);
        props.setProperty(directory[0], directory[1]);
        bll.addDefaultInput(directory[1]);
    }
    
    private void createDefaultProfile(Properties props) {
        try {
            String[] directory = new String[2];
            directory[0] = "defaultProfile";
            directory[1] = -1 + "";
            props.setProperty(directory[0], directory[1]);
            bll.addDefaultProfile(directory);
        } catch (BLLException ex) {
            AlertFactory.showError("Couldn't create properties", "Error: " + ex.getMessage());
        }
    }

    /**
     * Sets the tabs in all the tabs of the MainView.
     * @param paneConvert = The conversion view.
     * @param paneProfiles = The profiles view.
     * @param paneLog = The log view.
     * @param paneSettings = The settings view.
     */
    public void setupTabs(AnchorPane paneConvert, AnchorPane paneProfiles, AnchorPane paneLog, AnchorPane paneSettings, Tab tabConvert) {
        setPane("Convert", paneConvert);
        setPane("Profiles", paneProfiles);
        setPane("Log", paneLog);
        setPane("Settings", paneSettings);
        this.tabConvert = tabConvert;
    }

    /**
     * Re-scales the views to have the same anchors as the tab panes they are
     * located in. Also sets a node of the view into the tab pane.
     * @param PANE_NAME = Name of the tab.
     * @param PANE = Name of the pane.
     */
    private void setPane(String PANE_NAME, AnchorPane PANE) {
        try {
            URL url = getClass().getResource("/shoreline_exam_2018/gui/view/" + PANE_NAME + "View.fxml");
            FXMLLoader loader = new FXMLLoader(url);

            Node node = (Node) loader.load();

            if (PANE_NAME.equalsIgnoreCase("Convert")) {
                ConvertController cc = (ConvertController) loader.getController();
                Platform.runLater(()
                        -> {
                    cm = cc.getModel();
                });
            } else if (PANE_NAME.equalsIgnoreCase("Profiles")) {
                ProfilesController pc = (ProfilesController) loader.getController();
                Platform.runLater(()
                        -> {
                    pc.addSharedInfo(cm, tabConvert);
                });
            }

            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);

            PANE.getChildren().setAll(node);
        } catch (MalformedURLException ex) {
            LoggingHelper.logException(ex);
            AlertFactory.showError("An error has occured", "Error: " + ex.getMessage());
        } catch (IOException ex) {
            LoggingHelper.logException(ex);
            AlertFactory.showError("An error has occured", "Error: " + ex.getMessage());
        }
    }

    /**
     * Sets tab for Convert View.
     * @param tabConvert
     */
    public void setTabConvert(Tab tabConvert) {
        this.tabConvert = tabConvert;
    }

    /**
     * Gives ConvertModel the ability to see if the default output directory has changed.
     */
    public void hasDefaultDirChanged() {
        cm.hasDefaultDirChanged();
    }
}
