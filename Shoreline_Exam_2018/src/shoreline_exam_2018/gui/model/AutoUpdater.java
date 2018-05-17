/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.util.Observable;
import java.util.Observer;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.ChangeChecker;

/**
 *
 * @author alexl
 */
public class AutoUpdater implements Observer {
    
    LogModel model;
    BLLFacade bll;

    public AutoUpdater(LogModel model) {
        this.model = model;
        bll = BLLManager.getInstance();
    }
    
    /**
     * Sets this object to be the observer for changes in the database
     * regarding the LogView and it's list of logs.
     */
    public void setAsObserver(){
        bll.createChangeListener(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        model.loadLogItems();
    }
    
}
