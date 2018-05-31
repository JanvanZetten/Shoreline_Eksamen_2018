package shoreline_exam_2018.dal.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.rule.DateFormatRule;
import shoreline_exam_2018.be.output.rule.DefaultDateRule;
import shoreline_exam_2018.be.output.rule.DefaultDoubleRule;
import shoreline_exam_2018.be.output.rule.DefaultIntegerRule;
import shoreline_exam_2018.be.output.rule.DefaultStringRule;
import shoreline_exam_2018.be.output.rule.Rule;
import shoreline_exam_2018.be.output.structure.CollectionEntity;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.be.output.structure.entity.StructEntityArray;
import shoreline_exam_2018.be.output.structure.entity.StructEntityDate;
import shoreline_exam_2018.be.output.structure.entity.StructEntityDouble;
import shoreline_exam_2018.be.output.structure.entity.StructEntityInteger;
import shoreline_exam_2018.be.output.structure.entity.StructEntityObject;
import shoreline_exam_2018.be.output.structure.entity.StructEntityString;
import shoreline_exam_2018.be.output.structure.type.CollectionStructType;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.database.connection.DBConnectorPool;
import shoreline_exam_2018.be.output.structure.StructEntity;

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
    public Profile addProfile(String name, StructEntityObject structure, int createdBy, HashMap<String, Map.Entry<Integer, String>> headersIndexAndExamples) throws DALException
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

            for (StructEntity structEntryInterface : structure.getCollection())
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
            addProfileHeaders(id, headersIndexAndExamples);
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
            String sql = "INSERT INTO ProfileStructureSimple VALUES(?, ?, ?, ?, ?);";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, profileId);
            statement.setString(2, se.getColumnName());
            statement.setInt(3, se.getInputIndex());
            statement.setString(4, se.getSST().name());
            if (se.getBackupIndex() != null)
            {
                statement.setInt(5, se.getBackupIndex());
            }
            else
            {
                statement.setNull(5, Types.INTEGER);
            }

            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int id = rs.getInt(1);

            // Insert allDefaultRules.
            Rule rule = se.getDefaultValue();
            if (rule != null)
            {
                if (rule instanceof DefaultIntegerRule)
                {
                    DefaultIntegerRule dir = (DefaultIntegerRule) rule;
                    con = dbcp.checkOut();
                    sql = "INSERT INTO RuleDefaultInteger VALUES(?, ?, ?, ?);";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, profileId);
                    statement.setInt(2, id);
                    Integer defaultInt = dir.applyRuleOn(null);
                    statement.setInt(3, defaultInt == null ? 0 : defaultInt);
                    statement.setBoolean(4, dir.isForced());

                    statement.executeUpdate();
                }
                else if (rule instanceof DefaultDoubleRule)
                {
                    DefaultDoubleRule ddr = (DefaultDoubleRule) rule;
                    con = dbcp.checkOut();
                    sql = "INSERT INTO RuleDefaultDouble VALUES(?, ?, ?, ?);";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, profileId);
                    statement.setInt(2, id);
                    Double defaultDbl = ddr.applyRuleOn(null);
                    statement.setDouble(3, defaultDbl == null ? 0.0 : defaultDbl);
                    statement.setBoolean(4, ddr.isForced());

                    statement.executeUpdate();
                }
                else if (rule instanceof DefaultStringRule)
                {
                    DefaultStringRule dsr = (DefaultStringRule) rule;
                    con = dbcp.checkOut();
                    sql = "INSERT INTO RuleDefaultString VALUES(?, ?, ?, ?);";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, profileId);
                    statement.setInt(2, id);
                    String defaultStr = dsr.applyRuleOn(null);
                    statement.setString(3, defaultStr == null ? "" : defaultStr);
                    statement.setBoolean(4, dsr.isForced());

                    statement.executeUpdate();
                }
                else if (rule instanceof DefaultDateRule)
                {
                    DefaultDateRule ddr = (DefaultDateRule) rule;
                    con = dbcp.checkOut();
                    sql = "INSERT INTO RuleDefaultDate VALUES(?, ?, ?, ?);";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, profileId);
                    statement.setInt(2, id);
                    Date defaultDat = ddr.applyRuleOn(null);
                    statement.setTimestamp(3, new java.sql.Timestamp(defaultDat == null ? 0 : defaultDat.getTime()));
                    statement.setBoolean(4, ddr.isForced());

                    statement.executeUpdate();
                }
            }

            if (se instanceof StructEntityDate)
            {
                DateFormatRule dfr = ((StructEntityDate) se).getDfr();
                if (dfr != null)
                {
                    con = dbcp.checkOut();
                    sql = "INSERT INTO RuleDateFormat VALUES(?, ?, ?);";

                    statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, profileId);
                    statement.setInt(2, id);
                    statement.setString(3, dfr.getDateFormat());

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
    private int addListOfStructEntries(int id, List<StructEntity> seiLst) throws SQLException
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

            for (StructEntity structEntryInterface : seiLst)
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

    private void addProfileHeaders(int profileId, HashMap<String, Map.Entry<Integer, String>> headersIndexAndExamples) throws SQLException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();

            for (String string : headersIndexAndExamples.keySet())
            {
                String sql = "INSERT INTO ProfileHeaders VALUES(?, ?, ?, ?);";

                PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, profileId);
                statement.setString(2, string);
                statement.setString(3, headersIndexAndExamples.get(string).getValue());
                statement.setInt(4, headersIndexAndExamples.get(string).getKey());

                statement.executeUpdate();
            }
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
                Profile profile = new Profile(profileId, name, new StructEntityObject(profileId, name, getStructure(profileId)), String.valueOf(rs.getInt("createdBy")));

                profile.setHeadersIndexAndExamples(getProfileHeaders(profileId));
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
    private List<StructEntity> getStructure(int profileId) throws SQLException, DALException
    {
        List<StructEntity> lst = new ArrayList<>();
        Connection con = null;
        HashMap<Integer, Rule> allDefaultRules = getAllDefaultRules(profileId);
        HashMap<Integer, DateFormatRule> allDateFormatRules = getAllDateFormatRules(profileId);

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

            List<StructEntity> simples = new ArrayList<>();
            List<StructEntity> objects = new ArrayList<>();
            List<StructEntity> arrays = new ArrayList<>();

            int id;
            String columnName;
            int inputIndex;
            String sst;
            Integer backupIndex;

            while (rs.next())
            {
                id = rs.getInt("id");
                columnName = rs.getString("columnName");
                inputIndex = rs.getInt("inputIndex");
                sst = rs.getString("sst");
                backupIndex = rs.getInt("backupIndex");
                if (rs.wasNull())
                {
                    backupIndex = null;
                }

                SimpleEntity se = null;
                if (sst.equalsIgnoreCase(SimpleStructType.DATE.name()))
                {
                    if (allDateFormatRules.containsKey(id))
                    {
                        simples.add(se = new StructEntityDate(id, columnName, inputIndex, allDateFormatRules.get(id)));
                    }
                    else
                    {
                        simples.add(se = new StructEntityDate(id, columnName, inputIndex));
                    }
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.DOUBLE.name()))
                {
                    simples.add(se = new StructEntityDouble(id, columnName, inputIndex));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.INTEGER.name()))
                {
                    simples.add(se = new StructEntityInteger(id, columnName, inputIndex));
                }
                else if (sst.equalsIgnoreCase(SimpleStructType.STRING.name()))
                {
                    simples.add(se = new StructEntityString(id, columnName, inputIndex));
                }
                if (allDefaultRules.containsKey(id) && se != null)
                {
                    se.setDefaultValue(allDefaultRules.get(id));
                }
                se.setBackupIndex(backupIndex);
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

                objects.add(new StructEntityObject(id, columnName, getStructure(rs.getInt("otpId"))));
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

                arrays.add(new StructEntityArray(id, columnName, getStructure(rs.getInt("atpId"))));
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
     * Get all allDefaultRules for profile.
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
                boolean isForced = rs.getBoolean("isForced");
                DefaultIntegerRule rule = new DefaultIntegerRule(defaultValue, inputIndex, isForced);

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
                boolean isForced = rs.getBoolean("isForced");
                DefaultDoubleRule rule = new DefaultDoubleRule(defaultValue, inputIndex, isForced);

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
                boolean isForced = rs.getBoolean("isForced");
                DefaultStringRule rule = new DefaultStringRule(defaultValue, inputIndex, isForced);

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
                boolean isForced = rs.getBoolean("isForced");
                DefaultDateRule rule = new DefaultDateRule(defaultValue, inputIndex, isForced);

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

    /**
     * Get all Date Format Rules.
     * @param profileId
     * @return
     * @throws DALException
     */
    private HashMap<Integer, DateFormatRule> getAllDateFormatRules(int profileId) throws DALException
    {
        HashMap<Integer, DateFormatRule> inputRuleMap = new HashMap<>();
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "SELECT * FROM RuleDateFormat WHERE profileId = ?";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                int inputIndex = rs.getInt("inputIndex");
                String dateFormat = rs.getString("dateFormat");
                DateFormatRule rule = new DateFormatRule(dateFormat, inputIndex);

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

    /**
     * Get Headers used to create profile.
     * @param profileId
     * @return
     * @throws DALException
     */
    private HashMap<String, Map.Entry<Integer, String>> getProfileHeaders(int profileId) throws DALException
    {
        HashMap<String, Map.Entry<Integer, String>> headersIndexAndExamples = new HashMap<>();
        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "SELECT * FROM ProfileHeaders WHERE profileId = ?";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            ResultSet rs = statement.executeQuery();

            String name;
            String example;
            int inputIndex;
            while (rs.next())
            {
                name = rs.getString("name");
                example = rs.getString("example");
                inputIndex = rs.getInt("inputIndex");

                headersIndexAndExamples.put(name, new SimpleEntry<>(inputIndex, example));
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

        return headersIndexAndExamples;
    }

    /**
     * Deletes Profile with given Id.
     * @param profileId
     * @throws DALException
     */
    public void deleteProfile(int profileId) throws DALException
    {
        deleteChildren(profileId);

        Connection con = null;

        try
        {
            con = dbcp.checkOut();

            String sql = "DELETE FROM Profile WHERE id = ?;";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            statement.executeUpdate();
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
     * Delete everything beneath the Profile. So that the profile is empty.
     * @param profileId
     * @throws DALException
     */
    private void deleteChildren(int profileId) throws DALException
    {
        Connection con = null;

        try
        {
            con = dbcp.checkOut();

            /*
                Remove Array Profiles.
             */
            String sql = "SELECT *, atp.profileId AS newId FROM ProfileStructureArray psa INNER JOIN ArrayToProfile atp ON psa.id = atp.profileStructureArrayId WHERE psa.profileId = ?";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                int id = rs.getInt("newId");
                deleteProfile(id);
            }

            /*
                Remove Object Profiles.
             */
            sql = "SELECT *, otp.profileId AS newId FROM ProfileStructureObject pso INNER JOIN ObjectToProfile otp ON pso.id = otp.profileStructureObjectId WHERE pso.profileId = ?";

            statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);

            rs = statement.executeQuery();

            while (rs.next())
            {
                int id = rs.getInt("newId");
                deleteProfile(id);
            }


            /*
                Remove everything with this profileId.
             */
            sql = "DELETE FROM RuleDefaultInteger WHERE profileId = ?;"
                    + "DELETE FROM RuleDefaultString WHERE profileId = ?;"
                    + "DELETE FROM RuleDefaultDouble WHERE profileId = ?;"
                    + "DELETE FROM RuleDefaultDate WHERE profileId = ?;"
                    + "DELETE FROM RuleDateFormat WHERE profileId = ?;"
                    + "DELETE FROM ArrayToProfile WHERE profileId = ?;"
                    + "DELETE FROM ObjectToProfile WHERE profileId = ?;"
                    + "DELETE FROM ProfileStructureArray WHERE profileId = ?;"
                    + "DELETE FROM ProfileStructureObject WHERE profileId = ?;"
                    + "DELETE FROM ProfileStructureSimple WHERE profileId = ?;"
                    + "DELETE FROM ProfileHeaders WHERE profileId = ?;";

            statement = con.prepareStatement(sql);
            statement.setInt(1, profileId);
            statement.setInt(2, profileId);
            statement.setInt(3, profileId);
            statement.setInt(4, profileId);
            statement.setInt(5, profileId);
            statement.setInt(6, profileId);
            statement.setInt(7, profileId);
            statement.setInt(8, profileId);
            statement.setInt(9, profileId);
            statement.setInt(10, profileId);
            statement.setInt(11, profileId);

            statement.executeUpdate();
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
     * Update Profile.
     * @param profile
     * @param createdBy
     * @throws DALException
     */
    public Profile updateProfile(Profile profile, int createdBy) throws DALException
    {
        deleteChildren(profile.getId());

        Connection con = null;

        try
        {
            con = dbcp.checkOut();
            String sql = "UPDATE Profile SET name=?, createdBy=? WHERE id = ?;";

            PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, profile.getName());
            statement.setInt(2, createdBy);
            statement.setInt(3, profile.getId());

            statement.executeUpdate();

            for (StructEntity structEntryInterface : profile.getStructure().getCollection())
            {
                if (structEntryInterface instanceof CollectionEntity)
                {
                    CollectionEntity ce = (CollectionEntity) structEntryInterface;
                    addProfileStructureCollection(profile.getId(), ce);
                }
                else if (structEntryInterface instanceof SimpleEntity)
                {
                    SimpleEntity se = (SimpleEntity) structEntryInterface;
                    addProfileStructureSimple(profile.getId(), se);
                }
            }
            addProfileHeaders(profile.getId(), profile.getHeadersIndexAndExamples());
        }
        catch (SQLException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        finally
        {
            dbcp.checkIn(con);
        }

        // Make new references.
        int index = profile.getId();
        String str = profile.getName();
        List<StructEntity> entities = new ArrayList<>();
        entities.addAll(profile.getStructure().getCollection());
        StructEntityObject structure = new StructEntityObject(index, str, entities);
        Profile newProfile = new Profile(index, str, structure, String.valueOf(createdBy));
        HashMap<String, Entry<Integer, String>> headerMap = new HashMap<>();
        headerMap.putAll(profile.getHeadersIndexAndExamples());
        newProfile.setHeadersIndexAndExamples(headerMap);

        return newProfile;
    }
}
