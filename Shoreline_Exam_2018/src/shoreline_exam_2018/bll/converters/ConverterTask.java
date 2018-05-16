/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.converters;

import java.nio.file.Path;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.be.MutableBoolean;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.be.output.jsonpair.JsonPairJson;
import shoreline_exam_2018.bll.BLLExeption;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.output.Writer;
import shoreline_exam_2018.dal.filereader.Reader;
import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader_for_Big_Documents;
import shoreline_exam_2018.dal.output.json.JsonWriter;

/**
 *
 * @author janvanzetten
 */
public class ConverterTask extends Task {

    Reader reader;
    Writer writer;
    RowToOutputPairMapper mapper;
    BooleanProperty isDone;
    MutableBoolean isOperating;
    MutableBoolean isCanceld;
    Profile selectedProfile;
    Path inputFile;
    Path outputFile;

    public ConverterTask(Profile selectedProfile, Path inputFile, Path outputFile, MutableBoolean isCanceld, MutableBoolean isOperating, BooleanProperty isDone) throws BLLExeption {
        
        try {
            reader = new XLSX_horisontal_Reader_for_Big_Documents(inputFile.toString());
            writer = new JsonWriter(outputFile);
            mapper = new RowToOutputPairMapper();
            this.inputFile = inputFile;
            this.outputFile = outputFile;
            this.selectedProfile = selectedProfile;
            this.isCanceld = isCanceld;
            this.isOperating = isOperating;
            this.isDone = isDone;

        } catch (DALException ex) {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    protected Object call() throws Exception {
        int currentrow = 0;
        int totalRows = reader.numberOfRows();

        while (reader.hasNext()) {
            currentrow++;

            updateProgress(currentrow, totalRows);

            if (isCanceld.getValue()) {
                stop();
                return null;
            }
            while (!isOperating.getValue()) {
                Thread.sleep(1000);
            }

            
            
            Row input = read();

            OutputPair output = convert(input);

            write(output);

        }

        stop();

        return null;
    }

    private Row read() throws BLLExeption {
        try {
            return reader.getNextRow();
        } catch (DALException ex) {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    private OutputPair convert(Row input) throws BLLExeption {
        try {
            return new JsonPairJson("", mapper.mapRowToOutputpairListWithEntityCollection(selectedProfile.getStructure(), input));
        } catch (BLLExeption ex) {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    private void write(OutputPair output) throws BLLExeption {
        try {
            writer.writeObjectToFile(output);
        } catch (DALException ex) {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

    private void stop() throws BLLExeption {
        isDone.setValue(Boolean.TRUE);
        
        try {
            writer.closeStream();
        } catch (DALException ex) {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
    }

}