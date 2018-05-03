/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

                OutputPair outputpair = new JsonPairJson("", mapRowToOutputpairWithEntryCollection(structure, nextRow));

                outputObjects.add(outputpair);
            }
            System.out.println("Done converting --- written from ConvertXlsxToJson line 53");
            
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
        List<StructEntityInterface> collection = structObject.getCollection();
        
        List<OutputPair> output = new ArrayList<>();
        
        for (StructEntityInterface structEntry : collection) {
            
            //check what object this structE

            if (structEntry instanceof StructEntityArray) {
                
                List<OutputPair> jsonArray = mapRowToOutputpairWithEntryCollection((StructEntityArray)structEntry, row);
                output.add(new JsonPairArray(structEntry.getColumnName(), jsonArray));
                
            } else if (structEntry instanceof StructEntityDate) {
                
                output.add(new JsonPairDate(structEntry.getColumnName(), 
                        row.getCell(((StructEntityDate) structEntry).getInputIndex()).getDateCellValue()));

            } else if (structEntry instanceof StructEntityDouble) {
                
                output.add(new JsonPairDouble(structEntry.getColumnName(),
                        row.getCell(((StructEntityDouble) structEntry).getInputIndex()).getNumericCellValue()));
                
            } else if (structEntry instanceof StructEntityInteger) {
                Double asDouble = row.getCell(((StructEntityInteger) structEntry).getInputIndex()).getNumericCellValue();
                output.add(new JsonPairInteger(structEntry.getColumnName(), asDouble.intValue()));

            } else if (structEntry instanceof StructEntityObject) {
                List<OutputPair> jsonObject = mapRowToOutputpairWithEntryCollection((StructEntityObject)structEntry, row);
                output.add(new JsonPairJson(structEntry.getColumnName(), jsonObject));
                
            } else if (structEntry instanceof StructEntityString) {
                
                output.add(new JsonPairString(structEntry.getColumnName(), 
                        row.getCell(((StructEntityString) structEntry).getInputIndex()).getStringCellValue()));

            } else {
                throw new BLLExeption("The struct entry type is not supported");
            }

        }
        
        return output;

    }

}
