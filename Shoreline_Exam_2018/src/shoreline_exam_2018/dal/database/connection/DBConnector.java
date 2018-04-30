/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.database.connection;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

/**
 *
 * @author Asbamz
 */
public class DBConnector
{
    private final String SERVER_NAME = "EASV-DB2";
    private final int PORT_NUMBER = 1433;
    private final String DATABASE_NAME = "CS2017A_EnGruppe_Shoreline";
    private final String USER = "CS2017A_3_java";
    private final String PASSWORD = "javajava";

    private SQLServerDataSource dataSource;

    /**
     * Constructor saves connection information.
     */
    public DBConnector()
    {
        dataSource = new SQLServerDataSource();

        dataSource.setServerName(SERVER_NAME);
        dataSource.setPortNumber(PORT_NUMBER);
        dataSource.setDatabaseName(DATABASE_NAME);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);
    }

    /**
     * Gets connection.
     *
     * @return SQLServerDataSource.
     * @throws SQLServerException
     */
    public Connection getConnection() throws SQLServerException
    {
        return dataSource.getConnection();
    }
}
