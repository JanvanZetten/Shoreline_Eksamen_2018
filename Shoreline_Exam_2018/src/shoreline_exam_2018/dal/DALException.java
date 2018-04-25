/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

/**
 *
 * @author alexl
 */
public class DALException extends Exception {
    
    /**
     * Exception by message
     * @param message
     */
    public DALException(String message)
    {
        super(message);
    }

    /**
     * Exception by message and cause.
     * @param message
     * @param cause
     */
    public DALException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Exception by cause.
     * @param cause
     */
    public DALException(Throwable cause)
    {
        super(cause);
    }
    
}
