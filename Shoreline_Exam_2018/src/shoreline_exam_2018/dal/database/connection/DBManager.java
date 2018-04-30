/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.database.connection;

import java.sql.Connection;
import java.sql.SQLException;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.database.connection.DBConnector;

/**
 *
 * @author alexl
 */
public class DBManager {
    
    private DBConnector dbCon;
    
    public DBManager() {
        dbCon = new DBConnector();
    }
    
    public void MOCKMETHOD() throws DALException
    {
        try (Connection con = dbCon.getConnection())
        {
            //ENTER SOMETHING HERE OK?
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }
    
}
