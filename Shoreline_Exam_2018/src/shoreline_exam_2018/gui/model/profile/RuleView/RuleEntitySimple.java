/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile.RuleView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import shoreline_exam_2018.be.output.rule.DateFormatRule;
import shoreline_exam_2018.be.output.rule.DefaultDateRule;
import shoreline_exam_2018.be.output.rule.DefaultDoubleRule;
import shoreline_exam_2018.be.output.rule.DefaultIntegerRule;
import shoreline_exam_2018.be.output.rule.DefaultStringRule;
import shoreline_exam_2018.be.output.structure.SimpleEntity;
import shoreline_exam_2018.be.output.structure.entity.StructEntityDate;
import shoreline_exam_2018.be.output.structure.type.SimpleStructType;
import shoreline_exam_2018.gui.model.profile.ColourRectangle;
import shoreline_exam_2018.gui.model.profile.ProfileSpecification;

/**
 *
 * @author Asbamz
 */
public class RuleEntitySimple
{
    private final RulePane owner;
    private final ProfileSpecification specification;
    private final SimpleEntity se;

    private Rectangle colourBox;
    private Label lblHeader;
    private Label lblExample;
    private Label lblTo;
    private ComboBox<String> cmbDefaultRule;
    private DatePicker defaultDate;
    private TextField defaultString;
    private TextField dateFormat;
    private Label lblForced;
    private CheckBox cbForced;
    private GridPane defaultValueRow;
    private GridPane dateFormatRow;

