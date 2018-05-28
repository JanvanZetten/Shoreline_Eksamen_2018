/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.output.rule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Asbamz
 */
public class DateFormatRule extends Rule<Date, String>
{
    private final String DATE_FORMAT;

    /**
     * The following pattern letters are defined (all other characters from
     * <code>'A'</code> to <code>'Z'</code> and from <code>'a'</code> to
     * <code>'z'</code> are reserved):
     * <blockquote>
     * <table border=0 cellspacing=3 cellpadding=0 summary="Chart shows pattern letters, date/time component, presentation, and examples.">
     * <tr style="background-color: rgb(204, 204, 255);">
     * <th align=left>Letter
     * <th align=left>Date or Time Component
     * <th align=left>Presentation
     * <th align=left>Examples
     * <tr>
     * <td><code>G</code>
     * <td>Era designator
     * <td><a href="#text">Text</a>
     * <td><code>AD</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>y</code>
     * <td>Year
     * <td><a href="#year">Year</a>
     * <td><code>1996</code>; <code>96</code>
     * <tr>
     * <td><code>Y</code>
     * <td>Week year
     * <td><a href="#year">Year</a>
     * <td><code>2009</code>; <code>09</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>M</code>
     * <td>Month in year (context sensitive)
     * <td><a href="#month">Month</a>
     * <td><code>July</code>; <code>Jul</code>; <code>07</code>
     * <tr>
     * <td><code>L</code>
     * <td>Month in year (standalone form)
     * <td><a href="#month">Month</a>
     * <td><code>July</code>; <code>Jul</code>; <code>07</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>w</code>
     * <td>Week in year
     * <td><a href="#number">Number</a>
     * <td><code>27</code>
     * <tr>
     * <td><code>W</code>
     * <td>Week in month
     * <td><a href="#number">Number</a>
     * <td><code>2</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>D</code>
     * <td>Day in year
     * <td><a href="#number">Number</a>
     * <td><code>189</code>
     * <tr>
     * <td><code>d</code>
     * <td>Day in month
     * <td><a href="#number">Number</a>
     * <td><code>10</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>F</code>
     * <td>Day of week in month
     * <td><a href="#number">Number</a>
     * <td><code>2</code>
     * <tr>
     * <td><code>E</code>
     * <td>Day name in week
     * <td><a href="#text">Text</a>
     * <td><code>Tuesday</code>; <code>Tue</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>u</code>
     * <td>Day number of week (1 = Monday, ..., 7 = Sunday)
     * <td><a href="#number">Number</a>
     * <td><code>1</code>
     * <tr>
     * <td><code>a</code>
     * <td>Am/pm marker
     * <td><a href="#text">Text</a>
     * <td><code>PM</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>H</code>
     * <td>Hour in day (0-23)
     * <td><a href="#number">Number</a>
     * <td><code>0</code>
     * <tr>
     * <td><code>k</code>
     * <td>Hour in day (1-24)
     * <td><a href="#number">Number</a>
     * <td><code>24</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>K</code>
     * <td>Hour in am/pm (0-11)
     * <td><a href="#number">Number</a>
     * <td><code>0</code>
     * <tr>
     * <td><code>h</code>
     * <td>Hour in am/pm (1-12)
     * <td><a href="#number">Number</a>
     * <td><code>12</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>m</code>
     * <td>Minute in hour
     * <td><a href="#number">Number</a>
     * <td><code>30</code>
     * <tr>
     * <td><code>s</code>
     * <td>Second in minute
     * <td><a href="#number">Number</a>
     * <td><code>55</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>S</code>
     * <td>Millisecond
     * <td><a href="#number">Number</a>
     * <td><code>978</code>
     * <tr>
     * <td><code>z</code>
     * <td>Time zone
     * <td><a href="#timezone">General time zone</a>
     * <td><code>Pacific Standard Time</code>; <code>PST</code>;
     * <code>GMT-08:00</code>
     * <tr style="background-color: rgb(238, 238, 255);">
     * <td><code>Z</code>
     * <td>Time zone
     * <td><a href="#rfc822timezone">RFC 822 time zone</a>
     * <td><code>-0800</code>
     * <tr>
     * <td><code>X</code>
     * <td>Time zone
     * <td><a href="#iso8601timezone">ISO 8601 time zone</a>
     * <td><code>-08</code>; <code>-0800</code>;  <code>-08:00</code>
     * </table>
     * </blockquote>
     *
     * Example: 2018-12/30 would be yyyy-MM/dd.
     * @param columnIndex
     * @param dateFormat
     */
    public DateFormatRule(String dateFormat, int columnIndex)
    {
        super(columnIndex);
        DATE_FORMAT = dateFormat;
    }

    /**
     * Converts string to date. Given the String is formated as DATE_FORMAT.
     * Returns null if it was not able to parse.
     * @param stringDate
     * @return
     */
    @Override
    public Date applyRuleOn(String stringDate)
    {
        Date date = null;
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);

        try
        {
            date = ft.parse(stringDate);
        }
        catch (ParseException ex)
        {
        }

        return date;
    }

    public String getDateFormat()
    {
        return DATE_FORMAT;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.DATE_FORMAT);
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
        final DateFormatRule other = (DateFormatRule) obj;
        if (!Objects.equals(this.DATE_FORMAT, other.DATE_FORMAT))
        {
            return false;
        }
        return true;
    }

}
