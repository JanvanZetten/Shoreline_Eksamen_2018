/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import shoreline_exam_2018.be.output.structure.StructEntityInterface;

/**
 *
 * @author Asbamz
 */
public class ProfileSpecification
{
    private final String STYLESHEET = "shoreline_exam_2018/gui/view/css/style.css"; // Root CSS
    private final String CSS_ID_DRAGANDDROP = "DRAGANDDROP";
    private final boolean IS_MASTER; // Is this the master grid
    private final double INDENT; // Indent for collections.
    private final double DEFAULT_INDENT = 10.0; // Indent for collections.
    private final double DEFAULT_GAP = 5.0;
    private final double DEFAULT_BUTTON_WIDTH = 60.0;
    private final double DEFAULT_COMBOBOX_WIDTH = 120.0;
    private final double DEFAULT_LABEL_WIDTH = 160.0;
    private final double DEFAULT_TEXTFIELD_WIDTH = 160.0;
    private final double DEFAULT_CHECKBOX_WIDTH = 30.0;
    private final double DEFAULT_RECTANGLE_WIDTH = 4.0;
    private final Paint[] COLOURS = new Paint[]
    {
        Color.web("737F8C"), Color.web("4986A8"), Color.web("2C546D"), Color.web("4D4D4D")
    }; // Group/Collection colours.
    private DragAndDropHandler dragAndDropHandler;
    private ObservableMap<String, Entry<Integer, String>> headersIndexAndExamples; // Mapping column headers from input file to their example.
    private HashMap<Integer, String> headersIndexToName; // Mapping column headers from input file to their example.
    private List<StructEntityInterface> structure; // The structure of made from grid elements.
    private List<ProfileEntity> structureEntities; // The grid elements.
    private MapChangeListener<String, Entry<Integer, String>> headersIndexToNameListener;

    ProfileSpecification(boolean IS_MASTER, double INDENT)
    {
        this.IS_MASTER = IS_MASTER;
        this.INDENT = INDENT;
        this.dragAndDropHandler = new DragAndDropHandler(this);
        this.headersIndexToName = new HashMap();
        this.headersIndexAndExamples = FXCollections.observableHashMap();
        setHeadersIndexToNameListener();
        this.structure = new ArrayList<>();
        this.structureEntities = new ArrayList<>();
    }

    public String getStylesheet()
    {
        return STYLESHEET;
    }

    public String getCSSIdDragAndDrop()
    {
        return CSS_ID_DRAGANDDROP;
    }

    public double getDefaultIndent()
    {
        return DEFAULT_INDENT;
    }

    public double getDefaultGap()
    {
        return DEFAULT_GAP;
    }

    public boolean isMaster()
    {
        return IS_MASTER;
    }

    public double getDefaultButtonWidth()
    {
        return DEFAULT_BUTTON_WIDTH;
    }

    public double getDefaultComboboxWidth()
    {
        return DEFAULT_COMBOBOX_WIDTH;
    }

    public double getDefaultLabelWidth()
    {
        return DEFAULT_LABEL_WIDTH;
    }

    public double getDefaultTextFieldWidth()
    {
        return DEFAULT_TEXTFIELD_WIDTH;
    }

    public double getDefaultCheckBoxWidth()
    {
        return DEFAULT_CHECKBOX_WIDTH;
    }

    public double getDefaultRectangleWidth()
    {
        return DEFAULT_RECTANGLE_WIDTH;
    }

    public double getIndent()
    {
        return INDENT;
    }

    public Paint[] getColours()
    {
        return COLOURS;
    }

    /**
     * Add Map from other class.
     * @param headersIndexAndExamples
     */
    void addHashMap(ObservableMap<String, Entry<Integer, String>> headersIndexAndExamples)
    {
        headersIndexToName.clear();
        this.headersIndexAndExamples.clear();

        if (IS_MASTER)
        {
            this.headersIndexAndExamples.putAll(headersIndexAndExamples);
        }
        else
        {
            this.headersIndexAndExamples = headersIndexAndExamples;
            setHeadersIndexToNameListener();
        }
    }

    void clearStructure()
    {
        structure.clear();
        structureEntities.clear();
    }

    /**
     * Gets structure.
     * @return
     */
    public List<StructEntityInterface> getStructure()
    {
        return structure;
    }

    /**
     * Gets structure entities.
     * @return
     */
    public List<ProfileEntity> getStructureEntities()
    {
        return structureEntities;
    }

    public ObservableMap<String, Entry<Integer, String>> getHeadersIndexAndExamples()
    {
        return headersIndexAndExamples;
    }

    public HashMap<Integer, String> getHeadersIndexToName()
    {
        return headersIndexToName;
    }

    public DragAndDropHandler getDragAndDropHandler()
    {
        return dragAndDropHandler;
    }

    /**
     * Set ObservableMap listener.
     */
    private void setHeadersIndexToNameListener()
    {
        if (headersIndexToNameListener != null)
        {
            headersIndexAndExamples.removeListener(headersIndexToNameListener);
        }
        headersIndexToNameListener = new MapChangeListener<String, Entry<Integer, String>>()
        {
            @Override
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Entry<Integer, String>> change)
            {
                onHeadersIndexToNameChanged();
            }
        };
        headersIndexAndExamples.addListener(headersIndexToNameListener);
        onHeadersIndexToNameChanged();
    }

    /**
     * What should happen when the ObservableMap is changed.
     */
    private void onHeadersIndexToNameChanged()
    {
        headersIndexToName.clear();
        if (headersIndexAndExamples != null)
        {
            if (!headersIndexAndExamples.isEmpty())
            {
                for (String string : headersIndexAndExamples.keySet())
                {
                    headersIndexToName.put(headersIndexAndExamples.get(string).getKey(), string);
                }
            }
        }
    }

    public void setDefaultWidth(Button btn)
    {
        btn.setMinWidth(DEFAULT_BUTTON_WIDTH);
        btn.setPrefWidth(DEFAULT_BUTTON_WIDTH);
        btn.setMaxWidth(DEFAULT_BUTTON_WIDTH);
    }

    public void setDefaultWidth(CheckBox cb)
    {
        cb.setMinWidth(DEFAULT_CHECKBOX_WIDTH);
        cb.setPrefWidth(DEFAULT_CHECKBOX_WIDTH);
        cb.setMaxWidth(DEFAULT_CHECKBOX_WIDTH);
    }

    public void setDefaultWidth(Label lbl)
    {
        lbl.setMinWidth(DEFAULT_LABEL_WIDTH);
        lbl.setPrefWidth(DEFAULT_LABEL_WIDTH);
        lbl.setMaxWidth(DEFAULT_LABEL_WIDTH);
    }

    public void setDefaultWidth(ComboBox cmb)
    {
        cmb.setMinWidth(DEFAULT_COMBOBOX_WIDTH);
        cmb.setPrefWidth(DEFAULT_COMBOBOX_WIDTH);
        cmb.setMaxWidth(DEFAULT_COMBOBOX_WIDTH);
    }

    public void setDefaultWidth(TextField tf)
    {
        tf.setMinWidth(DEFAULT_TEXTFIELD_WIDTH);
        tf.setPrefWidth(DEFAULT_TEXTFIELD_WIDTH);
        tf.setMaxWidth(DEFAULT_TEXTFIELD_WIDTH);
    }

    public void setDefaultWidth(DatePicker dp)
    {
        dp.setMinWidth(DEFAULT_TEXTFIELD_WIDTH);
        dp.setPrefWidth(DEFAULT_TEXTFIELD_WIDTH);
        dp.setMaxWidth(DEFAULT_TEXTFIELD_WIDTH);
    }
}
