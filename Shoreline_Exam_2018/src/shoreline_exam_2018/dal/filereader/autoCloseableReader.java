/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.filereader;

import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author janvanzetten
 */
public abstract class autoCloseableReader implements Reader
{

    protected static final long EXPIRATION_TIME = 10000; //in milliseconds
    protected long timeouttime = System.currentTimeMillis() + EXPIRATION_TIME;

    /**
     * makes a thread which cheks for timeout and closes it when it expires
     *
     */
    protected void makeTimeout()
    {
        Thread thread;
        thread = new Thread(() ->
        {
            while (System.currentTimeMillis() < timeouttime)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(XLSX_horisontal_Reader_for_Big_Documents.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try
            {
                closeMainStream();
            }
            catch (DALException ex)
            {
                Logger.getLogger(XLSX_horisontal_Reader_for_Big_Documents.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    abstract void closeMainStream() throws DALException;

}
