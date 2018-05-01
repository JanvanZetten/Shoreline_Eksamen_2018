/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.util.List;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.dal.database.ProfileDAO;

/**
 *
 * @author alexl
 */
public class DALManager implements DALFacade {
    
    ProfileDAO profileDAO;

    public DALManager() {
        this.profileDAO = new ProfileDAO();
    }

    @Override
    public List<Profile> getAllProfiles() throws DALException {
        return profileDAO.getAllProfiles();
    }
    
}
