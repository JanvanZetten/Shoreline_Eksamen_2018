/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.input;

import java.util.List;

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

}
