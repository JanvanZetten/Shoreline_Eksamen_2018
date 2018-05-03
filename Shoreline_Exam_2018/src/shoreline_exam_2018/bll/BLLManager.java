/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.util.List;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.DALFacade;
import shoreline_exam_2018.dal.DALManager;

/**
 *
 * @author alexl
 */
public class BLLManager implements BLLFacade
{

    private ConversionManager cMan;
    private DALFacade dal;

    public BLLManager()
    {
        cMan = new ConversionManager();
        dal = new DALManager();
    }

    @Override
    @Deprecated
    public ConversionJob setConversionFilePath(String taskName, Path selectedFilePath, Profile selectedProfile)
    {
        //return cMan.newConversion(taskName, selectedFilePath, selectedProfile);
        return null;
    }

    @Override
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws BLLExeption
    {
        try
        {
            return dal.addProfile(name, structure, createdBy);
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<Profile> getAllProfiles() throws BLLExeption
    {
        try
        {
            return dal.getAllProfiles();
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<String> getHeadersFromFile(Path path) throws BLLExeption
    {
        try
        {
            return dal.getHeadersFromFile(path);
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public ConversionJob startConversion(String taskName, Path inputFile, Path outputFile, Profile profile, ListView<ConversionJob> listJobs) throws BLLExeption {
        return cMan.newConversion(taskName, inputFile, outputFile, profile, listJobs);
    }

}
