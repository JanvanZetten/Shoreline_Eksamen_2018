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

    public InputField(InputFieldType type) {
        this.type = type;
    }
    
    public InputField(InputFieldType type, Object field) {
        this.type = type;
        
        switch(type){
            case STRING:
                stringField = (String) field;
                break;
            case DATE:
                dateField = (Date) field;
                break;
            case NUMERIC:
                numericfield = (Double) field;
                break;
            default:
                break;
        }
    }
    
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
    
    public String getStringValue(){
        return stringField;
    }
    
    public Date getDateValue(){
        return dateField;
    }
    
    public double getDoubleValue(){
        return numericfield;
    }
    
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
