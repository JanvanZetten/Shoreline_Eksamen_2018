/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.directorylistener;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import shoreline_exam_2018.be.runnable.RunnableWithPath;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author Asbamz
 */
public class DirectoryListenerTest
{

    public DirectoryListenerTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    /**
     * Test of listen method, of class DirectoryListener.
     */
    @Test
    public void testListen()
    {
        RunnableWithPath onCreatedFile = new RunnableWithPath()
        {
            @Override
            public void run()
            {
                if (path != null)
                {
                    System.out.println(path + " lol");
                }
                else
                {
                    System.out.println("WAT");
                }
            }
        };

        RunnableWithPath onError = new RunnableWithPath()
        {
            @Override
            public void run()
            {
                if (path != null)
                {
                    System.out.println(path + " error");
                }
                else
                {
                    System.out.println("WAT error");
                }
            }
        };

        RunnableWithPath onClosed = new RunnableWithPath()
        {
            @Override
            public void run()
            {
                if (path != null)
                {
                    System.out.println(path + " closed");
                }
                else
                {
                    System.out.println("WAT closed");
                }
            }
        };

        DirectoryListener dl;
        try
        {
            dl = new DirectoryListener(Paths.get(System.getProperty("user.dir")), onCreatedFile, onError, onClosed);

            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(DirectoryListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }

            dl.closeListener();
        }
        catch (DALException ex)
        {
            Logger.getLogger(DirectoryListenerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
