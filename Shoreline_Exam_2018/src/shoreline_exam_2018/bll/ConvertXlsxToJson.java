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

                OutputPair outputpair = new JsonPairJson("", mapRowToOutputpairWithEntryCollection(structure, nextRow));

                outputObjects.add(outputpair);
            }
            
            new JsonDAO().createFile(outputObjects, outputFile);
            
        } catch (DALException ex) {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }

    }

    /**
     * get a List of outputpairs with the data from the row and the structure and fields from the structObject
     * @param structObject the object describing which data should convert to what
     * @param row the row from which to load the data
     * @return a list of outputpairs
     * @throws BLLExeption if the stuctObject has a structEntry which is not supported. The supported are: StructEntryArray, StructEntryDate, 
     * StructEntryDouble, StructEntryInteger, StructEntryObject and StructEntryString.
     */
    private List<OutputPair> mapRowToOutputpairWithEntryCollection(CollectionEntry structObject, Row row) throws BLLExeption {
        List<StructEntryInterface> collection = structObject.getCollection();
        
        List<OutputPair> output = new ArrayList<>();
        
        for (StructEntryInterface structEntry : collection) {
            
            //check what object this structE

            if (structEntry instanceof StructEntryArray) {
                
                List<OutputPair> jsonArray = mapRowToOutputpairWithEntryCollection((StructEntryArray)structEntry, row);
                output.add(new JsonPairArray(structEntry.getColumnName(), jsonArray));
                
            } else if (structEntry instanceof StructEntryDate) {
                
                output.add(new JsonPairDate(structEntry.getColumnName(), 
                        row.getCell(((StructEntryDate) structEntry).getInputIndex()).getDateCellValue()));

            } else if (structEntry instanceof StructEntryDouble) {
                
                output.add(new JsonPairDouble(structEntry.getColumnName(),
                        row.getCell(((StructEntryDouble) structEntry).getInputIndex()).getNumericCellValue()));
                
            } else if (structEntry instanceof StructEntryInteger) {
                Double asDouble = row.getCell(((StructEntryInteger) structEntry).getInputIndex()).getNumericCellValue();
                output.add(new JsonPairInteger(structEntry.getColumnName(), asDouble.intValue()));

            } else if (structEntry instanceof StructEntryObject) {
                List<OutputPair> jsonObject = mapRowToOutputpairWithEntryCollection((StructEntryObject)structEntry, row);
                output.add(new JsonPairJson(structEntry.getColumnName(), jsonObject));
                
            } else if (structEntry instanceof StructEntryString) {
                
                output.add(new JsonPairString(structEntry.getColumnName(), 
                        row.getCell(((StructEntryString) structEntry).getInputIndex()).getStringCellValue()));

            } else {
                throw new BLLExeption("The struct entry type is not supported");
            }

        }
        
        return output;

    }

}
