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
import shoreline_exam_2018.be.output.jsonpair.JsonPairArray;
import shoreline_exam_2018.be.output.jsonpair.JsonPairDate;
import shoreline_exam_2018.be.output.jsonpair.JsonPairDouble;
import shoreline_exam_2018.be.output.jsonpair.JsonPairInteger;
import shoreline_exam_2018.be.output.jsonpair.JsonPairJson;
import shoreline_exam_2018.be.output.jsonpair.JsonPairString;
import shoreline_exam_2018.be.output.structure.CollectionEntry;
import shoreline_exam_2018.be.output.structure.entry.*;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.InputFileReader;
import shoreline_exam_2018.dal.XLSX_horisontal_Reader;
import shoreline_exam_2018.dal.output.json.JsonDAO;

/**
 *
 * @author Jan
 */
public class ConvertXlsxToJson implements ConversionInterface {

    InputFileReader reader;

    @Override
    public void convertFile(Profile selectedProfile, Path inputFile, Path outputFile) throws BLLExeption {
        reader = new XLSX_horisontal_Reader(inputFile.toString());

        StructEntryObject structure = selectedProfile.getStructure();

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
        List<StructEntryInterface> collection = structObject.getCollection();
        
        List<OutputPair> output = new ArrayList<>();
        
        for (StructEntryInterface structEntryInterface : collection) {

            if (structEntryInterface instanceof StructEntryArray) {
                
                List<OutputPair> jsonArray = doSomeStuff_FIND_BETTER_NAME((StructEntryArray)structEntryInterface, row);
                output.add(new JsonPairArray(structEntryInterface.getColumnName(), jsonArray));
                
            } else if (structEntryInterface instanceof StructEntryDate) {
                
                output.add(new JsonPairDate(structEntryInterface.getColumnName(), 
                        row.getCell(((StructEntryDate) structEntryInterface).getInputIndex()).getDateCellValue()));

            } else if (structEntryInterface instanceof StructEntryDouble) {
                
                output.add(new JsonPairDouble(structEntryInterface.getColumnName(),
                        row.getCell(((StructEntryDouble) structEntryInterface).getInputIndex()).getNumericCellValue()));
                
            } else if (structEntryInterface instanceof StructEntryInteger) {
                Double asDouble = row.getCell(((StructEntryInteger) structEntryInterface).getInputIndex()).getNumericCellValue();
                output.add(new JsonPairInteger(structEntryInterface.getColumnName(), asDouble.intValue()));

            } else if (structEntryInterface instanceof StructEntryObject) {
                List<OutputPair> jsonObject = doSomeStuff_FIND_BETTER_NAME((StructEntryObject)structEntryInterface, row);
                output.add(new JsonPairJson(structEntryInterface.getColumnName(), jsonObject));
                
            } else if (structEntryInterface instanceof StructEntryString) {
                
                output.add(new JsonPairString(structEntryInterface.getColumnName(), 
                        row.getCell(((StructEntryString) structEntryInterface).getInputIndex()).getStringCellValue()));

            } else {
                throw new BLLExeption("The struct type is not supported");
            }

        }
        
        return output;

    }

}
