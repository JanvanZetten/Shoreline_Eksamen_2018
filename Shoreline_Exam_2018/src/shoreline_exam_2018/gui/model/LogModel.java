/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import shoreline_exam_2018.be.Log;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;

/**
 *
 * @author janvanzetten
 */
public class LogModel {

    ObservableList<Log> logList;
    BLLFacade bll = BLLManager.getInstance();

    public LogModel() {
        logList = FXCollections.observableArrayList();
    }

    /**
     * load/reload the logItems and put it in the observable list 
     */
    public void loadLogItems() {
        logList.clear();
        try {
            logList.addAll(bll.getAllLogs());
        } catch (BLLException ex) {
            AlertFactory.showError("Could not load Log items", ex.getMessage());
        }
    }

    
    /**
     * get observable list with log items
     * @return 
     */
    public ObservableList<Log> getLogItems() {
        return logList;
    }

    
    /**
     * sets the listitem text for logitems 
     * @param listviewLog for which to set the text
     */
    public void setListItemString(ListView<Log> listviewLog) {
        listviewLog.setCellFactory(param -> new ListCell<Log>() {
            @Override
            protected void updateItem(Log item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(getName(item));
                }
            }

            private String getName(Log item) {
                return item.getMessage() + " : " + item.getDate().toString();
            }
        });
    }

}
