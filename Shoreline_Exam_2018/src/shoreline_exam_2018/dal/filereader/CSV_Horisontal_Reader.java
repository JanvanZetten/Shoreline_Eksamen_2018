/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.filereader;

import java.util.List;
import shoreline_exam_2018.be.InputObject;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author janvanzetten
 */
public class CSV_Horisontal_Reader implements Reader{

    public CSV_Horisontal_Reader(String FileName) {
        
        
    }

    
    @Override
    public List<String> getParameters() throws DALException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasNext() throws DALException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InputObject getNext() throws DALException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int numberOfRows() throws DALException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