    /**
     * Creates a rule row for Simple Entity.
     * @param index
     * @param se
     * @param owner
     * @param specification
     */
    public RuleEntitySimple(int index, SimpleEntity se, RulePane owner, ProfileSpecification specification)
    {
        this.owner = owner;
        this.specification = specification;
        this.se = se;

        final int entityIndex = index;

        String headerName = specification.getHeadersIndexToName().get(se.getInputIndex());
        lblHeader = new Label(headerName);
        lblExample = new Label(specification.getHeadersIndexAndExamples().get(headerName) != null ? specification.getHeadersIndexAndExamples().get(headerName).getValue() : "");
        lblTo = new Label(se.getColumnName());
        cmbDefaultRule = new ComboBox<>();
        defaultDate = new DatePicker();
        defaultString = new TextField();
        dateFormat = new TextField();
        lblForced = new Label("Force:");
        cbForced = new CheckBox();
        colourBox = new ColourRectangle(specification.getDefaultRectangleWidth(), defaultString.heightProperty(), specification);

        GridPane.setConstraints(colourBox, 0, specification.isMaster() ? entityIndex + 1 : entityIndex);
        GridPane.setConstraints(lblHeader, 1, specification.isMaster() ? entityIndex + 1 : entityIndex);
        GridPane.setConstraints(lblExample, 2, specification.isMaster() ? entityIndex + 1 : entityIndex);
        GridPane.setConstraints(lblTo, 3, specification.isMaster() ? entityIndex + 1 : entityIndex);
        GridPane.setConstraints(cmbDefaultRule, 4, specification.isMaster() ? entityIndex + 1 : entityIndex);

        // GridPane containing default value rule.
        defaultValueRow = new GridPane();
        owner.setupGridPane(defaultValueRow, -0.1);
        GridPane.setConstraints(lblForced, 0, 0);
        GridPane.setConstraints(cbForced, 1, 0);
        GridPane.setConstraints(defaultDate, 2, 0);
        GridPane.setConstraints(defaultString, 2, 0);
        GridPane.setConstraints(dateFormat, 0, 0);
        defaultValueRow.getChildren().addAll(defaultDate, defaultString, lblForced, cbForced);
        GridPane.setConstraints(defaultValueRow, 5, specification.isMaster() ? entityIndex + 1 : entityIndex);

        // GridPane to containing date format rule.
        dateFormatRow = new GridPane();
        owner.setupGridPane(dateFormatRow, -0.1);
        GridPane.setConstraints(dateFormatRow, 5, specification.isMaster() ? entityIndex + 1 : entityIndex);
        dateFormatRow.getChildren().addAll(dateFormat);

        // Set size.
        specification.setDefaultWidth(lblHeader);
        specification.setDefaultWidth(lblExample);
        specification.setDefaultWidth(lblTo);
        specification.setDefaultWidth(cmbDefaultRule);
        specification.setDefaultWidth(defaultDate);
        specification.setDefaultWidth(defaultString);
        specification.setDefaultWidth(cbForced);
        specification.setDefaultWidth(dateFormat);

        // Integer and Double cannot be empty restriction.
        defaultString.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue)
            {
                switch (se.getSST())
                {
                    case INTEGER:
                        if (defaultString.getText() != null)
                        {
                            if (defaultString.getText().isEmpty())
                            {
                                defaultString.setText("0");
                            }
                        }
                        else
                        {
                            defaultString.setText("0");
                        }
                        break;
                    case DOUBLE:
                        if (defaultString.getText() != null)
                        {
                            if (defaultString.getText().isEmpty())
                            {
                                defaultString.setText("0.0");
                            }
                        }
                        else
                        {
                            defaultString.setText("0.0");
                        }
                        break;
                    default:
                        if (defaultString.getText() == null)
                        {
                            defaultString.setText("");
                        }
                        break;
                }
            }
        });

        // Update Entity on change
        defaultDate.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                se.setDefaultValue(new DefaultDateRule(Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant()), entityIndex, cbForced.isSelected()));
            }
            else
            {
                se.setDefaultValue(null);
            }
        });

        // Update Entity on change. Set rule fitting Simple Struct Type.
        defaultString.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                switch (se.getSST())
                {
                    case STRING:
                        se.setDefaultValue(new DefaultStringRule(newValue, entityIndex, cbForced.isSelected()));
                        break;
                    case INTEGER:
                        if (newValue.isEmpty())
                        {
                            break;
                        }
                        try
                        {
                            int integer = Integer.parseInt(newValue);
                            se.setDefaultValue(new DefaultIntegerRule(integer, entityIndex, cbForced.isSelected()));
                        }
                        catch (NumberFormatException ex)
                        {
                            if (oldValue == null)
                            {
                                defaultString.setText("");
                                break;
                            }
                            try
                            {
                                int integer = Integer.parseInt(oldValue);
                                defaultString.setText(String.valueOf(integer));
                            }
                            catch (NumberFormatException ex1)
                            {
                                defaultString.setText("");
                            }
                        }
                        break;
                    case DOUBLE:
                        if (newValue.isEmpty())
                        {
                            break;
                        }
                        try
                        {
                            Double dbl = Double.parseDouble(newValue);
                            se.setDefaultValue(new DefaultDoubleRule(dbl, entityIndex, cbForced.isSelected()));
                        }
                        catch (NumberFormatException ex)
                        {
                            if (oldValue == null)
                            {
                                defaultString.setText("");
                                break;
                            }
                            try
                            {
                                Double dbl = Double.parseDouble(oldValue);
                                defaultString.setText(String.valueOf(dbl));
                            }
                            catch (NumberFormatException ex1)
                            {
                                defaultString.setText("");
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            else
            {
                se.setDefaultValue(null);
            }
        });

        // Updates TextFields so that CheckBox change is used.
        cbForced.selectedProperty().addListener((observable, oldValue, newValue) ->
        {
            LocalDate ld = defaultDate.getValue();
            defaultDate.setValue(null);
            defaultDate.setValue(ld);
            String str = defaultString.getText();
            defaultString.setText(null);
            defaultString.setText(str);
        });

        // Update rule on change.
        dateFormat.textProperty().addListener((observable, oldValue, newValue) ->
        {
            StructEntityDate sed = (StructEntityDate) se;

            if (newValue != null)
            {
                sed.setDfr(new DateFormatRule(newValue, entityIndex));
            }
            else
            {
                sed.setDfr(null);
            }
        });

        // Show elements matching choice.
        cmbDefaultRule.valueProperty().addListener((observable, oldValue, newValue) ->
        {
            switch (newValue)
            {
                case "Default":
                    showDefaultValue();
                    break;
                case "DateFormat":
                    showDateFormat();
                    break;
                default:
                    hideAll();
                    break;
            }
        });

        // Available choices.
        ObservableList<String> rules = FXCollections.observableArrayList();
        rules.addAll("No Rule", "Default");
        if (se.getSST() == SimpleStructType.DATE)
        {
            rules.add("DateFormat");
        }
        cmbDefaultRule.setItems(rules);
        cmbDefaultRule.getSelectionModel().selectFirst();

        index++;
    }

    /**
     * Show default elements matching Simple Struct Type.
     */
    private void showDefaultValue()
    {
        hideAll();
        switch (se.getSST())
        {
            case DATE:
                defaultValueRow.setVisible(true);
                LocalDate ld = defaultDate.getValue();
                defaultDate.setValue(ld);
                break;
            default:
                defaultValueRow.setVisible(true);
                String str = defaultString.getText();

                if (str == null)
                {
                    switch (se.getSST())
                    {
                        case INTEGER:
                            defaultString.setText("0");
                            break;
                        case DOUBLE:
                            defaultString.setText("0.0");
                            break;
                        default:
                            break;
                    }
                }
                else
                {
                    defaultString.setText(str);
                }
                break;
        }
    }

    /**
     * Show date format element.
     */
    private void showDateFormat()
    {
        hideAll();
        dateFormatRow.setVisible(true);
    }

    /**
     * Hide all elements.
     */
    private void hideAll()
    {
        se.setDefaultValue(null);
        defaultValueRow.setVisible(false);
        dateFormatRow.setVisible(false);
        defaultDate.setValue(null);
        defaultString.setText(null);
    }

    /**
     * Get all nodes.
     * @return
     */
    List<Node> getNodes()
    {
        List<Node> nodes = new ArrayList<>();

        nodes.add(colourBox);
        nodes.add(lblHeader);
        nodes.add(lblExample);
        nodes.add(lblTo);
        nodes.add(cmbDefaultRule);
        nodes.add(defaultValueRow);
        nodes.add(dateFormatRow);

        return nodes;
    }
}
