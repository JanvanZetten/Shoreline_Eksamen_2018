/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.util.HashMap;

/**
 *
 * @author Asbamz
 */
public class StringRenamer
{
    HashMap<String, Integer> knownString;

    public StringRenamer()
    {
        knownString = new HashMap<>();
    }

    public String checkForDuplicate(String str)
    {
        if (knownString.containsKey(str))
        {
            int index = knownString.get(str);
            String newStr = str;
            while (knownString.containsKey(newStr))
            {
                newStr = str + index;
                knownString.replace(str, index + 1);
            }
            return newStr;
        }
        else
        {
            knownString.put(str, 1);
            return str;
        }
    }
}
