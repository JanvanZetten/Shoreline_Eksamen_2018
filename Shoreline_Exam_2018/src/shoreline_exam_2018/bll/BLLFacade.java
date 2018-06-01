package shoreline_exam_2018.bll;

import shoreline_exam_2018.gui.model.conversion.ConversionBoxMulti;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.be.output.structure.entity.StructEntityObject;
import shoreline_exam_2018.gui.model.AutoUpdater;
import shoreline_exam_2018.gui.model.conversion.ConversionBoxManager;

/**
 *
 * @author janvanzetten
 */
public interface BLLFacade
{
    /**
     * Adds a profile to the database.
     * @param name = Name of the profile.
     * @param structure = The StructEntityObject that determines how it should
     * convert.
     * @param headersIndexAndExamples = The headers used creating Profile.
     * @return = The profile that was made with this method.
     * @throws BLLException
     */
    public Profile addProfile(String name, StructEntityObject structure, HashMap<String, Map.Entry<Integer, String>> headersIndexAndExamples) throws BLLException;

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
     * Deletes given Profile from the database.s
     * @param profile
     * @throws BLLException
     */
    public void deleteProfile(Profile profile) throws BLLException;

    /**
     * Updates Profile with given id.
     * @param profile
     * @throws BLLException
     */
    public Profile updateProfile(Profile profile) throws BLLException;

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
     * @param username
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

    /**
     * Gets a list of logs from the database
     * @return
     * @throws BLLException
     */
    public List<Log> getAllLogs() throws BLLException;

    /**
     * Adds a new log to the database.
     * @param type = The type of the log
     * @param message = The message of the log
     * @param creator = The user who caused the log
     * @return
     * @throws BLLException
     */
    public Log addLog(LogType type, String message) throws BLLException;

    /**
     * Gets the current user of the program
     * @return
     */
    public User getcurrentUser();

    /**
     * Creates a listener for the log that updates the log list automatically.
     * @param aThis
     */
    public void createChangeListener(AutoUpdater aThis);

    /**
     * Gets the newest log in the database to update the log list.
     * @return
     * @throws BLLException
     */
    public int getNewestLog() throws BLLException;

    /**
     * Updates the default directory of the chose type (input and output).
     * @param directory = [Type of directory, file path]
     * @throws BLLException
     */
    public void updateDefaultDirectory(String[] directory, String input, String output) throws BLLException;

    /**
     * Adds default output from the properties on start-up so that it can be
     * gotten later.
     * @param outputValue = Path of output
     */
    public void addDefaultOutput(String outputValue);

    /**
     * Adds default input from the properties on start-up so that it can be
     * gotten later.
     * @param inputValue = Path of output
     */
    public void addDefaultInput(String inputValue);

    /**
     * Gets the default directories from the DAL layer.
     * @return [0 = output directory, 1 = input directory]
     */
    public String[] getDefaultDirectories();

    /**
     * Adds a listener to a ConversionBoxMulti that is currently active.
     * @param conversionBoxMulti = The ConversionBoxMulti object
     * @param inputPath = The input path
     * @param outputPath = The output path
     * @param cManager = The ConversionBoxManager object needed to create new
     * COnversionJobSingles
     * @throws BLLException
     */
    public void addDirectoryListener(ConversionBoxMulti conversionBoxMulti, Path inputPath, Path outputPath, ConversionBoxManager cManager) throws BLLException;

    /**
     * Is called whenever tab focus is changed. Checks for ConvertModel if a new
     * default path has been set in SettingsView.
     * @param outputPath = The default output path.
     * @return
     */
    public Path checkForExisting(Path outputPath);

    /**
     * Updates the default profile in the properties.
     * @param profile = profile[0] = "defaultProfile", profile[1] = profile ID.
     * @throws BLLException
     */
    public void updateDefaultProfile(String[] profile) throws BLLException;

    /**
     * Adds a new default profile to the properties.
     * @param string = ID of the default profile.
     * @throws BLLException
     */
    public void addDefaultProfile(String string) throws BLLException;

    /**
     * Gets the defualt profile from the properties.
     * @return the ID of the default profile.
     */
    public int getDefaultProfile();
}
