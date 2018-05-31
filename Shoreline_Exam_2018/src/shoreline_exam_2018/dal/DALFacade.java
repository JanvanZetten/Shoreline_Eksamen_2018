/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.io.IOException;
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

/**
 *
 * @author janvanzetten
 */
public interface DALFacade
{
    /**
     * Adds a new profile to the database.
     * @param name = Name of the profile.
     * @param structure = The structure of the profile which was built.
     * @param headersIndexAndExamples = The headers used creating profile.
     * @return
     * @throws DALException
     */
    public Profile addProfile(String name, StructEntityObject structure, HashMap<String, Map.Entry<Integer, String>> headersIndexAndExamples) throws DALException;

    /**
     * Gets all profiles from the database.
     * @return
     * @throws DALException
     */
    public List<Profile> getAllProfiles() throws DALException;

    /**
     * Adds a structure to the database which is connected tot he profile.
     * @param name = Name of the profile that this structure belongs to
     * @param structure = The structure of the profile which was built.
     * @param createdBy = The user who created the profile.
     * @return
     * @throws DALException
     */
    public StructEntityObject addStructure(String name, StructEntityObject structure, int createdBy) throws DALException;

    /**
     * Gets all structures in the database.
     * @return
     * @throws DALException
     */
    public List<StructEntityObject> getAllStructures() throws DALException;

    /**
     * Deletes given profile from database.
     * @param profile
     * @throws DALException
     */
    public void deleteProfile(Profile profile) throws DALException;

    /**
     * Updates Profile in the database with matching Id.
     * @param profile
     * @throws DALException
     */
    public Profile updateProfile(Profile profile) throws DALException;

    /**
     * Gets headers and examples from files that profiles are made from.
     * @param path
     * @return
     * @throws DALException
     */
    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path, String filetype) throws DALException;

    /**
     * Attempts to log the user in and returns the user if it exists in the
     * database.
     * @param user = The username
     * @param password = The encrypted password
     * @return
     * @throws DALException
     */
    public User userLogin(String user, String password) throws DALException;

    /**
     * Gets the current user of the program.
     * @return
     */
    public User getCurrentUser();

    /**
     * Gets all the log objects from the database.
     * @return
     * @throws DALException
     */
    public List<Log> getAllLogs() throws DALException;

    /**
     * Adds a new log object to the database.
     * @param type = The type of the log
     * @param message = The message that accompanies the log
     * @param creator = The user who caused the log
     * @return
     * @throws DALException
     */
    public Log addLog(LogType type, String message) throws DALException;

    /**
     * Gets the newest log object currently in the database.
     * @return
     * @throws DALException
     */
    public int getNewestLog() throws DALException;

    /**
     * Updates the default directory of the desktop.
     * @param directory = [input or output, path of the new directory]
     * @param input = The path of the default input directory
     * @param output = The path of the default output directory
     * @throws DALException
     * @throws IOException
     */
    public void updateDefaultDirectory(String[] directory, String input, String output) throws DALException, IOException;

    /**
     * Sets default output so it can be gotten and updated.
     * @param outputValue
     */
    public void addDefaultOutput(String outputValue);

    /**
     * Sets default input so it can be gotten and updated.
     * @param inputValue
     */
    public void addDefaultInput(String inputValue);

    /**
     * Gets the default directories of the desktop.
     * @return
     */
    public String[] getDefaultDirectories();
    
    /**
     * Checks if the file already exists
     * @param file
     * @return
     */
    public boolean doesFileExist(Path file);

    /**
     * Adds a new default profile to the properties.
     * @param profile = ID of the default profile.
     */
    public void addDefaultProfile(String profile);

    /**
     * Updates the default profile in the properties.
     * @param profile = profile[0] = "defaultProfile", profile[1] = profile ID.
     * @throws shoreline_exam_2018.dal.DALException
     * @throws java.io.IOException
     */
    public void updateDefaultProfile(String[] profile) throws DALException, IOException;

    /**
     * Gets the defualt profile from the properties.
     * @return the ID of the default profile.
     */
    public String getDefaultProfile();
}
