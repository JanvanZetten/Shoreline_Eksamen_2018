/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.be.output.jsonpair.*;
import shoreline_exam_2018.be.output.structure.*;
import shoreline_exam_2018.be.output.structure.entry.*;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.InputFileReader;
import shoreline_exam_2018.dal.XLSX_horisontal_Reader;
import shoreline_exam_2018.dal.output.json.JsonDAO;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;

/**
 *
 * @author Jan
 */
public class ConvertXlsxToJson implements ConversionInterface {

    InputFileReader reader;

    @Override
    public void convertFile(Profile selectedProfile, Path inputFile, Path outputFile) throws BLLExeption {
        reader = new XLSX_horisontal_Reader(inputFile.toString());

        StructEntityObject structure = selectedProfile.getStructure();

        List<OutputPair> outputObjects = new ArrayList<>();

        try {

            while (reader.hasNext()) {
                Row nextRow = reader.getNextRow();

                OutputPair outputpair = new JsonPairJson("", doSomeStuff_FIND_BETTER_NAME(structure, nextRow));

                outputObjects.add(outputpair);
            }
            
            new JsonDAO().createFile(outputObjects, outputFile);
            
        } catch (DALException ex) {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }

    }

    private List<OutputPair> doSomeStuff_FIND_BETTER_NAME(CollectionEntry structObject, Row row) throws BLLExeption {
        List<StructEntityInterface> collection = structObject.getCollection();
        
        List<OutputPair> output = new ArrayList<>();
        
        for (StructEntityInterface structEntryInterface : collection) {

            if (structEntryInterface instanceof StructEntityArray) {
                
                List<OutputPair> jsonArray = doSomeStuff_FIND_BETTER_NAME((StructEntityArray)structEntryInterface, row);
                output.add(new JsonPairArray(structEntryInterface.getColumnName(), jsonArray));
                
            } else if (structEntryInterface instanceof StructEntityDate) {
                
                output.add(new JsonPairDate(structEntryInterface.getColumnName(), 
                        row.getCell(((StructEntityDate) structEntryInterface).getInputIndex()).getDateCellValue()));

            } else if (structEntryInterface instanceof StructEntityDouble) {
                
                output.add(new JsonPairDouble(structEntryInterface.getColumnName(),
                        row.getCell(((StructEntityDouble) structEntryInterface).getInputIndex()).getNumericCellValue()));
                
            } else if (structEntryInterface instanceof StructEntityInteger) {
                Double asDouble = row.getCell(((StructEntityInteger) structEntryInterface).getInputIndex()).getNumericCellValue();
                output.add(new JsonPairInteger(structEntryInterface.getColumnName(), asDouble.intValue()));

            } else if (structEntryInterface instanceof StructEntityObject) {
                List<OutputPair> jsonObject = doSomeStuff_FIND_BETTER_NAME((StructEntityObject)structEntryInterface, row);
                output.add(new JsonPairJson(structEntryInterface.getColumnName(), jsonObject));
                
            } else if (structEntryInterface instanceof StructEntityString) {
                
                output.add(new JsonPairString(structEntryInterface.getColumnName(), 
                        row.getCell(((StructEntityString) structEntryInterface).getInputIndex()).getStringCellValue()));

            } else {
                throw new BLLExeption("The struct type is not supported");
            }

        }
        
        return output;

    }

}
