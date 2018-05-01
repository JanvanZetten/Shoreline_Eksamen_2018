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
import shoreline_exam_2018.be.output.structure.entry.StructEntryDate;
import shoreline_exam_2018.be.output.structure.StructEntryInterface;
import shoreline_exam_2018.be.output.structure.entry.StructEntryObject;
import shoreline_exam_2018.be.output.structure.entry.StructEntryString;

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
        thisShouldWork.add(new StructEntryString("siteName", 0));
        thisShouldWork.add(new StructEntryString("assetSerialNumber", 1));
        thisShouldWork.add(new StructEntryString("type", 2));
        thisShouldWork.add(new StructEntryString("externalWorkOrderId", 3));
        thisShouldWork.add(new StructEntryString("systemStatus", 4));
        thisShouldWork.add(new StructEntryString("userStatus", 5));
        thisShouldWork.add(new StructEntryDate("createdOn", 6));
        thisShouldWork.add(new StructEntryString("createdBy", 7));
        thisShouldWork.add(new StructEntryString("name", 8));
        thisShouldWork.add(new StructEntryString("priority", 9));
        thisShouldWork.add(new StructEntryString("status", 10));

        List<StructEntryInterface> oioArr = new ArrayList<>();
        oioArr.add(new StructEntryDate("latestFinishDate", 11));
        oioArr.add(new StructEntryDate("earliestStartDate", 12));
        oioArr.add(new StructEntryDate("latestStartDate", 13));
        oioArr.add(new StructEntryDate("estimatedTime", 14));
        StructEntryObject oio = new StructEntryObject("planning", oioArr);
        thisShouldWork.add(oio);

        StructEntryObject structure = new StructEntryObject("Column test", thisShouldWork);

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
            for (StructEntryInterface structEntryInterface : profile.getStructure().getCollection())
            {
                System.out.println(structEntryInterface);
            }
        }
    }

}
