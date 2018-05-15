/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;

/**
 *
 * @author janvanzetten
 */
public interface DALFacade
{
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws DALException;

    public List<Profile> getAllProfiles() throws DALException;

    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path) throws DALException;
    
    public User userLogin(String user, String password) throws DALException;
}
