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
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;

/**
 *
 * @author janvanzetten
 */
public interface BLLFacade
{

    /**
     * Starts a conversion with the given input and output file using the given profile.
     * @param taskName      = Name for the conversion job.
     * @param inputFile     = The input file that is being converted.
     * @param outputFile    = The output file that is being converted to.
     * @param profile       = The profile that is used for the conversion.
     * @return              = A converison job.
     * @throws BLLExeption
     */
    public ConversionJob startConversion(String taskName, Path inputFile, Path outputFile, Profile profile, ListView<ConversionJob> listJobs) throws BLLExeption;

    /**
     * Adds a profile to the database.
     * @param name          = Name of the profile.
     * @param structure     = The StructEntityObject that determines how it should convert.
     * @param createdBy     = ID of the user who created the profile.
     * @return              = The profile that was made with this method.
     * @throws BLLExeption 
     */
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws BLLExeption;

    /**
     * Gets all profiles from the database and returns them in a list.
     * @return
     * @throws BLLExeption 
     */
    public List<Profile> getAllProfiles() throws BLLExeption;

    /**
     * Returns a hashmap that shows headers and examples when creating a profile.
     * @param path          = The path to the file.
     * @return
     * @throws BLLExeption 
     */
    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path) throws BLLExeption;
}
