/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.converters;

import shoreline_exam_2018.be.output.structure.entity.StructEntityInteger;
import shoreline_exam_2018.be.output.structure.entity.StructEntityDouble;
import shoreline_exam_2018.be.output.structure.entity.StructEntityObject;
import shoreline_exam_2018.be.output.structure.entity.StructEntityArray;
import shoreline_exam_2018.be.output.structure.entity.StructEntityDate;
import shoreline_exam_2018.be.output.structure.entity.StructEntityString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import shoreline_exam_2018.be.input.InputField;
import shoreline_exam_2018.be.input.InputFieldType;
import shoreline_exam_2018.be.input.InputObject;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.be.output.jsonpair.*;
import shoreline_exam_2018.be.output.rule.DateFormatRule;
import shoreline_exam_2018.be.output.rule.DefaultStringRule;
import shoreline_exam_2018.be.output.structure.*;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.bll.BLLException;

/**
 *
 * @author Jan
 */
public class RowToOutputPairMapper
{

    /**
     * Get a List of outputpairs with the data from the inputObject and the
     * structure and fields from the structObject
     *
     * @param structObject the object describing which data should convert to
     * what
     * @param inputObject the inputObject from which to load the data
     * @return a list of outputpairs
     *
     * @throws BLLException if the stuctObject has a structEntry which is not
     * supported. The supported are: StructEntryArray, StructEntryDate,
     * StructEntryDouble, StructEntryInteger, StructEntryObject and
     * StructEntryString.
     */
    public List<OutputPair> mapInputObjectToOutputpairList(CollectionEntity structObject, InputObject inputObject) throws BLLException
    {
        List<StructEntityInterface> collection = structObject.getCollection();

        List<OutputPair> output = new ArrayList<>();

        for (StructEntityInterface structEntry : collection)
        {
            try
            {
                //check what object this structEntity is.
                if (structEntry instanceof StructEntityArray)
                {
                    output.add(getArray(structEntry, inputObject));
                }
                else if (structEntry instanceof StructEntityDate)
                {
                    output.add(getDate(structEntry, inputObject));
                }
                else if (structEntry instanceof StructEntityDouble)
                {
                    output.add(getDouble(structEntry, inputObject));
                }
                else if (structEntry instanceof StructEntityInteger)
                {
                    output.add(getInteger(structEntry, inputObject));
                }
                else if (structEntry instanceof StructEntityObject)
                {
                    output.add(getObject(structEntry, inputObject));
                }
                else if (structEntry instanceof StructEntityString)
                {
                    output.add(getString(structEntry, inputObject));
                }
                else
                {
                    throw new BLLException("The struct entry type is not supported");
                }
            }
            catch (IndexOutOfBoundsException ex)
            {
                throw new BLLException("A struct entry column index exceeds the amount of colums in file");
            }
        }
        return output;
    }

    /**
     * Get Array.
     * @param structEntry
     * @param inputObject
     * @return
     * @throws BLLException
     */
    private JsonPairArray getArray(StructEntityInterface structEntry, InputObject inputObject) throws BLLException
    {
        List<OutputPair> jsonArray = mapInputObjectToOutputpairList((StructEntityArray) structEntry, inputObject);
        return new JsonPairArray(structEntry.getColumnName(), jsonArray);
    }

    /**
     * Get Date.
     * @param structEntry
     * @param inputObject
     * @return
     * @throws BLLException
     */
    private JsonPairDate getDate(StructEntityInterface structEntry, InputObject inputObject) throws BLLException
    {
        StructEntityDate se = (StructEntityDate) structEntry;
        InputField inputField = inputObject.getField(se.getInputIndex());
        InputField backupField = null;
        if (se.getBackupIndex() != null)
        {
            backupField = inputObject.getField(se.getBackupIndex());
        }

        if (inputField != null
                && (inputField.getType() == InputFieldType.DATE
                || inputField.getType() == InputFieldType.STRING
                || inputField.getType() == InputFieldType.EMPTY))
        {
            Date date = null;
            if (inputField.getType() == InputFieldType.STRING)
            {
                DateFormatRule dateFormat = se.getDfr();
                date = dateFormat.applyRuleOn(inputField.getStringValue());
                if (date == null && backupField != null)
                {
                    date = dateFormat.applyRuleOn(backupField.getStringValue());
                }
                if (date == null)
                {
                    throw new BLLException("It was not possible to convert " + inputField.getStringValue() + " to a Date using " + dateFormat.getDateFormat() + " DateFormat.");
                }
            }
            else
            {
                date = inputField.getDateValue();
                if (date == null && backupField != null)
                {
                    date = backupField.getDateValue();
                }
            }

            if (date != null)
            {
                return new JsonPairDate(se.getColumnName(), date);
            }
            else
            {
                throw new BLLException("Was not able to get Date: " + se.getInputIndex() + ":" + se.getColumnName() + ":" + inputField.getValue());
            }

        }
        else
        {
            throw new BLLException("The field is missing or has wrong data type, check profile");
        }
    }

