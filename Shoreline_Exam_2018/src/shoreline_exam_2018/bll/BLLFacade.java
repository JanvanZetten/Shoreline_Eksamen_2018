/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.gui.model.AutoUpdater;

/**
 *
 * @author janvanzetten
 */
public interface BLLFacade
{

    /**
     * Starts a conversion with the given input and output file using the given
     * profile.
     * @param taskName = Name for the conversion job.
     * @param inputFile = The input file that is being converted.
     * @param outputFile = The output file that is being converted to.
     * @param profile = The profile that is used for the conversion.
     * @return = A converison job.
     * @throws BLLException
     */
    public ConversionJob startConversion(String taskName, Path inputFile, Path outputFile, Profile profile, ListView<ConversionJob> listJobs) throws BLLException;

    /**
     * Adds a profile to the database.
     * @param name = Name of the profile.
     * @param structure = The StructEntityObject that determines how it should
     * convert.
     * @param createdBy = ID of the user who created the profile.
     * @return = The profile that was made with this method.
     * @throws BLLException
     */
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws BLLException;

    /**
     * Gets all profiles from the database and returns them in a list.
     * @return
     * @throws BLLException
     */
    public List<Profile> getAllProfiles() throws BLLException;

    /**
     * Adds a Default structure to the data layer.
     * @param name
     * @param structure
     * @param createdBy
     * @return
     * @throws BLLException
     */
    public StructEntityObject addStructure(String name, StructEntityObject structure, int createdBy) throws BLLException;

    /**
     * Gets all default structures from the data layer.
     * @return
     * @throws BLLException
     */
    public List<StructEntityObject> getAllStructures() throws BLLException;

    /**
     * Returns a hashmap that shows headers and examples when creating a
     * profile.
     * @param path = The path to the file.
     * @return
     * @throws BLLException
     */
    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path) throws BLLException;

    /**
     * Attempts to log the user in. If it fails, it throws back an exception.
     * @param username =
     * @param password
     * @return
     * @throws BLLException
     */
    public User login(String username, String password) throws BLLException;

    /**
     * Encrypts the password of a given string.
     * @param base = The password that is to be encrypted
     * @return = The encrypted passwaord
     * @throws BLLException
     */
    public String encrypt(String base) throws BLLException;

    public List<Log> getAllLogs() throws BLLException;

    public Log addLog(LogType type, String message, User creator) throws BLLException;

    public User getcurrentUser();

    public void createChangeListener(AutoUpdater aThis);

    public int getNewestLog() throws BLLException;
}
