/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.util.List;
import javafx.scene.layout.FlowPane;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;

/**
 *
 * @author janvanzetten
 */
public interface BLLFacade
{

    /**
     * Creates a conversion that is based on the file path of a file and a
     * profile.
     * @param taskName = The name of the Conversion
     * @param selectedFilePath = The path of the file attempted to convert
     * @param selectedProfile = The selected profile for the conversion
     * @return = Returns the Task so that it can be set in the view.
     */
    @Deprecated
    public ConversionJob setConversionFilePath(String taskName, Path selectedFilePath, Profile selectedProfile);
    
    public ConversionJob startConversion(String taskName, Path inputFile, Path outputFile, Profile profile, FlowPane paneJobs) throws BLLExeption;

    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws BLLExeption;

    public List<Profile> getAllProfiles() throws BLLExeption;

    public List<String> getHeadersFromFile(Path path) throws BLLExeption;
}
