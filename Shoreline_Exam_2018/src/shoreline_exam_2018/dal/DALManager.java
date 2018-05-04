/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import shoreline_exam_2018.dal.filereader.XLSX_horisontal_Reader;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import shoreline_exam_2018.be.Profile;
import shoreline_exam_2018.be.output.structure.entry.StructEntityObject;
import shoreline_exam_2018.dal.database.ProfileDAO;

/**
 *
 * @author alexl
 */
public class DALManager implements DALFacade
{

    private ProfileDAO profileDAO;
    private XLSX_horisontal_Reader xhr;

    public DALManager()
    {
        this.profileDAO = new ProfileDAO();
    }

    @Override
    public Profile addProfile(String name, StructEntityObject structure, int createdBy) throws DALException
    {
        return profileDAO.addProfile(name, structure, createdBy);
    }

    @Override
    public List<Profile> getAllProfiles() throws DALException
    {
        return profileDAO.getAllProfiles();
    }

    @Override
    public HashMap<String, Entry<Integer, String>> getHeadersAndExamplesFromFile(Path path) throws DALException
    {
        xhr = new XLSX_horisontal_Reader(path.toString());
        HashMap<String, Entry<Integer, String>> hae = new HashMap();
        List<String> headers = xhr.getParameters();
        Row row = null;
        if (xhr.hasNext())
        {
            row = xhr.getNextRow();
        }

        for (int i = 0; i < headers.size(); i++)
        {
            String str = "";
            if (row != null)
            {
                Cell currentCell = row.getCell(i);
                if (currentCell != null)
                {
                    switch (currentCell.getCellTypeEnum())
                    {
                        case STRING:
                            str = currentCell.getStringCellValue();
                            break;
                        case BOOLEAN:
                            str = Boolean.toString(currentCell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            str = currentCell.getCellFormula();
                            break;
                        case NUMERIC:
                            str = Double.toString(currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                }
                hae.put(headers.get(i), new SimpleImmutableEntry<>(i, str));
            }
            else
            {
                hae.put(headers.get(i), new SimpleImmutableEntry<>(i, str));
            }
        }
        return hae;
    }

}
