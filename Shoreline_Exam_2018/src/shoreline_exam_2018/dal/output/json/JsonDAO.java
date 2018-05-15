/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.output.json;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_exam_2018.dal.DALException;
import shoreline_exam_2018.dal.output.OutputDAO;
import shoreline_exam_2018.be.output.jsonpair.JsonPairArray;
import shoreline_exam_2018.be.output.OutputPair;

/**
 *
 * @author Asbamz
 */
public class JsonDAO implements OutputDAO
{
    private Path outputPath;
    private FileWriter fw;
    private boolean isOpen;
    private boolean isFirst;
    private Thread t;
    private long lastTime;

    @Deprecated
    public JsonDAO()
    {

    }

    public JsonDAO(Path outputPath) throws DALException
    {
        this.outputPath = outputPath;
        this.isOpen = false;
        this.isFirst = true;
        try
        {
            Files.deleteIfExists(outputPath);
            openStream();
        }
        catch (IOException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Open stream for file writing.
     * @throws IOException
     */
    private void openStream() throws DALException
    {
        if (!isFirst)
        {
            removeSquareBrackets();
        }
        try
        {
            fw = new FileWriter(outputPath.toFile(), true);
        }
        catch (IOException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
        isOpen = true;
        t = new Thread(closeTimer(this, 10000));
        t.start();
    }

    /**
     * Close stream for file writing.
     * @throws DALException
     */
    @Override
    public void closeStream() throws DALException
    {
        if (isOpen && fw != null)
        {
            try
            {
                fw.close();
                addSquareBrackets();
                isOpen = false;
            }
            catch (IOException ex)
            {
                throw new DALException(ex.getMessage(), ex.getCause());
            }
        }
    }

    /**
     * Adds a square bracket in beginning and end of file so that the object are
     * packed in a array.
     * @throws DALException
     */
    private void addSquareBrackets() throws DALException
    {
        Path tmp = Paths.get(outputPath.getParent().toString() + "/_tmp_" + outputPath.getFileName());
        try (FileWriter file = new FileWriter(tmp.toFile()))
        {
            file.write("[");
            try (FileReader fr = new FileReader(outputPath.toFile()))
            {
                int c = fr.read();
                while (c != -1)
                {
                    file.write(c);
                    c = fr.read();
                }
            }
            file.write("]");
            file.flush();
            file.close();
            Files.copy(tmp, outputPath, StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(tmp);
        }
        catch (IOException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * Adds a square bracket in beginning and end of file so that the object are
     * packed in a array.
     * @throws DALException
     */
    private void removeSquareBrackets() throws DALException
    {
        Path tmp = Paths.get(outputPath.getParent().toString() + "/_tmp_" + outputPath.getFileName());
        try (FileWriter file = new FileWriter(tmp.toFile()))
        {
            try (FileReader fr = new FileReader(outputPath.toFile()))
            {
                boolean isFirstChar = true;
                int buf = -2;
                int c = fr.read();
                while (c != -1)
                {
                    // If beginning "["
                    if (isFirstChar && c == 91)
                    {
                        c = fr.read();
                        continue;
                    }
                    if (buf != -2)
                    {
                        file.write(buf);
                    }
                    buf = c;
                    c = fr.read();
                }
                if (buf != 93)
                {
                    file.write(buf);
                }
            }
            file.flush();
            file.close();
            Files.copy(tmp, outputPath, StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(tmp);
        }
        catch (IOException ex)
        {
            throw new DALException(ex.getMessage(), ex.getCause());
        }
    }

    private Runnable closeTimer(JsonDAO dao, long time)
    {
        Runnable r = new Runnable()
        {
            private long total = time;

            @Override
            public void run()
            {
                setLastTime(System.currentTimeMillis());
                try
                {
                    while (total > System.currentTimeMillis() - getLastTime())
                    {
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException ex)
                        {
                            dao.closeStream();
                        }
                        if (!isOpen)
                        {
                            return;
                        }
                    }
                    dao.closeStream();
                }
                catch (DALException ex)
                {
                    try
                    {
                        dao.closeStream();
                    }
                    catch (DALException ex1)
                    {
                        //
                        // Call an exception handler
                        //

                        Logger.getLogger(JsonDAO.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        };
        return r;
    }

    private long getLastTime()
    {
        return lastTime;
    }

    private void setLastTime(Long lastTime)
    {
        this.lastTime = lastTime;
    }

    @Override
    public void createFile(List<OutputPair> entities, Path outputPath) throws DALException
    {
        JsonPairArray jsonArr = new JsonPairArray("jsonArray", entities);

        try (FileWriter file = new FileWriter(outputPath.toFile()))
        {
            file.write(jsonArr.getValue().toJSONString());
            file.flush();
        }
        catch (IOException e)
        {
            throw new DALException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void writeObjectToFile(OutputPair entity) throws DALException
    {
        try
        {
            if (!isOpen)
            {
                openStream();
            }
            else
            {
                setLastTime(System.currentTimeMillis());
            }
            fw.write(isFirst ? entity.toString() : ", " + entity.toString());
            fw.flush();

            if (isFirst)
            {
                isFirst = false;
            }
        }
        catch (IOException e)
        {
            throw new DALException(e.getMessage(), e.getCause());
        }
    }
}
