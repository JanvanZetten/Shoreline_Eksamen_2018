/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.be.runnable;

import java.nio.file.Path;

/**
 *
 * @author Asbamz
 */
public abstract class RunnableWithPath implements Runnable
{
    protected Path path;

    public void setPath(Path path)
    {
        this.path = path;
    }
}
