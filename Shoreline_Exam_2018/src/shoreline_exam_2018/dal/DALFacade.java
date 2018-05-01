/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.nio.file.Path;
import java.util.List;
import shoreline_exam_2018.be.Profile;

/**
 *
 * @author janvanzetten
 */
public interface DALFacade
{
    public List<Profile> getAllProfiles() throws DALException;

    public List<String> getHeadersFromFile(Path path) throws DALException;
}
