/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

/**
 *
 * @author janvanzetten
 */
public class BLLExeption extends Exception {

    public BLLExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public BLLExeption(String message) {
        super(message);
    }

    public BLLExeption(Throwable cause) {
        super(cause);
    }
    
    
}
