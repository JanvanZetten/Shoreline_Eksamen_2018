/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import shoreline_exam_2018.gui.model.profile.StructurePane;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.entity.StructEntityObject;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.LoggingHelper;
import shoreline_exam_2018.bll.Utilities.StructEntityUtils;
import shoreline_exam_2018.gui.controller.NewProfileController;
import shoreline_exam_2018.gui.model.profile.HeaderPane;
import shoreline_exam_2018.be.output.structure.StructEntity;

/**
 *
 * @author Asbamz
 */
public class ProfilesModel
{
    private AnchorPane anchorMain;
    private AnchorPane anchorProfiles;
    private AnchorPane anchorNewProfile;
    private Button btnNewProfile;
    private ListView<Profile> listviewProfiles;
    private TextField txtFieldProfileName;
    private TextField txtfieldSourcefile;
    private Button btnSource;
    private AnchorPane innerAnchor;
    private SplitPane splitPane;
    private AnchorPane paneHeader;
    private ScrollPane scrollMain;
    private Button btnBack;
    private Button btnDelete;
    private Button btnUpdate;
    private Tab tabConvert;
    private ConvertModel cm;

    private BLLFacade bll; // BLL Manager to contact database.
    private NewProfileModel npm; //New Profile
    private ObservableList<Profile> listProfiles;
    private StructurePane pg; // The Master Grid.
    private boolean isNext; // is rule view.
    private Profile selectedProfile;

