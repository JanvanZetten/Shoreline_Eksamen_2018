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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.rule.DefaultDateRule;
import shoreline_exam_2018.be.output.rule.DefaultDoubleRule;
import shoreline_exam_2018.be.output.rule.DefaultIntegerRule;
import shoreline_exam_2018.be.output.rule.DefaultStringRule;
import shoreline_exam_2018.be.output.rule.Rule;
import shoreline_exam_2018.be.output.structure.CollectionEntity;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
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
import shoreline_exam_2018.be.output.structure.StructEntityInterface;

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
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws DALException
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

        profile = new Profile(id, name, structure, String.valueOf(createdBy));
        return profile;
    }

    /**
     * Adds simple structure.
     * @param profileId
     * @param se
     * @throws SQLException
     */
    private void addProfileStructureSimple(int profileId, SimpleEntity se) throws SQLException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "INSERT INTO ProfileStructureSimple VALUES(?, ?, ?, ?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, profileId);
            statement.setString(2, se.getColumnName());
            statement.setInt(3, se.getInputIndex());
            statement.setString(4, se.getSST().name());

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);

            // Insert rules.
            Rule rule = se.getDefaultValue();
            if (rule != null)
            {
                if (rule instanceof DefaultIntegerRule)
                {
                    DefaultIntegerRule dir = (DefaultIntegerRule) rule;
                    con = dbcp.checkOut();
                    sql = "INSERT INTO RuleDefaultInteger VALUES(?, ?, ?);";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, profileId);
                    statement.setInt(2, id);
                    Integer defaultInt = dir.applyRuleOn(null);
                    statement.setInt(3, defaultInt == null ? 0 : defaultInt);

                    statement.executeUpdate();
                }
                else if (rule instanceof DefaultDoubleRule)
                {
                    DefaultDoubleRule ddr = (DefaultDoubleRule) rule;
                    con = dbcp.checkOut();
                    sql = "INSERT INTO RuleDefaultDouble VALUES(?, ?, ?);";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, profileId);
                    statement.setInt(2, id);
                    Double defaultDbl = ddr.applyRuleOn(null);
                    statement.setDouble(3, defaultDbl == null ? 0.0 : defaultDbl);

                    statement.executeUpdate();
                }
                else if (rule instanceof DefaultStringRule)
                {
                    DefaultStringRule dsr = (DefaultStringRule) rule;
                    con = dbcp.checkOut();
                    sql = "INSERT INTO RuleDefaultString VALUES(?, ?, ?);";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, profileId);
                    statement.setInt(2, id);
                    String defaultStr = dsr.applyRuleOn(null);
                    statement.setString(3, defaultStr == null ? "" : defaultStr);

                    statement.executeUpdate();
                }
                else if (rule instanceof DefaultDateRule)
                {
                    DefaultDateRule ddr = (DefaultDateRule) rule;
                    con = dbcp.checkOut();
                    sql = "INSERT INTO RuleDefaultDate VALUES(?, ?, ?);";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, profileId);
                    statement.setInt(2, id);
                    Date defaultDat = ddr.applyRuleOn(null);
                    statement.setTimestamp(3, new java.sql.Timestamp(defaultDat == null ? 0 : defaultDat.getTime()));

                    statement.executeUpdate();
                }
            }
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
    private void addProfileStructureCollection(int profileId, CollectionEntity ce) throws SQLException
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
    private int addListOfStructEntries(int id, List<StructEntityInterface> seiLst) throws SQLException
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
     * Get All Profiles.
     * @return
     * @throws DALException
     */
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
                Profile profile = new Profile(profileId, name, new StructEntityObject(name, getStructure(profileId)), String.valueOf(rs.getInt("createdBy")));

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
    private List<StructEntityInterface> getStructure(int profileId) throws SQLException, DALException
    {
        List<StructEntityInterface> lst = new ArrayList<>();
        Connection con = null;
        HashMap<Integer, Rule> rules = getAllDefaultRules(profileId);

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

            StructEntityObject seo;

            List<StructEntityInterface> simples = new ArrayList<>();
            List<StructEntityInterface> objects = new ArrayList<>();
            List<StructEntityInterface> arrays = new ArrayList<>();

            int id;
            String columnName;
            int inputIndex;
            String sst;

            while (rs.next())
            {
                id = rs.getInt("id");
                sst = rs.getString("sst");
                columnName = rs.getString("columnName");
                inputIndex = rs.getInt("inputIndex");

                SimpleEntity se = null;
                if (sst.equalsIgnoreCase(SimpleStructType.DATE.name()))
                {
                    simples.add(se = new StructEntityDate(columnName, inputIndex));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.DOUBLE.name()))
                {
                    simples.add(se = new StructEntityDouble(columnName, inputIndex));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.INTEGER.name()))
                {
                    simples.add(se = new StructEntityInteger(columnName, inputIndex));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.STRING.name()))
                {
                    simples.add(se = new StructEntityString(columnName, inputIndex));
                }
                if (rules.containsKey(id) && se != null)
                {
                    se.setDefaultValue(rules.get(id));
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

                objects.add(new StructEntityObject(columnName, getStructure(rs.getInt("otpId"))));
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

                arrays.add(new StructEntityArray(columnName, getStructure(rs.getInt("atpId"))));
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

    /**
     * Get all rules for profile.
     * @param profileId
     * @return
     * @throws DALException
     */
    private HashMap<Integer, Rule> getAllDefaultRules(int profileId) throws DALException
    {
        HashMap<Integer, Rule> inputRuleMap = new HashMap<>();
        Connection con = null;

        try
        {
            con = dbcp.checkOut();

            //Default Integer
            String sql = "SELECT * FROM RuleDefaultInteger WHERE profileId = ?";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                int inputIndex = rs.getInt("inputIndex");
                int defaultValue = rs.getInt("defaultValue");
                DefaultIntegerRule rule = new DefaultIntegerRule(defaultValue, inputIndex);

                if (inputRuleMap.containsKey(inputIndex))
                {
                    inputRuleMap.replace(inputIndex, rule);
                }
                else
                {
                    inputRuleMap.put(inputIndex, rule);
                }
            }

            //Default Double
            sql = "SELECT * FROM RuleDefaultDouble WHERE profileId = ?";

            statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            rs = statement.executeQuery();

            while (rs.next())
            {
                int inputIndex = rs.getInt("inputIndex");
                Double defaultValue = rs.getDouble("defaultValue");
                DefaultDoubleRule rule = new DefaultDoubleRule(defaultValue, inputIndex);

                if (inputRuleMap.containsKey(inputIndex))
                {
                    inputRuleMap.replace(inputIndex, rule);
                }
                else
                {
                    inputRuleMap.put(inputIndex, rule);
                }
            }

            //Default String
            sql = "SELECT * FROM RuleDefaultString WHERE profileId = ?";

            statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            rs = statement.executeQuery();

            while (rs.next())
            {
                int inputIndex = rs.getInt("inputIndex");
                String defaultValue = rs.getString("defaultValue");
                DefaultStringRule rule = new DefaultStringRule(defaultValue, inputIndex);

                if (inputRuleMap.containsKey(inputIndex))
                {
                    inputRuleMap.replace(inputIndex, rule);
                }
                else
                {
                    inputRuleMap.put(inputIndex, rule);
                }
            }

            //Default Date
            sql = "SELECT * FROM RuleDefaultDate WHERE profileId = ?";

            statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            rs = statement.executeQuery();

            while (rs.next())
            {
                int inputIndex = rs.getInt("inputIndex");
                Date defaultValue = rs.getDate("defaultValue");
                DefaultDateRule rule = new DefaultDateRule(defaultValue, inputIndex);

                if (inputRuleMap.containsKey(inputIndex))
                {
                    inputRuleMap.replace(inputIndex, rule);
                }
                else
                {
                    inputRuleMap.put(inputIndex, rule);
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

        return inputRuleMap;
    }
}
