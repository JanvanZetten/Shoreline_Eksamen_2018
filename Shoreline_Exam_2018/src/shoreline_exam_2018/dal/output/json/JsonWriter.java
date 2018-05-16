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
import shoreline_exam_2018.be.output.jsonpair.JsonPairArray;
import shoreline_exam_2018.be.output.OutputPair;
import shoreline_exam_2018.dal.output.Writer;

/**
 *
 * @author Asbamz
 */
public class JsonWriter implements Writer
{
    private Path outputPath;
    private FileWriter fw;
    private boolean isOpen;
    private boolean isFirst;
    private Thread t;
    private long lastTime;

    @Deprecated
    public JsonWriter()
    {

    }

    /**
     * Writer which writes each separate object to an output file.
     * @param outputPath
     * @throws DALException
     */
    public JsonWriter(Path outputPath) throws DALException
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
                fw.flush();
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
        // Get Path.
        Path tmp = Paths.get(outputPath.getParent().toString() + "/_tmpa_" + outputPath.getFileName());

        // Open writer stream.
        try (FileWriter file = new FileWriter(tmp.toFile()))
        {
            // Write beginning "[".
            file.write("[");

            // Open reader stream. Read all characters from original file and write to tmp.
            try (FileReader fr = new FileReader(outputPath.toFile()))
            {
                int c = fr.read();
                while (c != -1)
                {
                    file.write(c);
                    c = fr.read();
                }
            }

            // Write end "]".
            file.write("]");

            // Flush and close writer so that the tmp can be copied and deleted.
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
        // Get Path.
        Path tmp = Paths.get(outputPath.getParent().toString() + "/_tmpr_" + outputPath.getFileName());

        // Open write stream.
        try (FileWriter file = new FileWriter(tmp.toFile()))
        {
            // Open read stream.
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
                // If not end "]"
                if (buf != 93)
                {
                    file.write(buf);
                }
            }

            // Flush and close writer so that the tmp can be copied and deleted.
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
     * Runnable to countdown to close file.
     * @param dao for object reference.
     * @param time until closing stream.
     * @return
     */
    private Runnable closeTimer(JsonWriter dao, long time)
    {
        Runnable r = new Runnable()
        {
            private long total = time;

            @Override
            public void run()
            {
                dao.setLastTime(System.currentTimeMillis());
                try
                {
                    while (total > System.currentTimeMillis() - dao.getLastTime())
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

                        Logger.getLogger(JsonWriter.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        };
        return r;
    }

    /**
     * Get lastTime
     * @return
     */
    private long getLastTime()
    {
        return lastTime;
    }

    /**
     * Set lastTime.
     */
    private void setLastTime(Long lastTime)
    {
        this.lastTime = lastTime;
    }

    @Override
    @Deprecated
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
            // Open stream if it is not open.
            if (!isOpen)
            {
                openStream();
            }
            // Else extend open time.
            else
            {
                setLastTime(System.currentTimeMillis());
            }

            // Write object to file. Only add comma if not first.
            fw.write(isFirst ? entity.toString() : ", " + entity.toString());
            fw.flush();

            // Set first to false if first.
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
