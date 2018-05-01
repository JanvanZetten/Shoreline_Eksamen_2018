/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.database;

import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.CollectionEntry;
import shoreline_exam_2018.be.output.structure.entry.StructEntityDate;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.be.output.structure.entry.StructEntityString;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;

/**
 *
 * @author Asbamz
 */
public class ProfileDAOTest
{

    public ProfileDAOTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    /**
     * Test of addProfile method, of class ProfileDAO.
     */
    @Test
    public void testAddProfile() throws Exception
    {
        /*
        List<StructEntryInterface> thisShouldWork = new ArrayList<>();
        thisShouldWork.add(new StructEntityString("siteName", 0));
        thisShouldWork.add(new StructEntityString("assetSerialNumber", 1));
        thisShouldWork.add(new StructEntityString("type", 2));
        thisShouldWork.add(new StructEntityString("externalWorkOrderId", 3));
        thisShouldWork.add(new StructEntityString("systemStatus", 4));
        thisShouldWork.add(new StructEntityString("userStatus", 5));
        thisShouldWork.add(new StructEntityDate("createdOn", 6));
        thisShouldWork.add(new StructEntityString("createdBy", 7));
        thisShouldWork.add(new StructEntityString("name", 8));
        thisShouldWork.add(new StructEntityString("priority", 9));
        thisShouldWork.add(new StructEntityString("status", 10));

        List<StructEntryInterface> oioArr = new ArrayList<>();
        oioArr.add(new StructEntityDate("latestFinishDate", 11));
        oioArr.add(new StructEntityDate("earliestStartDate", 12));
        oioArr.add(new StructEntityDate("latestStartDate", 13));
        oioArr.add(new StructEntityDate("estimatedTime", 14));
        StructEntityObject oio = new StructEntityObject("planning", oioArr);
        thisShouldWork.add(oio);

        StructEntityObject structure = new StructEntityObject("Column test", thisShouldWork);

        ProfileDAO pdao = new ProfileDAO();
        pdao.addProfile("Test_Profile", structure, 0);
         */
    }

    /**
     * Test of getAllProfiles method, of class ProfileDAO.
     */
    @Test
    public void testGetAllProfiles() throws Exception
    {
        ProfileDAO pdao = new ProfileDAO();
        List<Profile> profiles = pdao.getAllProfiles();
        for (Profile profile : profiles)
        {
            System.out.println(profile.getName() + " " + profile.getStructure().getCollection().size());
            for (StructEntityInterface structEntryInterface : profile.getStructure().getCollection())
            {
                System.out.println(structEntryInterface);
            }
        }
    }

}
