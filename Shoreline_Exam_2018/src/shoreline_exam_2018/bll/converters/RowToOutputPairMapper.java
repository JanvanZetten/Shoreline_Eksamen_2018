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
import shoreline_exam_2018.be.InputField;
import shoreline_exam_2018.be.InputFieldType;
import shoreline_exam_2018.be.InputObject;
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
     * get a List of outputpairs with the data from the inputObject and the
     * structure and fields from the structObject
     *
     * @param structObject the object describing which data should convert to
     * what
     * @param inputObject the inputObject from which to load the data
     * @return a list of outputpairs
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
                //check what object this structE
                if (structEntry instanceof StructEntityArray)
                {
                    List<OutputPair> jsonArray = mapInputObjectToOutputpairList((StructEntityArray) structEntry, inputObject);
                    output.add(new JsonPairArray(structEntry.getColumnName(), jsonArray));

                }
                else if (structEntry instanceof StructEntityDate)
                {
                    StructEntityDate se = (StructEntityDate) structEntry;
                    InputField inputField = inputObject.getField(se.getInputIndex());
                    if (inputField != null
                            && (inputField.getType() == InputFieldType.DATE
                            || inputField.getType() == InputFieldType.STRING || inputField.getType() == InputFieldType.EMPTY))
                    {
                        Date date = null;
                        if (inputField.getType() == InputFieldType.STRING)
                        {
                            DateFormatRule dateFormat = se.getDfr();
                            date = dateFormat.applyRuleOn(inputField.getStringValue());
                            if (date == null)
                            {
                                throw new BLLException("It was not possible to convert " + inputField.getStringValue() + " to a Date using " + dateFormat.getDateFormat() + " DateFormat.");
                            }
                        }
                        else
                        {
                            date = inputField.getDateValue();
                        }

                        
                        output.add(new JsonPairDate(se.getColumnName(), date));
                        
                    }
                    else
                    {
                        throw new BLLException("The field is missing or has wrong data type, check profile");
                    }

                }
                else if (structEntry instanceof StructEntityDouble)
                {
                    if (inputObject.getField(((StructEntityDouble) structEntry).getInputIndex()) != null
                            && (inputObject.getField(((StructEntityDouble) structEntry).getInputIndex()).getType() == InputFieldType.NUMERIC
                            || inputObject.getField(((StructEntityDouble) structEntry).getInputIndex()).getType() == InputFieldType.EMPTY))
                    {
                        if (((StructEntityDouble) structEntry).getDefaultValue() != null)
                        {
                            output.add(new JsonPairDouble(structEntry.getColumnName(), (Double) ((StructEntityDouble) structEntry).getDefaultValue()
                                    .applyRuleOn(inputObject.getField(((StructEntityDouble) structEntry).getInputIndex()).getDoubleValue())));
                        }
                        else
                        {
                            output.add(new JsonPairDouble(structEntry.getColumnName(), inputObject.getField(
                                    ((StructEntityDouble) structEntry).getInputIndex()).getDoubleValue()));
                        }
                    }
                    else
                    {
                        throw new BLLException("The field is missing or has wrong data type, check profile");
                    }
                }
                else if (structEntry instanceof StructEntityInteger)
                {
                    if (inputObject.getField(((StructEntityInteger) structEntry).getInputIndex()) != null
                            && (inputObject.getField(((StructEntityInteger) structEntry).getInputIndex()).getType() == InputFieldType.NUMERIC
                            || inputObject.getField(((StructEntityInteger) structEntry).getInputIndex()).getType() == InputFieldType.EMPTY))
                    {
                        if (((StructEntityInteger) structEntry).getDefaultValue() != null)
                        {
                            output.add(new JsonPairInteger(structEntry.getColumnName(), (Integer) ((StructEntityInteger) structEntry).getDefaultValue()
                                    .applyRuleOn(inputObject.getField(((StructEntityInteger) structEntry).getInputIndex()).getIntValue())));
                        }
                        else
                        {
                            output.add(new JsonPairInteger(structEntry.getColumnName(), inputObject.getField(
                                    ((StructEntityInteger) structEntry).getInputIndex()).getIntValue()));
                        }
                    }
                    else
                    {
                        throw new BLLException("The field is missing or has wrong data type, check profile");
                    }
                }
                else if (structEntry instanceof StructEntityObject)
                {
                    List<OutputPair> jsonObject = mapInputObjectToOutputpairList((StructEntityObject) structEntry, inputObject);
                    output.add(new JsonPairJson(structEntry.getColumnName(), jsonObject));
                }
                else if (structEntry instanceof StructEntityString)
                {
                    StructEntityString se = (StructEntityString) structEntry;
                    InputField inputField = inputObject.getField(se.getInputIndex());

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
                            if (fieldValue == null)
                            {
                                throw new BLLException("It was not possible to convert Numeric value to String");
                            }
                        }
                        else
                        {
                            fieldValue = inputField.getStringValue();
                        }
                        if (se.getDefaultValue() != null)
                        {
                            DefaultStringRule dsr = (DefaultStringRule) se.getDefaultValue();
                            output.add(new JsonPairString(se.getColumnName(), dsr.applyRuleOn(fieldValue)));
                        }
                        else
                        {
                            output.add(new JsonPairString(structEntry.getColumnName(), fieldValue));
                        }
                    }
                    else
                    {
                        throw new BLLException("The field is missing or has wrong data type, check profile");
                    }
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

}
