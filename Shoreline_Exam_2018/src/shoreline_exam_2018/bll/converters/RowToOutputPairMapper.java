/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.converters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import shoreline_exam_2018.be.InputFieldType;
import shoreline_exam_2018.be.InputObject;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.be.output.jsonpair.*;
import shoreline_exam_2018.be.output.structure.*;
import shoreline_exam_2018.be.output.structure.entry.*;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.bll.BLLException;

/**
 *
 * @author Jan
 */
public class RowToOutputPairMapper
{

    /**
     * get a List of outputpairs with the data from the inputObject and the structure
 and fields from the structObject
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
    public List<OutputPair> mapRowToOutputpairListWithEntityCollection(CollectionEntity structObject, InputObject inputObject) throws BLLException
    {
        List<StructEntityInterface> collection = structObject.getCollection();

        List<OutputPair> output = new ArrayList<>();

        for (StructEntityInterface structEntry : collection)
        {
            //check what object this structE
            if (structEntry instanceof StructEntityArray)
            {
                List<OutputPair> jsonArray = mapRowToOutputpairListWithEntityCollection((StructEntityArray) structEntry, inputObject);
                output.add(new JsonPairArray(structEntry.getColumnName(), jsonArray));

            }
            else if (structEntry instanceof StructEntityDate)
            {
                if (inputObject.getField(((StructEntityDate) structEntry).getInputIndex()) != null 
                        && (inputObject.getField(((StructEntityDate) structEntry).getInputIndex()).getType() == InputFieldType.DATE
                        || inputObject.getField(((StructEntityDate) structEntry).getInputIndex()).getType() == InputFieldType.EMPTY) )
                {
                    if (((StructEntityDate) structEntry).getDefaultValue() != null){
                    output.add(new JsonPairDate(structEntry.getColumnName(), (Date) ((StructEntityDate) structEntry).getDefaultValue()
                            .applyRuleOn(inputObject.getField(((StructEntityDate) structEntry).getInputIndex()).getDateValue())));
                    }else{
                        output.add(new JsonPairDate(structEntry.getColumnName(), inputObject.getField((
                                (StructEntityDate) structEntry).getInputIndex()).getDateValue()));
                    }
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
                    if (((StructEntityDouble) structEntry).getDefaultValue() != null){
                    output.add(new JsonPairDouble(structEntry.getColumnName(), (Double) ((StructEntityDouble) structEntry).getDefaultValue()
                            .applyRuleOn(inputObject.getField(((StructEntityDouble) structEntry).getInputIndex()).getDoubleValue())));
                    }
                    else {
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
                    if (((StructEntityInteger) structEntry).getDefaultValue() != null){
                    output.add(new JsonPairInteger(structEntry.getColumnName(), (Integer) ((StructEntityInteger) structEntry).getDefaultValue()
                            .applyRuleOn(inputObject.getField(((StructEntityInteger) structEntry).getInputIndex()).getIntValue())));
                    }
                    else{
                        output.add(new JsonPairInteger(structEntry.getColumnName(),inputObject.getField(
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
                List<OutputPair> jsonObject = mapRowToOutputpairListWithEntityCollection((StructEntityObject) structEntry, inputObject);
                output.add(new JsonPairJson(structEntry.getColumnName(), jsonObject));

            }
            else if (structEntry instanceof StructEntityString)
            {
                if (inputObject.getField(((StructEntityString) structEntry).getInputIndex()) != null && 
                        (inputObject.getField(((StructEntityString) structEntry).getInputIndex()).getType() == InputFieldType.STRING || 
                        inputObject.getField(((StructEntityString) structEntry).getInputIndex()).getType() == InputFieldType.EMPTY))
                {
                    if (((StructEntityString) structEntry).getDefaultValue() != null){
                    output.add(new JsonPairString(structEntry.getColumnName(), (String) ((StructEntityString) structEntry).getDefaultValue()
                            .applyRuleOn(inputObject.getField(((StructEntityString) structEntry).getInputIndex()).getStringValue())));
                    }
                    else {
                        output.add(new JsonPairString(structEntry.getColumnName(), inputObject.getField(
                                ((StructEntityString) structEntry).getInputIndex()).getStringValue()));
                    }
                }
                else
                {
                    System.out.println("String: " + inputObject.getField(((StructEntityString) structEntry).getInputIndex()).getType());
                    throw new BLLException("The field is missing or has wrong data type, check profile");
                }
            }
            else
            {
                throw new BLLException("The struct entry type is not supported");
            }

        }
        return output;

    }

}
