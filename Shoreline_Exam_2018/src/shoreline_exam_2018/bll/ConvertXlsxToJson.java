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
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.be.MutableBoolean;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.be.output.jsonpair.*;
import shoreline_exam_2018.be.output.structure.*;
import shoreline_exam_2018.be.output.structure.entry.*;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.filereader.InputFileReader;
import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader;
import shoreline_exam_2018.dal.output.json.JsonDAO;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;

/**
 *
 * @author Jan
 */
public class ConvertXlsxToJson implements ConversionInterface
{

    InputFileReader reader;

    /**
     *
     * Convert inputfile acording to the given profile and write it to the
     * outputfile
     * @throws shoreline_exam_2018.bll.BLLExeption
     */
    @Override
    public void convertFile(Profile selectedProfile, Path inputFile, Path outputFile, MutableBoolean isCanceld, MutableBoolean isOperating, DoubleProperty progress) throws BLLExeption
    {
        reader = new XLSX_horisontal_Reader(inputFile.toString());

        StructEntityObject structure = selectedProfile.getStructure();

        List<OutputPair> outputObjects = new ArrayList<>();

        try
        {
            int totalRows = reader.numberOfRows();
            int currentrow = 0;
            while (reader.hasNext())
            {
                currentrow++;

                progress.setValue(totalRows / currentrow * 100); // sets the percent of done

                if (isCanceld.getValue())
                {
                    return;
                }
                while (!isOperating.getValue())
                {
                    Thread.sleep(1000);
                }

                Row nextRow = reader.getNextRow();

                OutputPair outputpair = new JsonPairJson("", mapRowToOutputpairWithEntityCollection(structure, nextRow));

                outputObjects.add(outputpair);
            }

            System.out.println("Done converting --- written from ConvertXlsxToJson line 60");

            new JsonDAO().createFile(outputObjects, outputFile);

        }
        catch (DALException ex)
        {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ConvertXlsxToJson.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * get a List of outputpairs with the data from the row and the structure
     * and fields from the structObject
     * @param structObject the object describing which data should convert to
     * what
     * @param row the row from which to load the data
     * @return a list of outputpairs
     * @throws BLLExeption if the stuctObject has a structEntry which is not
     * supported. The supported are: StructEntryArray, StructEntryDate,
     * StructEntryDouble, StructEntryInteger, StructEntryObject and
     * StructEntryString.
     */
    private List<OutputPair> mapRowToOutputpairWithEntityCollection(CollectionEntity structObject, Row row) throws BLLExeption
    {
        List<StructEntityInterface> collection = structObject.getCollection();

        List<OutputPair> output = new ArrayList<>();

        for (StructEntityInterface structEntry : collection)
        {
            System.out.println(structEntry);
            //check what object this structE
            if (structEntry instanceof StructEntityArray)
            {

                List<OutputPair> jsonArray = mapRowToOutputpairWithEntityCollection((StructEntityArray) structEntry, row);
                output.add(new JsonPairArray(structEntry.getColumnName(), jsonArray));

            }
            else if (structEntry instanceof StructEntityDate)
            {

                output.add(new JsonPairDate(structEntry.getColumnName(),
                        row.getCell(((StructEntityDate) structEntry).getInputIndex()).getDateCellValue()));

            }
            else if (structEntry instanceof StructEntityDouble)
            {

                output.add(new JsonPairDouble(structEntry.getColumnName(),
                        row.getCell(((StructEntityDouble) structEntry).getInputIndex()).getNumericCellValue()));

            }
            else if (structEntry instanceof StructEntityInteger)
            {
                Double asDouble = row.getCell(((StructEntityInteger) structEntry).getInputIndex()).getNumericCellValue();
                output.add(new JsonPairInteger(structEntry.getColumnName(), asDouble.intValue()));

            }
            else if (structEntry instanceof StructEntityObject)
            {
                List<OutputPair> jsonObject = mapRowToOutputpairWithEntityCollection((StructEntityObject) structEntry, row);
                output.add(new JsonPairJson(structEntry.getColumnName(), jsonObject));

            }
            else if (structEntry instanceof StructEntityString)
            {

                output.add(new JsonPairString(structEntry.getColumnName(),
                        row.getCell(((StructEntityString) structEntry).getInputIndex()).getStringCellValue()));

            }
            else
            {
                throw new BLLExeption("The struct entry type is not supported");
            }

        }

        return output;

    }

}
