/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal.directorylistener;

import java.nio.file.Path;
import java.util.HashMap;
import shoreline_exam_2018.be.runnable.RunnableWithPath;
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author Asbamz
 */
public class DirectoryListenerManager
{
    HashMap<Integer, DirectoryListener> dlMap;
    int index;

    public DirectoryListenerManager()
    {
        dlMap = new HashMap<>();
        index = 0;
    }

    /**
     * Creates DirectoryListener and returns the index of the listener.
     * @param path
     * @param onFileCreated
     * @param onError
     * @param onClosed
     * @return
     */
    public Integer GetDirectoryListener(Path path, RunnableWithPath onFileCreated, RunnableWithPath onError, RunnableWithPath onClosed) throws DALException
    {
        DirectoryListener dl = new DirectoryListener(path, onFileCreated, onError, onClosed);
        dlMap.put(index, dl);
        index++;
        return index;
    }

    /**
     * Close listener with given index.
     * @param i
     */
    public void closeListener(Integer i)
    {
        if (dlMap.containsKey(i))
        {
            dlMap.get(i).closeListener();
            dlMap.remove(i);
        }
    }
}
