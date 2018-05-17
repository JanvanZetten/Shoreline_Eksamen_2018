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

    /**
     * Convert Apache POI Row to InpuObject
     * @param row the row to convert
     * @return inputObject with data from row
     */
    public static InputObject rowToInputObject(Row row) {
        List<InputField> fields = new ArrayList<>();
        
        for (int cn=0; cn<row.getLastCellNum(); cn++) {
            Cell cell = row.getCell(cn);
            InputField input;
            if (cell == null) {
                input = new InputField(InputFieldType.EMPTY);
            } else {
                switch (cell.getCellTypeEnum()) {
                    case FORMULA:
                        switch (cell.getCachedFormulaResultTypeEnum()) {
                            case STRING:
                                if (cell.getStringCellValue().isEmpty()) {
                                    input = new InputField(InputFieldType.EMPTY);
                                } else {
                                    input = new InputField(InputFieldType.STRING, cell.getStringCellValue());
                                }
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
                        if (cell.getStringCellValue().isEmpty()) {
                            input = new InputField(InputFieldType.EMPTY);
                        } else {
                            input = new InputField(InputFieldType.STRING, cell.getStringCellValue());
                        }
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
            }
            fields.add(input);
        }

        return new InputObject(fields);
    }

}
