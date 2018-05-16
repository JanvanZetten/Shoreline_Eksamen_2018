/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output;

import shoreline_exam_2018.be.output.OutputPair;
import java.nio.file.Path;
import java.util.List;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author Asbamz
 */
public interface Writer
{

    /**
     * Close stream for file writing.
     * @throws DALException
     */
    public void closeStream() throws DALException;

    /**
     * Creates a file with given entities.
     * @param entities
     * @param outputPath
     * @throws DALException
     */
    @Deprecated
    public void createFile(List<OutputPair> entities, Path outputPath) throws DALException;

    /**
     * Creates or appends to file.
     * @param entity
     * @throws DALException
     */
    public void writeObjectToFile(OutputPair entity) throws DALException;
}
