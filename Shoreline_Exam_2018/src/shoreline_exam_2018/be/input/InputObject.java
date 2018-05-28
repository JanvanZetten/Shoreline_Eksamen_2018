/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.input;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author janvanzetten
 */
public class InputObject
{
    List<InputField> fields;

    public InputObject(List<InputField> fields)
    {
        this.fields = fields;
    }

    public InputField getField(int index) throws IndexOutOfBoundsException
    {
        return fields.get(index);
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.fields);
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
        final InputObject other = (InputObject) obj;
        if (!Objects.equals(this.fields, other.fields))
        {
            return false;
        }
        return true;
    }

}
