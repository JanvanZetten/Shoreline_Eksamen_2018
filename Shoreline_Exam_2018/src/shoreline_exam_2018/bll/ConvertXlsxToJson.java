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
        System.out.println("line 34 ");
        System.out.println(selectedProfile);
        System.out.println(selectedProfile);
        StructEntityObject structure = selectedProfile.getStructure();
        System.out.println("line 37");
        List<OutputPair> outputObjects = new ArrayList<>();
        System.out.println("line 39");
        try {
            
            while (reader.hasNext()) {
                System.out.println("line 43");
                Row nextRow = reader.getNextRow();
                
                System.out.println("46");

                OutputPair outputpair = new JsonPairJson("", mapRowToOutputpairWithEntryCollection(structure, nextRow));

                outputObjects.add(outputpair);
            }
            System.out.println("Done");
            
            new JsonDAO().createFile(outputObjects, outputFile);
            
        } catch (DALException ex) {
            System.out.println("There is an error in Conversion XLSX TO JSON");
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
