/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be;

import java.util.Date;
import java.util.Objects;

/**
 * Business Entity representation of a Log.
 * @author Asbamz
 */
public class Log
{
    private int id;
    private LogType type;
    private String message;
    private User createdBy;
    private Date date;

    public Log(int id, LogType type, String message, User createdBy, Date date)
    {
        this.id = id;
        this.type = type;
        this.message = message;
        this.createdBy = createdBy;
        this.date = date;
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

    public User getCreatedBy()
    {
        return createdBy;
    }

    public Date getDate()
    {
        return date;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 37 * hash + this.id;
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + Objects.hashCode(this.message);
        hash = 37 * hash + Objects.hashCode(this.createdBy);
        hash = 37 * hash + Objects.hashCode(this.date);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Log other = (Log) obj;
        if (this.id != other.id)
        {
            return false;
        }
        if (!Objects.equals(this.message, other.message))
        {
            return false;
        }
        if (this.type != other.type)
        {
            return false;
        }
        if (!Objects.equals(this.createdBy, other.createdBy))
        {
            return false;
        }
        if (!Objects.equals(this.date, other.date))
        {
            return false;
        }
        return true;
    }

}
