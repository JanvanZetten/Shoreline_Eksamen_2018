/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.database.connection.DBConnector;

/**
 *
 * @author alexl
 */
public class DBChangeDAO {

    DBConnector connecter;

    public DBChangeDAO() {
        connecter = new DBConnector();
    }

    public int getNewestLog() throws DALException {
        try(Connection con = connecter.getConnection()){
            String sql = "SELECT max(id) FROM Log;";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            rs.next();

            return rs.getInt(1);
        } catch (SQLException ex) {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

}
