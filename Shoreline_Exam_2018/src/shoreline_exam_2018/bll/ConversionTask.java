/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

/**
 *
 * @author alexl
 */
public class ConversionTask extends HBox {
    
    private Label conversionName;
    private ProgressBar progress;
    private Button btnPause;
    private Button btnCancel;

    public ConversionTask(ConversionThread cThread) {
        super();

        conversionName = new Label();
        progress = new ProgressBar();
        btnPause = new Button();
        btnCancel = new Button();

        this.getChildren().addAll(conversionName, progress, btnPause, btnCancel);
    }
}
