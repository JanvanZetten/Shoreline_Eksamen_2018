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
     * Starts a conversion with the given input and output file using the given
     * profile,
     * @param taskName the name for the conversion job it returns
     * @param inputFile
     * @param outputFile
     * @param profile
     * @return a converison job with the given name
     * @throws BLLExeption
     */
    public ConversionJob startConversion(String taskName, Path inputFile, Path outputFile, Profile profile, ListView<ConversionJob> listJobs) throws BLLExeption;

    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws BLLExeption;

    public List<Profile> getAllProfiles() throws BLLExeption;

    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path) throws BLLExeption;
}
