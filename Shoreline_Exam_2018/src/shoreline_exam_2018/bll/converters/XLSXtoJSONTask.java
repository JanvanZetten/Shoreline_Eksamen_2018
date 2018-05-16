/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.converters;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.be.MutableBoolean;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.be.output.jsonpair.JsonPairJson;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.ConversionJob;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader;
import shoreline_exam_2018.dal.output.json.JsonWriter;
import shoreline_exam_2018.dal.filereader.Reader;

/**
 *
 * @author janvanzetten
 */
public class XLSXtoJSONTask extends Task {

    private Profile selectedProfile;
    private Path inputFile;
    private Path outputFile;
    private MutableBoolean isCanceld;
    private MutableBoolean isOperating;
    private Reader reader;
    private RowToOutputPairMapper mapper;
    private ConversionJob job;
    private BooleanProperty isDone;

    public XLSXtoJSONTask(Profile selectedProfile, Path inputFile, Path outputFile, MutableBoolean isCanceld, MutableBoolean isOperating, BooleanProperty isDone) {
        this.selectedProfile = selectedProfile;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.isCanceld = isCanceld;
        this.isOperating = isOperating;
        this.isDone = isDone;
        job = null;
        mapper = new RowToOutputPairMapper();
    }

    @Override
    protected Object call() throws Exception {
        updateProgress(0, 1);
        reader = new XLSX_horisontal_Reader(inputFile.toString());

        StructEntityObject structure = selectedProfile.getStructure();

        List<OutputPair> outputObjects = new ArrayList<>();

        try {
            int totalRows = reader.numberOfRows();
            int currentrow = 0;
            while (reader.hasNext()) {
                
                currentrow++;

                updateProgress(currentrow, totalRows);

                if (isCanceld.getValue()) {
                    return null;
                }
                while (!isOperating.getValue()) {
                    Thread.sleep(1000);
                }

                Row nextRow = reader.getNextRow();

                OutputPair outputpair = new JsonPairJson("", mapper.mapRowToOutputpairListWithEntityCollection(structure, nextRow));

                outputObjects.add(outputpair);
            }

            new JsonWriter().createFile(outputObjects, outputFile);

        } catch (DALException | InterruptedException ex) {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
        
        isDone.setValue(Boolean.TRUE);
        
        return null;
    }

}
