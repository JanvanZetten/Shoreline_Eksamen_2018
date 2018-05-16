/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be;

/**
 * Business Entity representation of a Log.
 * @author Asbamz
 */
public class Log
{
    private int id;
    private LogType type;
    private String message;
    private int createdBy;

    public Log(int id, LogType type, String message, int createdBy)
    {
        this.id = id;
        this.type = type;
        this.message = message;
        this.createdBy = createdBy;
    }

    public int getId()
    {
        return id;
    }

    public LogType getType()
    {
        return type;
    }

    public String getMessage()
    {
        return message;
    }

    public int getCreatedBy()
    {
        return createdBy;
    }
}
