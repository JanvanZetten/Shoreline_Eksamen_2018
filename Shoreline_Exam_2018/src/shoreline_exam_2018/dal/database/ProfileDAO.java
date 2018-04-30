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
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.CollectionEntry;
import shoreline_exam_2018.be.output.structure.SimpleEntry;
import shoreline_exam_2018.be.output.structure.entry.StructEntryArray;
import shoreline_exam_2018.be.output.structure.entry.StructEntryDate;
import shoreline_exam_2018.be.output.structure.entry.StructEntryDouble;
import shoreline_exam_2018.be.output.structure.entry.StructEntryInteger;
import shoreline_exam_2018.be.output.structure.entry.StructEntryInterface;
import shoreline_exam_2018.be.output.structure.entry.StructEntryObject;
import shoreline_exam_2018.be.output.structure.entry.StructEntryString;
import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.database.connection.DBConnectorPool;

/**
 *
 * @author Asbamz
 */
public class ProfileDAO
{
    private DBConnectorPool dbcp;

    public ProfileDAO()
    {
        dbcp = new DBConnectorPool();
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
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
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
        finally
        {
            dbcp.checkIn(con);
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
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "INSERT INTO ProfileStructureSimple VALUES(?, ?, ?, ?);";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);
            statement.setString(2, se.getColumnName());
            statement.setInt(3, se.getInputIndex());
            statement.setString(4, se.getSST().name());

            statement.executeUpdate();
        }
        finally
        {
            dbcp.checkIn(con);
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
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
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
    private int addListOfStructEntries(int id, List<StructEntryInterface> seiLst) throws SQLException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
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
        finally
        {
            dbcp.checkIn(con);
        }
    }

    public List<Profile> getAllProfiles() throws DALException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "SELECT * FROM Profile WHERE createdBy IS NOT NULL";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            List<Profile> profiles = new ArrayList<>();

            while (rs.next())
            {
                int profileId = rs.getInt("id");
                String name = rs.getString("name");
                Profile profile = new Profile(profileId, name, new StructEntryObject(name, getStructure(profileId)), String.valueOf(rs.getInt("createdBy")));

                profiles.add(profile);

            }

            return profiles;
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
     * Gets all entries which belongs to the given profile id.
     * @param profileId
     * @return
     * @throws SQLException
     */
    private List<StructEntryInterface> getStructure(int profileId) throws SQLException
    {
        List<StructEntryInterface> lst = new ArrayList<>();
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            /*
                Simple
             */

            String sql = "SELECT * FROM ProfileStructureSimple WHERE profileId = ?;";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            ResultSet rs = statement.executeQuery();

            StructEntryObject seo;

            List<StructEntryInterface> simples = new ArrayList<>();
            List<StructEntryInterface> objects = new ArrayList<>();
            List<StructEntryInterface> arrays = new ArrayList<>();

            int id;
            String columnName;
            int inputIndex;
            String sst;

            while (rs.next())
            {
                sst = rs.getString("sst");
                columnName = rs.getString("columnName");
                inputIndex = rs.getInt("inputIndex");

                if (sst.equalsIgnoreCase(SimpleStructType.DATE.name()))
                {
                    simples.add(new StructEntryDate(columnName, inputIndex));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.DOUBLE.name()))
                {
                    simples.add(new StructEntryDouble(columnName, inputIndex));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.INTEGER.name()))
                {
                    simples.add(new StructEntryInteger(columnName, inputIndex));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.STRING.name()))
                {
                    simples.add(new StructEntryString(columnName, inputIndex));
                }
            }

            /*
                Object
             */
            sql = "SELECT *, otp.profileId AS otpId FROM ProfileStructureObject pso INNER JOIN ObjectToProfile otp ON pso.id = otp.profileStructureObjectId WHERE pso.profileId = ?;";

            statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            rs = statement.executeQuery();

            while (rs.next())
            {
                id = rs.getInt("id");
                columnName = rs.getString("columnName");

                objects.add(new StructEntryObject(columnName, getStructure(rs.getInt("otpId"))));
            }

            /*
                Array
             */
            sql = "SELECT *, atp.profileId AS atpId FROM ProfileStructureArray psa INNER JOIN ArrayToProfile atp ON psa.id = atp.profileStructureArrayId WHERE psa.profileId = ?;";

            statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            rs = statement.executeQuery();

            while (rs.next())
            {
                id = rs.getInt("id");
                columnName = rs.getString("columnName");

                arrays.add(new StructEntryArray(columnName, getStructure(rs.getInt("atpId"))));
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