    /**
     * Get Double.
     * @param structEntry
     * @param inputObject
     * @return
     * @throws BLLException
     */
    private JsonPairDouble getDouble(StructEntityInterface structEntry, InputObject inputObject) throws BLLException
    {
        StructEntityDouble se = (StructEntityDouble) structEntry;
        InputField inputField = inputObject.getField(se.getInputIndex());
        InputField backupField = null;
        if (se.getBackupIndex() != null)
        {
            backupField = inputObject.getField(se.getBackupIndex());
        }

        if (inputField != null
                && (inputField.getType() == InputFieldType.NUMERIC
                || inputField.getType() == InputFieldType.STRING
                || inputField.getType() == InputFieldType.EMPTY))
        {
            Double fieldValue = null;
            if (inputField.getType() == InputFieldType.STRING)
            {
                fieldValue = Double.parseDouble(inputField.getStringValue());
                if (fieldValue == null && backupField != null)
                {
                    fieldValue = Double.parseDouble(backupField.getStringValue());
                }
                if (fieldValue == null)
                {
                    throw new BLLException("It was not possible to convert String value to Numeric");
                }
            }
            else
            {
                fieldValue = inputField.getDoubleValue();
                if (fieldValue == null && backupField != null)
                {
                    fieldValue = backupField.getDoubleValue();
                }
            }

            if (((StructEntityDouble) structEntry).getDefaultValue() != null)
            {
                return new JsonPairDouble(structEntry.getColumnName(), (Double) ((StructEntityDouble) structEntry).getDefaultValue()
                        .applyRuleOn(fieldValue));
            }
            else
            {
                return new JsonPairDouble(structEntry.getColumnName(), fieldValue);
            }
        }
        else
        {
            throw new BLLException("The field is missing or has wrong data type, check profile");
        }
    }

    /**
     * Get Integer.
     * @param structEntry
     * @param inputObject
     * @return
     * @throws BLLException
     */
    private JsonPairInteger getInteger(StructEntityInterface structEntry, InputObject inputObject) throws BLLException
    {
        StructEntityInteger se = (StructEntityInteger) structEntry;
        InputField inputField = inputObject.getField(se.getInputIndex());
        InputField backupField = null;
        if (se.getBackupIndex() != null)
        {
            backupField = inputObject.getField(se.getBackupIndex());
        }

        if (inputField != null
                && (inputField.getType() == InputFieldType.NUMERIC
                || inputField.getType() == InputFieldType.STRING
                || inputField.getType() == InputFieldType.EMPTY))
        {
            Integer fieldValue = null;
            if (inputField.getType() == InputFieldType.STRING)
            {
                fieldValue = Integer.parseInt(inputField.getStringValue());
                if (fieldValue == null && backupField != null)
                {
                    fieldValue = Integer.parseInt(backupField.getStringValue());
                }
                if (fieldValue == null)
                {
                    throw new BLLException("It was not possible to convert String value to Numeric");
                }
            }
            else
            {
                fieldValue = inputField.getIntValue();
                if (fieldValue == null && backupField != null)
                {
                    fieldValue = backupField.getIntValue();
                }
            }

            if (((StructEntityInteger) structEntry).getDefaultValue() != null)
            {
                return new JsonPairInteger(structEntry.getColumnName(), (Integer) ((StructEntityInteger) structEntry).getDefaultValue()
                        .applyRuleOn(fieldValue));
            }
            else
            {
                return new JsonPairInteger(structEntry.getColumnName(), fieldValue);
            }
        }
        else
        {
            throw new BLLException("The field is missing or has wrong data type, check profile");
        }
    }

    /**
     * Get Object.
     * @param structEntry
     * @param inputObject
     * @return
     * @throws BLLException
     */
    private JsonPairJson getObject(StructEntityInterface structEntry, InputObject inputObject) throws BLLException
    {
        List<OutputPair> jsonObject = mapInputObjectToOutputpairList((StructEntityObject) structEntry, inputObject);
        return new JsonPairJson(structEntry.getColumnName(), jsonObject);
    }

    /**
     * Get String.
     * @param structEntry
     * @param inputObject
     * @return
     * @throws BLLException
     */
    private JsonPairString getString(StructEntityInterface structEntry, InputObject inputObject) throws BLLException
    {
        StructEntityString se = (StructEntityString) structEntry;
        InputField inputField = inputObject.getField(se.getInputIndex());
        InputField backupField = null;
        if (se.getBackupIndex() != null)
        {
            backupField = inputObject.getField(se.getBackupIndex());
        }

        if (inputField != null
                && (inputField.getType() == InputFieldType.STRING
                || inputField.getType() == InputFieldType.NUMERIC
                || inputField.getType() == InputFieldType.EMPTY))
        {
            String fieldValue = null;
            if (inputField.getType() == InputFieldType.NUMERIC)
            {
                fieldValue = String.valueOf(inputField.getDoubleValue());
                if (fieldValue == null)
                {
                    fieldValue = String.valueOf(inputField.getIntValue());
                }
                if (fieldValue == null && backupField != null)
                {
                    fieldValue = String.valueOf(backupField.getDoubleValue());
                }
                if (fieldValue == null && backupField != null)
                {
                    fieldValue = String.valueOf(backupField.getIntValue());
                }
                if (fieldValue == null)
                {
                    throw new BLLException("It was not possible to convert Numeric value to String");
                }
            }
            else
            {
                fieldValue = inputField.getStringValue();
                if (fieldValue == null || fieldValue.isEmpty() && backupField != null)
                {
                    fieldValue = backupField.getStringValue();
                }
            }

            if (se.getDefaultValue() != null)
            {
                DefaultStringRule dsr = (DefaultStringRule) se.getDefaultValue();
                return new JsonPairString(se.getColumnName(), dsr.applyRuleOn(fieldValue));
            }
            else
            {
                return new JsonPairString(structEntry.getColumnName(), fieldValue);
            }
        }
        else
        {
            throw new BLLException("The field is missing or has wrong data type, check profile");
        }
    }
}
