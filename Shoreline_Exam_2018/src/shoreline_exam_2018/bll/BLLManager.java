/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import shoreline_exam_2018.bll.ConversionManager;

/**
 *
 * @author alexl
 */
public class BLLManager implements BLLFacade {
    
    private ConversionManager cMan;
    
    public BLLManager() {
        cMan = new ConversionManager();
    }
    
    public ConversionTask setNewTask() {
        return cMan.newConversion();
    }
    
}
