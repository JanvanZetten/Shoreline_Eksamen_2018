/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author janvanzetten
 */
public class XLSXStaticConverterTestStartClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        XLSXToJasonStaticConverter converter = new XLSXToJasonStaticConverter(
                "/Users/janvanzetten/Documents/GitHub/Shoreline_Eksamen_2018/Shoreline_Exam_2018/src/shoreline_exam_2018/Import_data.xlsx",
                "/Users/janvanzetten/Documents/GitHub/Shoreline_Eksamen_2018/Shoreline_Exam_2018/src/shoreline_exam_2018/someName.json");
        
        try {
            converter.convert();
        } catch (BLLExeption ex) {
            Logger.getLogger(XLSXStaticConverterTestStartClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
