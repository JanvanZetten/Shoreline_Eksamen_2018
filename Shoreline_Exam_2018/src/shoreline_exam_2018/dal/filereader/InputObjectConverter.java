/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.filereader;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.be.InputField;
import shoreline_exam_2018.be.InputFieldType;
import shoreline_exam_2018.be.InputObject;

/**
 *
 * @author janvanzetten
 */
public class InputObjectConverter {

    public static InputObject rowToInputObject(Row row) {
        List<InputField> fields = new ArrayList<>();

        for (Cell cell : row) {
            InputField input;
            switch (cell.getCellTypeEnum()) {
                case FORMULA:
                    switch (cell.getCachedFormulaResultTypeEnum()) {
                        case STRING:
                            input = new InputField(InputFieldType.STRING, cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                input = new InputField(InputFieldType.DATE, cell.getDateCellValue());
                            } else {
                                input = new InputField(InputFieldType.NUMERIC, cell.getNumericCellValue());
                            }
                            break;
                        default: 
                            input = new InputField(InputFieldType.EMPTY);
                    }
                    break;
                case STRING:
                    input = new InputField(InputFieldType.STRING, cell.getStringCellValue());
                    break;
                case NUMERIC:
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        input = new InputField(InputFieldType.DATE, cell.getDateCellValue());
                    } else {
                        input = new InputField(InputFieldType.NUMERIC, cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    input = new InputField(InputFieldType.STRING, Boolean.toString(cell.getBooleanCellValue()));
                    break;
                default:
                    input = new InputField(InputFieldType.EMPTY);

            }
            
            fields.add(input);

        }

        return new InputObject(fields);
    }

}
