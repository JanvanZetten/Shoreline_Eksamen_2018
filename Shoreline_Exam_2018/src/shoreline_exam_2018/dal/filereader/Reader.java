/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.filereader;

import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author janvanzetten
 */
public interface Reader {
    
    /**
     * get the parameters of the file
     * @return
     * @throws DALException 
     */
    List<String> getParameters() throws DALException;
    
    /**
     * CHeck if there is a element more in the file
     * @return
     * @throws DALException 
     */
    boolean hasNext() throws DALException;
    
    /**
     * get the next row/element of the file
     * @return
     * @throws DALException 
     */
    Row getNextRow() throws DALException;
    
    int numberOfRows() throws DALException;
    
}
