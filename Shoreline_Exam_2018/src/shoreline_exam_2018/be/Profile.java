/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import shoreline_exam_2018.be.output.structure.entity.StructEntityObject;

/**
 * The Profile BE Has a id for database purpose. Name of the profile. Structure
 * explaining what from the input should be converted into the output file.
 * CreatedBy is the creator of the Profile.
 * @author Asbamz
 */
public class Profile implements Comparable<Profile>
{
    private int id;
    private String name;
    private StructEntityObject structure;
    private String createdBy;
    private HashMap<String, Map.Entry<Integer, String>> headersIndexAndExamples;

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

    /**
     * Gets header map.
     * @return
     */
    public HashMap<String, Map.Entry<Integer, String>> getHeadersIndexAndExamples()
    {
        return headersIndexAndExamples;
    }

    /**
     * Sets header map.
     * @param headersIndexAndExamples
     */
    public void setHeadersIndexAndExamples(HashMap<String, Map.Entry<Integer, String>> headersIndexAndExamples)
    {
        this.headersIndexAndExamples = headersIndexAndExamples;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + this.id;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + Objects.hashCode(this.structure);
        hash = 89 * hash + Objects.hashCode(this.createdBy);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Profile other = (Profile) obj;
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return name;
    }

    @Override
    public int compareTo(Profile o)
    {
        if (name.equals(o.getName()))
        {
            return id - o.getId();
        }
        return name.compareTo(o.getName());
    }
}
