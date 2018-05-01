/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be;

import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;

/**
 *
 * @author Asbamz
 */
public class Profile
{
    private int id;
    private String name;
    private StructEntityObject structure;
    private String createdBy;

    public Profile(int id, String name, StructEntityObject structure, String createdBy)
    {
        this.id = id;
        this.name = name;
        this.structure = structure;
        this.createdBy = createdBy;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public StructEntityObject getStructure()
    {
        return structure;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }
}
