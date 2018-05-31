/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile;

import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import shoreline_exam_2018.be.output.structure.StructEntity;

/**
 *
 * @author Asbamz
 */
public class DragAndDropHandler
{
    private final ProfileSpecification specification;

    /**
     * Handles Drag and Drop Events.
     * @param specification
     */
    public DragAndDropHandler(ProfileSpecification specification)
    {
        this.specification = specification;
    }

    /**
     * On Drag from a header the text is copied.
     * @param header
     * @return
     */
    EventHandler<? super MouseEvent> getOnHeaderDragDetected(TextField header)
    {
        EventHandler<? super MouseEvent> onHeaderDragDetected = new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (specification.getHeadersIndexAndExamples() != null)
                {
                    if (specification.getHeadersIndexAndExamples().containsKey(header.getText()))
                    {
                        Dragboard db = header.startDragAndDrop(TransferMode.COPY);
                        db.setDragView(getDragAndDropScene(new TextField(header.getText())).snapshot(null), event.getX(), event.getY());
                        ClipboardContent cc = new ClipboardContent();
                        cc.putString(header.getText());
                        db.setContent(cc);
                    }
                }
            }
        };
        return onHeaderDragDetected;
    }

    /**
     * On Drag hovering header accept copying.
     * @param header
     * @return
     */
    EventHandler<? super DragEvent> getOnDragOverAcceptCopy()
    {
        EventHandler<? super DragEvent> onDragOverAcceptCopy = new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent event)
            {
                event.acceptTransferModes(TransferMode.COPY);
            }
        };
        return onDragOverAcceptCopy;
    }

    /**
     * On Drag dropped on header, change the header, if match is found in map.
     * @param header
     * @return
     */
    EventHandler<? super DragEvent> getOnHeaderDragDropped(TextField header)
    {
        EventHandler<? super DragEvent> onHeaderDragDropped = new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent event)
            {
                Dragboard db = event.getDragboard();
                if (db.hasString())
                {
                    header.setText(db.getString());
                    event.setDropCompleted(true);
                }
                else
                {
                    event.setDropCompleted(false);
                }
            }
        };
        return onHeaderDragDropped;
    }

    /**
     * On Drag dropped on header, change the header, if match is found in map.
     * @param header
     * @return
     */
    EventHandler<? super DragEvent> getOnHeaderDragDone(int index, TextField header)
    {
        EventHandler<? super DragEvent> onHeaderDragDropped = new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent event)
            {
                List<StructEntity> sei = specification.getStructure();
                if (sei.size() > 0)
                {
                    Integer fromIndex = specification.getHeadersIndexAndExamples().get(header.getText()).getKey();

                    if (fromIndex == null)
                    {
                        return;
                    }
                    sei.set(index, null);
                }
                header.setText("");
            }
        };
        return onHeaderDragDropped;
    }

    /**
     * Drag and Drop event Simple sets drop completed if the drop is not empty.
     * @return
     */
    EventHandler<? super DragEvent> getOnHeaderPaneDropped()
    {
        EventHandler onHeaderPaneDropped = new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent e)
            {
                Dragboard db = e.getDragboard();
                if (db.hasString())
                {
                    e.setDropCompleted(true);
                }
                else
                {
                    e.setDropCompleted(false);
                }
            }
        };
        return onHeaderPaneDropped;
    }

    /**
     * Getting the right style for drag and drop
     * @param node
     * @return
     */
    private Scene getDragAndDropScene(Parent parent)
    {
        Scene scene = new Scene(parent);
        scene.getStylesheets().add(specification.getStylesheet());
        return scene;
    }
}
