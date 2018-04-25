/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.util.List;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author janvanzetten
 */
public interface InputFileReader {
    
    List<String> getParameters();
    
    boolean hasNext();
    
    Row getNextRow();
    
}
