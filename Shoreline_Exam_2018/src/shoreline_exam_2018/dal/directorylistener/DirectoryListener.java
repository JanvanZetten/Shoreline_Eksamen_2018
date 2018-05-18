/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.directorylistener;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import shoreline_exam_2018.be.runnable.RunnableWithPath;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author Asbamz
 */
public class DirectoryListener
{
    private final Path PATH;
    private RunnableWithPath onFileCreated;
    private RunnableWithPath onError;
    private RunnableWithPath onClosed;
    private boolean isClosed;

    public DirectoryListener(Path path, RunnableWithPath onFileCreated, RunnableWithPath onError, RunnableWithPath onClosed) throws DALException
    {
        this.PATH = path;
        this.onFileCreated = onFileCreated;
        this.onError = onError;
        this.onClosed = onClosed;
        listen();
    }

    private void listen() throws DALException
    {
        // Checks if folder.
        try
        {
            Boolean isFolder = (Boolean) Files.getAttribute(PATH, "basic:isDirectory", LinkOption.NOFOLLOW_LINKS);
            if (!isFolder)
            {
                throw new DALException("Path: " + PATH + " is not a folder");
            }
        }
        catch (IOException ex)
        {
            throw new DALException("Folder does not exist", ex.getCause());
        }

        FileSystem fs = PATH.getFileSystem();

        Thread thread = new Thread(() ->
        {
            try (WatchService service = fs.newWatchService())
            {
                isClosed = false;
                PATH.register(service, ENTRY_CREATE);

                WatchKey key = null;
                while (true)
                {
                    key = service.take();

                    Kind<?> kind = null;
                    for (WatchEvent<?> watchEvent : key.pollEvents())
                    {
                        kind = watchEvent.kind();
                        if (StandardWatchEventKinds.OVERFLOW == kind)
                        {
                            continue;
                        }
                        else if (ENTRY_CREATE == kind)
                        {
                            // Get new Path.
                            Path newPath = ((WatchEvent<Path>) watchEvent).context();

                            // Update Runnables with Path.
                            onFileCreated.setPath(newPath);
                            onError.setPath(newPath);
                            onClosed.setPath(newPath);

                            // Run on file created.
                            onFileCreated.run();
                        }
                    }

                    if (!key.reset() || isClosed)
                    {
                        break; // loop
                    }
                }
            }
            catch (IOException | InterruptedException ex)
            {
                throw new RuntimeException(ex.getMessage(), ex.getCause());
            }
        });

        // Run on error.
        thread.setUncaughtExceptionHandler((t, e) ->
        {
            onError.run();
        });

        thread.start();
    }

    /**
     * Close listener.
     */
    public void closeListener()
    {
        isClosed = true;
        onClosed.run();
    }
}
