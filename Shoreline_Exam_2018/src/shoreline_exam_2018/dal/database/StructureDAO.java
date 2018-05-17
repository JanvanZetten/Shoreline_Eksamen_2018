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
import shoreline_exam_2018.be.output.structure.CollectionEntity;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;
import shoreline_exam_2018.be.output.structure.entry.StructEntityArray;
import shoreline_exam_2018.be.output.structure.entry.StructEntityDate;
import shoreline_exam_2018.be.output.structure.entry.StructEntityDouble;
import shoreline_exam_2018.be.output.structure.entry.StructEntityInteger;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.be.output.structure.entry.StructEntityString;
import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.database.connection.DBConnectorPool;

/**
 *
 * @author Asbamz
 */
public class StructureDAO
{
    private DBConnectorPool dbcp;

    public StructureDAO()
    {
        dbcp = new DBConnectorPool();
    }

    /**
     * Adds a new Structure.
     * @param name
     * @param structure
     * @param createdBy
     * @return
     * @throws DALException
     */
    public StructEntityObject addStructure(String name, StructEntityObject structure, int createdBy) throws DALException
    {
        int id;
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "INSERT INTO Structure VALUES(?, ?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setInt(2, createdBy);

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);

            for (StructEntityInterface structEntryInterface : structure.getCollection())
            {
                if (structEntryInterface instanceof CollectionEntity)
                {
                    CollectionEntity ce = (CollectionEntity) structEntryInterface;
                    addProfileStructureCollection(id, ce);
                }
                else if (structEntryInterface instanceof SimpleEntity)
                {
                    SimpleEntity se = (SimpleEntity) structEntryInterface;
                    addProfileStructureSimple(id, se);
                }
            }
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        finally
        {
            dbcp.checkIn(con);
        }

        return structure;
    }

    /**
     * Adds simple structure.
     * @param structureId
     * @param se
     * @throws SQLException
     */
    private void addProfileStructureSimple(int structureId, SimpleEntity se) throws SQLException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "INSERT INTO StructSimple VALUES(?, ?, ?);";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, structureId);
            statement.setString(2, se.getColumnName());
            statement.setString(3, se.getSST().name());

            statement.executeUpdate();
        }
        finally
        {
            dbcp.checkIn(con);
        }
    }

    /**
     * Adds collection.
     * @param structureId
     * @param ce
     * @throws SQLException
     */
    private void addProfileStructureCollection(int structureId, CollectionEntity ce) throws SQLException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql;
            if (ce.getCST() == CollectionStructType.ARRAY)
            {
                sql = "INSERT INTO StructArray VALUES(?, ?);";
            }
            else
            {
                sql = "INSERT INTO StructObject VALUES(?, ?);";
            }

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, structureId);
            statement.setString(2, ce.getColumnName());

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);
            int id2 = addListOfStructEntries(id, ce.getCollection());

            if (ce.getCST() == CollectionStructType.ARRAY)
            {
                sql = "INSERT INTO ArrayToStruct VALUES(?, ?);";
            }
            else
            {
                sql = "INSERT INTO ObjectToStruct VALUES(?, ?);";
            }

            statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            statement.setInt(2, id2);

            statement.executeUpdate();
        }
        finally
        {
            dbcp.checkIn(con);
        }
    }

    /**
     * Add all entries with recursion.
     * @param id
     * @param seiLst
     * @return
     * @throws SQLException
     */
    private int addListOfStructEntries(int id, List<StructEntityInterface> seiLst) throws SQLException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "INSERT INTO Structure VALUES(?, ?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, "generated_for_" + id);
            statement.setNull(2, java.sql.Types.INTEGER);

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);

            for (StructEntityInterface structEntryInterface : seiLst)
            {
                if (structEntryInterface instanceof SimpleEntity)
                {
                    addProfileStructureSimple(id, (SimpleEntity) structEntryInterface);
                }
                else if (structEntryInterface instanceof CollectionEntity)
                {
                    addProfileStructureCollection(id, (CollectionEntity) structEntryInterface);
                }
            }

            return id;
        }
        finally
        {
            dbcp.checkIn(con);
        }
    }

    /**
     * Get All Structures.
     * @return
     * @throws DALException
     */
    public List<StructEntityObject> getAllStructures() throws DALException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "SELECT * FROM Structure WHERE createdBy IS NOT NULL";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<StructEntityObject> structures = new ArrayList<>();

            while (rs.next())
            {
                int profileId = rs.getInt("id");
                String name = rs.getString("name");
                StructEntityObject structure = new StructEntityObject(name, getStructure(profileId));

                structures.add(structure);

            }

            return structures;
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
     * Gets all entries which belongs to the given structure id.
     * @param structureId
     * @return
     * @throws SQLException
     */
    private List<StructEntityInterface> getStructure(int structureId) throws SQLException
    {
        List<StructEntityInterface> lst = new ArrayList<>();
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            /*
                Simple
             */

            String sql = "SELECT * FROM StructSimple WHERE structId = ?;";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, structureId);

            ResultSet rs = statement.executeQuery();

            StructEntityObject seo;

            List<StructEntityInterface> simples = new ArrayList<>();
            List<StructEntityInterface> objects = new ArrayList<>();
            List<StructEntityInterface> arrays = new ArrayList<>();

            int id;
            String sst;

            while (rs.next())
            {
                sst = rs.getString("sst");

                if (sst.equalsIgnoreCase(SimpleStructType.DATE.name()))
                {
                    simples.add(new StructEntityDate(rs.getString("columnName"), -1));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.DOUBLE.name()))
                {
                    simples.add(new StructEntityDouble(rs.getString("columnName"), -1));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.INTEGER.name()))
                {
                    simples.add(new StructEntityInteger(rs.getString("columnName"), -1));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.STRING.name()))
                {
                    simples.add(new StructEntityString(rs.getString("columnName"), -1));
                }
            }

            /*
                Object
             */
            sql = "SELECT *, ots.structId AS otsId FROM StructObject so INNER JOIN ObjectToStruct ots ON so.id = ots.objectId WHERE so.structId = ?;";

            statement = con.prepareStatement(sql);
            statement.setInt(1, structureId);

            rs = statement.executeQuery();

            while (rs.next())
            {
                id = rs.getInt("id");

                objects.add(new StructEntityObject(rs.getString("columnName"), getStructure(rs.getInt("otsId"))));
            }

            /*
                Array
             */
            sql = "SELECT *, ats.structId AS atsId FROM StructArray sa INNER JOIN ArrayToStruct ats ON sa.id = ats.arrayId WHERE sa.structId = ?;";

            statement = con.prepareStatement(sql);
            statement.setInt(1, structureId);

            rs = statement.executeQuery();

            while (rs.next())
            {
                id = rs.getInt("id");

                arrays.add(new StructEntityArray(rs.getString("columnName"), getStructure(rs.getInt("atsId"))));
            }

            lst.addAll(simples);
            lst.addAll(objects);
            lst.addAll(arrays);
            return lst;
        }
        finally
        {
            dbcp.checkIn(con);
        }
    }
}
