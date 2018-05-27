/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile;

/**
 *
 * @author Asbamz
 */
public abstract class ProfileEntity
{
    /**
     * Shift position from index ascending.
     * @param fromIndex
     * @param shiftIdentifier implies shift direction and amount.
     */
    abstract void shiftPosition(int fromIndex, int shiftIdentifier);
}
