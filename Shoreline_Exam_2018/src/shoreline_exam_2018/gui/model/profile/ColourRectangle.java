/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model.profile;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Asbamz
 */
public class ColourRectangle extends Rectangle
{
    /**
     * Creates a rectangle with the right indent colour.
     * @param width
     * @param height
     * @param specification
     */
    public ColourRectangle(double width, ReadOnlyDoubleProperty height, ProfileSpecification specification)
    {
        super();
        this.setWidth(width);
        this.heightProperty().bind(height);
        GridPane.setValignment(this, VPos.TOP);
        Double colourCount = (specification.getIndent() / specification.getDefaultIndent()) % specification.getColours().length;
        this.setFill(specification.getColours()[colourCount.intValue()]);
    }
}
