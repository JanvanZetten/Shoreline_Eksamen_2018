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
import shoreline_exam_2018.be.User;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.database.connection.DBConnectorPool;

/**
 *
 * @author alexl
 */
public class UserDAO {
    
    private DBConnectorPool dbcp;
    
    public UserDAO()
    {
        dbcp = new DBConnectorPool();
    }
    
    public User login(String username, String password) throws DALException
    {
        
        Connection con = null;
        int id;
        
        try
        {
            con = dbcp.checkOut();
            String sql = "SELECT * FROM [User] WHERE Name = ? AND Password = ?";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if (!rs.next())
            {
                throw new DALException("The username or password is ivalid, create user if you do not have one.");
            }
            id = rs.getInt(1);
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        finally
        {
            dbcp.checkIn(con);
        }
        return new User(id, username, password);
    }
    
}
