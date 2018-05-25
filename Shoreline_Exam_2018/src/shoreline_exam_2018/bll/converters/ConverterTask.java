/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.converters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import shoreline_exam_2018.be.input.InputObject;
import shoreline_exam_2018.be.MutableBoolean;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.be.output.jsonpair.JsonPairJson;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.Utilities.FileUtils;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.filereader.CSV_Horisontal_Reader;
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

    public ConverterTask(Profile selectedProfile, Path inputFile, Path outputFile, MutableBoolean isCanceld, MutableBoolean isOperating, BooleanProperty isDone) throws BLLException {
        String filetype = FileUtils.getFiletype(inputFile);
        try {
            if (filetype.equalsIgnoreCase("xlsx")) {
                reader = new XLSX_horisontal_Reader_for_Big_Documents(inputFile.toString());
            } else if (filetype.equalsIgnoreCase("csv")) {
                reader = new CSV_Horisontal_Reader(inputFile.toString());
            }else{
                throw new BLLException("The file type is not supported");
            }
            writer = new JsonWriter(outputFile);
            mapper = new RowToOutputPairMapper();
            this.inputFile = inputFile;
            this.outputFile = outputFile;
            this.selectedProfile = selectedProfile;
            this.isCanceld = isCanceld;
            this.isOperating = isOperating;
            this.isDone = isDone;
        } catch (DALException ex) {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    protected Object call() throws Exception {
        try {
            updateProgress(0, 1);
            int currentrow = 0;
            int totalRows = reader.numberOfRows();

            while (reader.hasNext()) {
                InputObject input = read();

                currentrow++;

                updateProgress(currentrow, totalRows);

                if (isCanceld.getValue()) {
                    stop();
                    //Deletes the output file from the outputPath.
                    Files.delete(outputFile);
                    return null;
                }
                while (!isOperating.getValue()) {
                    Thread.sleep(1000);
                }

                OutputPair output = convert(input);

                write(output);

            }

            stop();

            return null;
        } catch (BLLException | DALException ex) {
            stop();
            String str = "";

            try {
                Files.delete(outputFile);
            } catch (IOException e) {
                str += e.getMessage();
            }

            throw new BLLException(ex.getMessage() + " " + str, ex.getCause());
        }
    }

    /**
     * Reads the next row from the reader
     *
     * @return
     * @throws BLLException
     */
    private InputObject read() throws BLLException {
        try {
            return reader.getNext();
        } catch (DALException ex) {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * converts the row to a outputpair Object
     *
     * @param input
     * @return
     * @throws BLLException
     */
    private OutputPair convert(InputObject input) throws BLLException {
        try {
            return new JsonPairJson("", mapper.mapInputObjectToOutputpairList(selectedProfile.getStructure(), input));
        } catch (BLLException ex) {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Writes the outputobject
     *
     * @param output
     * @throws BLLException
     */
    private void write(OutputPair output) throws BLLException {
        try {
            writer.writeObjectToFile(output);
        } catch (DALException ex) {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * does everything for stopping the conversion also for ended conversion
     *
     * @throws BLLException
     */
    public void stop() throws BLLException {
        try {
            writer.closeStream();
        } catch (DALException ex) {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

}