    /**
     *
     */
    public ProfilesModel(AnchorPane anchorMain, AnchorPane anchorProfiles, AnchorPane anchorNewProfile, Button btnNewProfile, ListView<Profile> listviewProfiles, TextField txtFieldProfileName, TextField txtfieldSourcefile, Button btnSource, AnchorPane innerAnchor, SplitPane splitPane, AnchorPane paneHeader, ScrollPane scrollMain, Button btnBack, Button btnDelete, Button btnUpdate)
    {
        this.anchorMain = anchorMain;
        this.anchorProfiles = anchorProfiles;
        this.anchorNewProfile = anchorNewProfile;
        this.btnNewProfile = btnNewProfile;
        this.listviewProfiles = listviewProfiles;
        this.txtFieldProfileName = txtFieldProfileName;
        this.txtfieldSourcefile = txtfieldSourcefile;
        this.btnSource = btnSource;
        this.innerAnchor = innerAnchor;
        this.splitPane = splitPane;
        this.paneHeader = paneHeader;
        this.scrollMain = scrollMain;
        this.btnBack = btnBack;
        this.btnDelete = btnDelete;
        this.btnUpdate = btnUpdate;

        Platform.runLater(() ->
        {
            createNewProfileView();
            handleBack();
        });

        this.bll = BLLManager.getInstance();
        this.pg = new StructurePane(true);
        this.scrollMain.setContent(pg);

        this.listProfiles = FXCollections.observableArrayList();
        this.listviewProfiles.setItems(listProfiles);

        // On Profile selection Runnable.
        this.listviewProfiles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                selectedProfile = newValue;
                this.txtFieldProfileName.setText(newValue.getName());
                addHashMap(newValue.getHeadersIndexAndExamples());
                this.pg.loadProfile(newValue);
                btnDelete.setVisible(true);
                btnUpdate.setVisible(true);
            }
            else
            {
                btnDelete.setVisible(false);
                btnUpdate.setVisible(false);
            }
        });
    }

    /**
     * Shows Profile Creation View.
     */
    public void handleNewProfile()
    {
        showNewProfileView();
    }

    /**
     * Gets file path with a FileChooser and update TextField. to show the path.
     */
    public void handleSource()
    {
        Path sourceFile = chooseFile();
        if (sourceFile != null)
        {
            txtfieldSourcefile.setText(sourceFile.toString());
            getDataFromFile(sourceFile);
        }
    }

    /**
     * Shows structurePane
     */
    public void handleBack()
    {
        isNext = false;
        splitPane.setVisible(true);
        btnSource.setVisible(true);
        btnBack.setVisible(false);
        btnUpdate.setText("Next");
        innerAnchor.getChildren().clear();
        innerAnchor.getChildren().add(splitPane);
    }

    /**
     * Deletes selected Profile.
     */
    public void handleDelete()
    {
        if (selectedProfile != null)
        {
            try
            {
                bll.deleteProfile(selectedProfile);
                cm.getProfiles().remove(selectedProfile);
                bll.addLog(LogType.PROFILE, "Profile " + selectedProfile.getName() + " was successfully removed from the system.");
                AlertFactory.showInformation("Success", "The Profile was successfully been removed from the system.");
            }
            catch (BLLException ex)
            {
                AlertFactory.showWarning("Deleting Profile", "Was not able to delete profile.");
                LoggingHelper.logException(ex);
            }
        }
    }

    /**
     * Updates Profile.
     */
    public void handleUpdate()
    {
        // Gets structure from Master Grid.
        List<StructEntity> result = pg.getStructure();

        // Checks if empty.
        if (result == null || result.isEmpty())
        {
            AlertFactory.showInformation("No headers", "There was not found any headers in output structure.");
            return;
        }

        // Checks if any entity is null/not filled out.
        if (StructEntityUtils.isAnyEntryNull(result))
        {
            AlertFactory.showInformation("Empty Collection", "Some of the Collection in output structure is empty.");
            return;
        }

        // If name is not null nor empty.
        if (!txtFieldProfileName.getText().isEmpty() && txtFieldProfileName.getText() != null)
        {
            if (!isNext)
            {
                isNext = true;
                splitPane.setVisible(false);
                btnSource.setVisible(false);
                btnBack.setVisible(true);
                btnUpdate.setText("Update");
                ScrollPane sp = new ScrollPane();
                AnchorPane.setTopAnchor(sp, 0.0);
                AnchorPane.setRightAnchor(sp, 0.0);
                AnchorPane.setBottomAnchor(sp, 0.0);
                AnchorPane.setLeftAnchor(sp, 0.0);
                sp.setContent(pg.createRuleView());
                innerAnchor.getChildren().add(sp);
            }
            else
            {
                Profile selectedProfile = listviewProfiles.getSelectionModel().getSelectedItem();
                if (selectedProfile != null)
                {
                    try
                    {
                        // Update Profile in database.
                        int id = selectedProfile.getId();
                        String profileName = txtFieldProfileName.getText();
                        Profile profile = new Profile(id, profileName, new StructEntityObject(id, profileName, result), "");
                        HashMap<String, Entry<Integer, String>> headersIndexAndExamples = new HashMap<>();
                        headersIndexAndExamples.putAll(pg.getHashMap());
                        profile.setHeadersIndexAndExamples(headersIndexAndExamples);
                        profile = bll.updateProfile(profile);

                        cm.setProfile(selectedProfile, profile);

                        bll.addLog(LogType.PROFILE, "Profile " + profileName + " was successfully updated in the system.");
                        AlertFactory.showInformation("Success", "Profile has successfully been updated.");

                        handleBack();
                    }
                    catch (BLLException ex)
                    {
                        LoggingHelper.logException(ex);
                        AlertFactory.showError("Data error", "An error happened trying to update the profile.\nERROR: " + ex.getMessage());
                    }
                }
            }
        }
        else
        {
            AlertFactory.showInformation("Name missing", "The profile has to be named.");
        }
    }

    /**
     * Gets headers from file and creates the header Grid. Resets Master Grid.
     * @param path
     */
    private void getDataFromFile(Path path)
    {
        try
        {
            addHashMap(bll.getHeadersAndExamplesFromFile(path));
        }
        catch (BLLException ex)
        {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Could not get data from file", "The program was unable to get any data from " + path.toString() + ", Try another file");
        }
    }

    private void addHashMap(HashMap<String, Entry<Integer, String>> headersIndexAndExamples)
    {
        clearData();

        ObservableMap<String, Entry<Integer, String>> obsHeader = FXCollections.observableHashMap();
        obsHeader.putAll(headersIndexAndExamples);
        pg.addHashMap(obsHeader);
        HeaderPane hp = new HeaderPane(pg);

        AnchorPane.setTopAnchor(hp, 0.0);
        AnchorPane.setRightAnchor(hp, 0.0);
        AnchorPane.setBottomAnchor(hp, 0.0);
        AnchorPane.setLeftAnchor(hp, 0.0);

        paneHeader.getChildren().add(hp);
    }

    /**
     * Opens a file chooser and returns chosen path.
     */
    private Path chooseFile()
    {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Supported Files", "*.xlsx", "*.csv");
        FileChooser.ExtensionFilter xlsxfilter = new FileChooser.ExtensionFilter("XLSX Files", "*.xlsx");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        FileChooser fc = new FileChooser();

        fc.getExtensionFilters().addAll(filter, xlsxfilter, csvFilter);
        String currentDir = System.getProperty("user.dir") + File.separator;

        File dir = new File(currentDir);
        fc.setInitialDirectory(dir);
        fc.setTitle("Choose a source file.");

        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null)
        {
            return Paths.get(selectedFile.toURI());
        }
        return null;
    }

    /**
     * Clear all data for profile.
     * @param size
     */
    private void clearData()
    {
        paneHeader.getChildren().clear();
    }

    /**
     * Shows pane with ProfilesView.
     */
    void showProfilesView()
    {
        anchorProfiles.setVisible(true);
        anchorNewProfile.setVisible(false);
    }

    /**
     * Shows pane with NewProfileView.
     */
    private void showNewProfileView()
    {
        anchorProfiles.setVisible(false);
        anchorNewProfile.setVisible(true);
    }

    /**
     * Creates NewProfileView for creating new profiles.
     */
    private void createNewProfileView()
    {
        try
        {
            URL url = getClass().getResource("/shoreline_exam_2018/gui/view/NewProfileView.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Node newProfileView = (Node) loader.load();

            NewProfileController npc = (NewProfileController) loader.getController();

            Platform.runLater(() ->
            {
                npm = npc.getModel();
                npm.setProfilesModel(this);

                if (cm != null && tabConvert != null)
                {
                    npm.addSharedInfo(cm, tabConvert);
                }

                showProfilesView();
            });

            AnchorPane.setTopAnchor(newProfileView, 0.0);
            AnchorPane.setRightAnchor(newProfileView, 0.0);
            AnchorPane.setLeftAnchor(newProfileView, 0.0);
            AnchorPane.setBottomAnchor(newProfileView, 0.0);

            anchorNewProfile.getChildren().setAll(newProfileView);
        }
        catch (MalformedURLException ex)
        {
            LoggingHelper.logException(ex);
            AlertFactory.showError("An error has occured", "Error: " + ex.getMessage());
        }
        catch (IOException ex)
        {
            LoggingHelper.logException(ex);
            AlertFactory.showError("An error has occured", "Error: " + ex.getMessage());
        }
    }

    /**
     * Adds convert model and tab.
     * @param cm
     * @param tabConvert
     */
    public void addSharedInfo(ConvertModel cm, Tab tabConvert)
    {
        this.tabConvert = tabConvert;
        this.cm = cm;

        if (npm != null)
        {
            npm.addSharedInfo(this.cm, this.tabConvert);
        }

        // Run method each time Observable List is changed.
        this.cm.getProfiles().addListener((ListChangeListener.Change<? extends Profile> c) ->
        {
            onProfilesListUpdate(this.cm.getProfiles());
        });

        onProfilesListUpdate(this.cm.getProfiles());
    }

    /**
     * Method updating Observable List with Profiles.
     * @param profiles
     */
    private void onProfilesListUpdate(ObservableList<Profile> profiles)
    {
        listProfiles.clear();
        listProfiles.addAll(profiles);
        Collections.sort(listProfiles);
        listviewProfiles.getSelectionModel().selectFirst();
    }
}
