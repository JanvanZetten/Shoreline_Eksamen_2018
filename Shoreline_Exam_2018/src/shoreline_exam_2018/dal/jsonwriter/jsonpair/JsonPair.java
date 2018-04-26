/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.jsonwriter.jsonpair;

/**
 *
 * @author Asbamz
 */
public interface JsonPair<V>
{
    /**
     * Get key of pair.
     * @return key.
     */
    public String getKey();

    /**
     * Get value of pair.
     * @return value.
     */
    public V getValue();
}
