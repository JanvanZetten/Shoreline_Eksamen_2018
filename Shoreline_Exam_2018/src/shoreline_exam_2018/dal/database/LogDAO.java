/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.database.connection.DBConnectorPool;

/**
 *
 * @author Asbamz
 */
public class LogDAO
{
    private DBConnectorPool dbcp;

    public LogDAO()
    {
        dbcp = new DBConnectorPool();
    }

    /**
     * Gets all logs from database.
     * @return
     * @throws DALException
     */
    public List<Log> getAllLogs() throws DALException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "SELECT * FROM Log";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<Log> logs = new ArrayList<>();

            while (rs.next())
            {
                int logId = rs.getInt("id");
                String type = rs.getString("type");
                String message = rs.getString("message");
                int createdBy = rs.getInt("createdBy");
                Log log = new Log(logId, LogType.valueOf(type), message, createdBy);

                logs.add(log);
            }

            return logs;
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        finally
        {
            dbcp.checkIn(con);
        }
    }

    /**
     * Adds log to database.
     * @param type
     * @param message
     * @param creator
     * @return
     * @throws DALException
     */
    public Log addLog(LogType type, String message, User creator) throws DALException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "INSERT INTO Log VALUES(?, ?, ?);";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, type.name());
            statement.setString(2, message);
            statement.setInt(3, creator.getId());

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);

            return new Log(id, type, message, creator.getId());
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        finally
        {
            dbcp.checkIn(con);
        }
    }
}