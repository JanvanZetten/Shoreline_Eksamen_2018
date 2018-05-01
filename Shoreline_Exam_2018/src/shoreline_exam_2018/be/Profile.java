/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be;

import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;

/**
 * The Profile BE Has a id for database purpose. Name of the profile. Structure
 * explaining what from the input should be converted into the output file.
 * CreatedBy is the creator of the Profile.
 * @author Asbamz
 */
public class Profile
{
    private int id;
    private String name;
    private StructEntityObject structure;
    private String createdBy;

    /**
     * Constructor.
     * @param id
     * @param name
     * @param structure
     * @param createdBy
     */
    public Profile(int id, String name, StructEntityObject structure, String createdBy)
    {
        this.id = id;
        this.name = name;
        this.structure = structure;
        this.createdBy = createdBy;
    }

    /**
     * Get database id.
     * @return
     */
    public int getId()
    {
        return id;
    }

    /**
     * Get name of profile.
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get StructEntityObject which contain structure.
     * @return
     */
    public StructEntityObject getStructure()
    {
        return structure;
    }

    /**
     * Gets database id of creator. SHOULD BE INTEGER!!!!!!!!!
     * @return
     */
    public String getCreatedBy()
    {
        return createdBy;
    }
}
