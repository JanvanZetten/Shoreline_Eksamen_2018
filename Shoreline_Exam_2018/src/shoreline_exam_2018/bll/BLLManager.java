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
    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path) throws BLLExeption
    {
        try
        {
            return dal.getHeadersAndExamplesFromFile(path);
        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public ConversionJob startConversion(String taskName, Path inputFile, Path outputFile, Profile profile, ListView<ConversionJob> listJobs) throws BLLExeption
    {
        return cMan.newConversion(taskName, inputFile, outputFile, profile, listJobs);
    }

}
