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
import java.sql.SQLType;
import java.sql.Statement;
import java.util.List;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.CollectionEntry;
import shoreline_exam_2018.be.output.structure.SimpleEntry;
import shoreline_exam_2018.be.output.structure.entry.StructEntryInterface;
import shoreline_exam_2018.be.output.structure.entry.StructEntryObject;
import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.database.connection.DBConnector;

/**
 *
 * @author Asbamz
 */
public class ProfileDAO
{
    private DBConnector dbc;

    public ProfileDAO()
    {
        dbc = new DBConnector();
    }

    /**
     * Adds a new profile.
     * @param name
     * @param structure
     * @param createdBy
     * @return
     * @throws DALException
     */
    public Profile addProfile(String name, StructEntryObject structure, int createdBy) throws DALException
    {
        Profile profile;
        int id;

        try (Connection con = dbc.getConnection())
        {
            String sql = "INSERT INTO Profile VALUES(?, ?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setInt(2, createdBy);

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);

            for (StructEntryInterface structEntryInterface : structure.getCollection())
            {
                if (structEntryInterface instanceof CollectionEntry)
                {
                    CollectionEntry ce = (CollectionEntry) structEntryInterface;
                    addProfileStructureCollection(id, ce);
                }
                else if (structEntryInterface instanceof SimpleEntry)
                {
                    SimpleEntry se = (SimpleEntry) structEntryInterface;
                    addProfileStructureSimple(id, se);
                }
            }
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }

        profile = new Profile(id, name, structure, String.valueOf(createdBy));
        return profile;
    }

    /**
     * Adds simple structure.
     * @param profileId
     * @param se
     * @throws SQLException
     */
    private void addProfileStructureSimple(int profileId, SimpleEntry se) throws SQLException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "INSERT INTO ProfileStructureSimple VALUES(?, ?, ?, ?);";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);
            statement.setString(2, se.getColumnName());
            statement.setInt(3, se.getInputIndex());
            statement.setString(4, se.getSST().name());

            statement.executeUpdate();
        }
    }

    /**
     * Adds collection.
     * @param profileId
     * @param ce
     * @throws SQLException
     */
    private void addProfileStructureCollection(int profileId, CollectionEntry ce) throws SQLException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql;
            if (ce.getCST() == CollectionStructType.ARRAY)
            {
                sql = "INSERT INTO ProfileStructureArray VALUES(?, ?);";
            }
            else
            {
                sql = "INSERT INTO ProfileStructureObject VALUES(?, ?);";
            }

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, profileId);
            statement.setString(2, ce.getColumnName());

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            int id2 = addListOfStructEntries(id, ce.getCollection());

            if (ce.getCST() == CollectionStructType.ARRAY)
            {
                sql = "INSERT INTO ArrayToProfile VALUES(?, ?);";
            }
            else
            {
                sql = "INSERT INTO ObjectToProfile VALUES(?, ?);";
            }

            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            statement.setInt(2, id2);

            statement.executeUpdate();
        }
    }

    /**
     * Add all entries with recursion.
     * @param id
     * @param seiLst
     * @return
     * @throws SQLException
     */
    private int addListOfStructEntries(int id, List<StructEntryInterface> seiLst) throws SQLException
    {
        try (Connection con = dbc.getConnection())
        {
            String sql = "INSERT INTO Profile VALUES(?, ?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, "generated_for_" + id);
            statement.setNull(2, java.sql.Types.INTEGER);

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);

            for (StructEntryInterface structEntryInterface : seiLst)
            {
                if (structEntryInterface instanceof SimpleEntry)
                {
                    addProfileStructureSimple(id, (SimpleEntry) structEntryInterface);
                }
                else if (structEntryInterface instanceof CollectionEntry)
                {
                    addProfileStructureCollection(id, (CollectionEntry) structEntryInterface);
                }
            }

            return id;
        }
    }
}
