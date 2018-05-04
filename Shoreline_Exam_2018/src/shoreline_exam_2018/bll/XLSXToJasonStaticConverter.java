/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.filereader.InputFileReader;
import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader;
import shoreline_exam_2018.dal.output.OutputDAO;
import shoreline_exam_2018.dal.output.json.JsonDAO;
import shoreline_exam_2018.be.output.jsonpair.*;

/**
 *
 * @author janvanzetten
 */
public class XLSXToJasonStaticConverter {

    InputFileReader inputReader;
    String jsonFilePath;
    int counter = 0;

    public XLSXToJasonStaticConverter(String xlsxFilepath, String newJasonoutputFilepath) {
        this.jsonFilePath = newJasonoutputFilepath;
        inputReader = new XLSX_horisontal_Reader(xlsxFilepath);
    }

    
    /**
     * converts XLSX as in given example to Json file 
     * @throws BLLExeption 
     */
    public void convert() throws BLLExeption {
        
        
        
        List<OutputPair> outputObjects = new ArrayList<>();

        try {

            List<String> paramaterNamesFromXLSX = inputReader.getParameters();

            while (inputReader.hasNext()) {
                
                counter ++;
                System.out.println("convert Counter: " + counter);
                
                Row inputRow = inputReader.getNextRow();

                List<OutputPair> outputFields = new ArrayList<>();

                // from row to output fields
                OutputPair jsonObject = new JsonPairString("siteName", "");
                outputFields.add(jsonObject);

                jsonObject = new JsonPairString("siteasset", getAssetFrom(inputRow.getCell(find("Description", paramaterNamesFromXLSX))));
                outputFields.add(jsonObject);

                jsonObject = new JsonPairString("type", inputRow.getCell(find("Order Type", paramaterNamesFromXLSX)).getStringCellValue());
                outputFields.add(jsonObject);

                jsonObject = new JsonPairString("externalWorkOrderId", inputRow.getCell(find("Order", paramaterNamesFromXLSX)).getStringCellValue());
                outputFields.add(jsonObject);

                jsonObject = new JsonPairString("systemStatus", inputRow.getCell(find("System status", paramaterNamesFromXLSX)).getStringCellValue());
                outputFields.add(jsonObject);

                jsonObject = new JsonPairString("userStatus", inputRow.getCell(find("User status", paramaterNamesFromXLSX)).getStringCellValue());
                outputFields.add(jsonObject);

                Calendar cal = Calendar.getInstance();
                jsonObject = new JsonPairDate("createdOn", cal.getTime());
                outputFields.add(jsonObject);

                jsonObject = new JsonPairString("createdBy", "SAP");
                outputFields.add(jsonObject);

                jsonObject = new JsonPairString("name", thisElseThat(inputRow.getCell(find("Opr. short text", paramaterNamesFromXLSX)).getStringCellValue(), inputRow.getCell(findSecond("Description", paramaterNamesFromXLSX)).getStringCellValue()));
                outputFields.add(jsonObject);

                jsonObject = new JsonPairString("priority", thisElseThat(inputRow.getCell(findSecond("Priority", paramaterNamesFromXLSX)).getStringCellValue(), "low"));
                outputFields.add(jsonObject);

                jsonObject = new JsonPairString("status", "NEW");
                outputFields.add(jsonObject);

                //planning object
                List<OutputPair> planningFields = new ArrayList<>();
                
                {
                    jsonObject = new JsonPairDate("latestFinishDate", inputRow.getCell(find("Lat.finish date", paramaterNamesFromXLSX)).getDateCellValue());
                    planningFields.add(jsonObject);

                    jsonObject = new JsonPairDate("earliestStartDate", inputRow.getCell(find("Earl.start date", paramaterNamesFromXLSX)).getDateCellValue());
                    planningFields.add(jsonObject);

                    jsonObject = new JsonPairDate("latestStartDate", inputRow.getCell(find("Latest start", paramaterNamesFromXLSX)).getDateCellValue());
                    planningFields.add(jsonObject);

                    jsonObject = new JsonPairString("estimatedTime", thisElseThat(Double.toString(inputRow.getCell(find("Work", paramaterNamesFromXLSX)).getNumericCellValue()), ""));
                    planningFields.add(jsonObject);

                }

                jsonObject = new JsonPairJson("planning", planningFields);
                outputFields.add(jsonObject);

                
                
                
                OutputPair outputpair = new JsonPairJson("", outputFields);

                outputObjects.add(outputpair);
            }

            OutputDAO output = new JsonDAO();
            output.createFile(outputObjects, Paths.get(jsonFilePath));

        } catch (DALException ex) {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }

    }

    
    /**
     * to get the asset number from the description
     * @param cell
     * @return 
     */
    private String getAssetFrom(Cell cell) {
        
        String someString = cell.getStringCellValue();
        System.out.println(someString);
        
        
        String[] someArray;
        
        
        
        someArray = someString.split(Pattern.quote("("));
        
        someString = someArray[1];
        
        
        someString = someString.split(Pattern.quote(")"))[0];
        System.out.println(someString + " : "+ counter);
        
        return someString;
    }

    /**
     * Find the indeks og the parameter given as toFind in the list of parameters given
     * @param toFind
     * @param paramaterNamesFromXLSX
     * @return the index of the parameter in the list if the parameter is not in the list return -1
     */
    private int find(String toFind, List<String> paramaterNamesFromXLSX) {
        for (int i = 0; i < paramaterNamesFromXLSX.size(); i++) {
            if (paramaterNamesFromXLSX.get(i).equals(toFind)) {
                return i;
            }
        }
        return -1;
    }

    
    /**
     * if the first is not empty return first if first empty return second
     * @param first
     * @param second
     * @return 
     */
    private String thisElseThat(String first, String second) {
        if (first == null) {
            return second;
        }else if (first.equals("")){
            return second;
        }else{
            return first;
        }
    }

    /**
     * same as find but finds the second ocurence of the parameter name
     * @param toFind
     * @param paramaterNamesFromXLSX
     * @return 
     */
    private int findSecond(String toFind, List<String> paramaterNamesFromXLSX) {
        boolean first = true;
        for (int i = 0; i < paramaterNamesFromXLSX.size(); i++) {
            if (paramaterNamesFromXLSX.get(i).equals(toFind)) {
                if (first) {
                    first = false;
                } else {
                    return i;
                }
            }
        }
        return -1;
    }

}
