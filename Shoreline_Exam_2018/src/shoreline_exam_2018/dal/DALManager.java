/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.nio.file.Path;
import java.util.List;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.dal.database.ProfileDAO;

/**
 *
 * @author alexl
 */
public class DALManager implements DALFacade
{

    private ProfileDAO profileDAO;
    private XLSX_horisontal_Reader xhr;

    public DALManager()
    {
        this.profileDAO = new ProfileDAO();
    }

    @Override
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws DALException
    {
        return profileDAO.addProfile(name, structure, createdBy);
    }

    @Override
    public List<Profile> getAllProfiles() throws DALException
    {
        return profileDAO.getAllProfiles();
    }

    @Override
    public List<String> getHeadersFromFile(Path path) throws DALException
    {
        xhr = new XLSX_horisontal_Reader(path.toString());
        return xhr.getParameters();
    }

}
