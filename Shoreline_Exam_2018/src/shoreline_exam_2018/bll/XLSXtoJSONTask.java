/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.be.MutableBoolean;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.be.output.jsonpair.JsonPairJson;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.filereader.InputFileReader;
import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader;
import shoreline_exam_2018.dal.output.json.JsonDAO;

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
    private InputFileReader reader;
    private RowToOutputPairMapper mapper;
    private ConversionJob job;

    public XLSXtoJSONTask(Profile selectedProfile, Path inputFile, Path outputFile, MutableBoolean isCanceld, MutableBoolean isOperating, ConversionJob job) {
        this.selectedProfile = selectedProfile;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.isCanceld = isCanceld;
        this.isOperating = isOperating;
        this.job = job;
        mapper = new RowToOutputPairMapper();
    }

    @Override
    protected Object call() throws Exception {
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

            new JsonDAO().createFile(outputObjects, outputFile);

        } catch (DALException | InterruptedException ex) {
            throw new BLLExeption(ex.getMessage(), ex.getCause());
        }
        
        
        while (true) {
                if (job != null) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            job.conversionDone();
                        }
                    });
                    break;
                } else {
                    Thread.sleep(10);
                }

            }
        
        
        return null;
    }

}
