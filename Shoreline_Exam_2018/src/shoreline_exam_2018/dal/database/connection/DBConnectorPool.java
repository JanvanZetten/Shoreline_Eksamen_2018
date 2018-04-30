/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.database.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Connector pool for SQL Connetions. This class prevent the program to open and
 * close connections to the database all the time.
 * @author Asbamz
 */
public class DBConnectorPool
{
    private long expirationTime;

    private ConcurrentHashMap<Connection, Long> locked, unlocked;
    private DBConnector dbc;

    /**
     * Construct DBConnectorPool.
     */
    public DBConnectorPool()
    {
        expirationTime = 5000;
        locked = new ConcurrentHashMap<>();
        unlocked = new ConcurrentHashMap<>();
        dbc = new DBConnector();
    }

    /**
     * Create new connection.
     * @return
     */
    private Connection create()
    {
        try
        {
            return dbc.getConnection();
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    /**
     * Check if valid connection / open connection.
     * @param con
     * @return
     */
    public boolean validate(Connection con)
    {
        try
        {
            return !con.isClosed();
        }
        catch (SQLException ex)
        {
            return false;
        }
    }

    /**
     * Close connection.
     * @param con
     */
    public void expire(Connection con)
    {
        try
        {
            con.close();
        }
        catch (SQLException ex)
        {
        }
    }

    /**
     * Get connection.
     * @return
     */
    public synchronized Connection checkOut()
    {
        long now = System.currentTimeMillis();
        Connection con;
        if (unlocked.size() > 0)
        {
            Enumeration<Connection> e = unlocked.keys();
            while (e.hasMoreElements())
            {
                con = e.nextElement();
                if ((now - unlocked.get(con)) > expirationTime)
                {
                    // object has expired
                    unlocked.remove(con);
                    expire(con);
                    con = null;
                }
                else
                {
                    if (validate(con))
                    {
                        unlocked.remove(con);
                        locked.put(con, now);
                        return con;
                    }
                    else
                    {
                        // object failed validation
                        unlocked.remove(con);
                        expire(con);
                        con = null;
                    }
                }
            }
        }
        // no objects available, create a new one
        con = create();
        locked.put(con, now);
        return con;
    }

    /**
     * Unblock connection once finished with it.
     * @param con
     */
    public synchronized void checkIn(Connection con)
    {
        locked.remove(con);
        unlocked.put(con, System.currentTimeMillis());
    }
}
