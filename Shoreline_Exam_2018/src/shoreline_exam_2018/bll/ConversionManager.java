/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import shoreline_exam_2018.bll.ConversionThread;

/**
 *
 * @author alexl
 */
public class ConversionManager {
    
    public ConversionThread createConversion() {
        ConversionThread cThread = new ConversionThread();
        return cThread;
    }
}
