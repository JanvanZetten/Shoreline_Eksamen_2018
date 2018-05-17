/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be;

import java.util.Date;

/**
 *
 * @author janvanzetten
 */
public class InputField {
    String stringField;
    Date dateField;
    double numericfield;
    InputFieldType type;

    /**
     * Field with no initial value
     * @param type 
     */
    public InputField(InputFieldType type) {
        this.type = type;
    }
    
    /**
     * Field with initial value
     * @param type the fieleds type as enum 
     * @param value the initial value
     */
    public InputField(InputFieldType type, Object value) {
        this.type = type;
        
        switch(type){
            case STRING:
                stringField = (String) value;
                break;
            case DATE:
                dateField = (Date) value;
                break;
            case NUMERIC:
                numericfield = (Double) value;
                break;
            default:
                break;
        }
    }
    
    
    /**
     * Gets the value as object
     * @return 
     */
    public Object getValue(){
        switch(type){
            case STRING:
                return stringField;
            case DATE:
                return dateField;
            case NUMERIC:
                return numericfield;
            default:
                return null;
        }
    }
    
    
    /**
     * Gets the string value. Should only be used if type is STRING
     * @return the string or empty string if type is empty
     */
    public String getStringValue(){
        if (type == InputFieldType.EMPTY){
            return "";
        }
        return stringField;
    }
    
    /**
     * Gets the date value. Should only be used if type is DATE
     * @return the date or null when type is empty
     */
    public Date getDateValue(){
        if (type == InputFieldType.EMPTY){
            return null;
        }
        return dateField;
    }
    
    /**
     * Gets the double value. Should only be used if type is NUMERIC
     * @return 
     */
    public double getDoubleValue(){
        return numericfield;
    }
    
    /**
     * Gets the int value. Should only be used if type is NUMERIC
     * @return 
     */
    public int getIntValue(){
        Double asdouble = numericfield;
        return asdouble.intValue();
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public void setDateField(Date dateField) {
        this.dateField = dateField;
    }

    public void setNumericfield(double numericfield) {
        this.numericfield = numericfield;
    }
    
    public InputFieldType getType(){
        return type;
    }
    
    
    
    
    
    
    
    
    
}
