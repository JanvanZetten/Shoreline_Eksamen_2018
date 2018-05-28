package shoreline_exam_2018.bll;

import java.util.Observable;
import javafx.application.Platform;
import javafx.concurrent.Task;
import shoreline_exam_2018.gui.model.AlertFactory;

/**
 * This class creates a thread that checks changes in the database every second
 * to update the log list.
 * @author alexl
 */
public class LogChangeChecker extends Observable
{

    private BLLFacade bll;
    private int lastLogID;
    private static Thread thread;
    private static boolean running = true;

    private final static long WAIT_TIME = 1000;

    public LogChangeChecker()
    {

        try
        {
            bll = BLLManager.getInstance();
            lastLogID = bll.getNewestLog();

            thread = new Thread(getTask(Thread.currentThread()));
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
        catch (BLLException ex)
        {
            AlertFactory.showError("A connection to the database was lost", ex.getMessage());
        }
    }

    /**
     * Creates a new thread for the ChangeChecker to run on.
     * @param fxThread
     * @return
     */
    private Runnable getTask(Thread fxThread)
    {
        Task task = new Task()
        {
            @Override
            protected Object call() throws Exception
            {
                while (running)
                {
                    int logId = bll.getNewestLog();
                    if (logId != lastLogID)
                    {
                        //if the last log is not the same:
                        setChanged();
                        Platform.runLater(() ->
                        {
                            notifyObservers();
                        });

                        lastLogID = logId;
                    }

                    Thread.sleep(WAIT_TIME);
                }
                return null;
            }
        };
        return task;
    }
}
