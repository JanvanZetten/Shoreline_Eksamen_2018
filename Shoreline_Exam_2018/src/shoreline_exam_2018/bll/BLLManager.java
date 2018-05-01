/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.util.List;
import shoreline_exam_2018.be.Profile;
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
    public ConversionTask setConversionFilePath(String taskName, Path selectedFilePath, Profile selectedProfile)
    {
        return cMan.newConversion(taskName, selectedFilePath, selectedProfile);
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

}
