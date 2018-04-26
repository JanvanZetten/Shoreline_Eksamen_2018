/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;


/**
 *
 * @author alexl
 */
public class ConversionManager {
    
    public void newConversion() {
        ConversionThread cThread = new ConversionThread();
        ConversionTask cTask = new ConversionTask(cThread);
    }
}
