/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Asbamz
 */
public class AlertFactory
{
    private static final Image LOGO = new Image("shoreline_exam_2018/logo.png");
    private static final String STYLESHEET = "shoreline_exam_2018/gui/view/css/style.css";
    private static final String TITLE_WARNING = "Warning";
    private static final String TITLE_ERROR = "Error";
    private static final String TITLE_INFO = "Information";
    private static final String TITLE_CONFIRMATION = "Confirmation";

    /**
     * Shows Alert Warning.
     * @param message
     */
    public static void showWarning(String header, String message)
    {
        showCustomAlert(TITLE_WARNING, header, message);
    }

    /**
     * Shows Alert Error.
     * @param message
     */
    public static void showError(String header, String message)
    {
        showCustomAlert(TITLE_ERROR, header, message);
    }

    /**
     * Shows Alert Information.
     * @param message
     */
    public static void showInformation(String header, String message)
    {
        showCustomAlert(TITLE_INFO, header, message);
    }
    
    /**
     * Shows Alert Confirmation.
     * @param message
     */
    public static void showConfirmation(String header, String message)
    {
        showCustomConfirmation(TITLE_CONFIRMATION, header, message);
    }

    /**
     * Shared rules for an information alert.
     * @param title
     * @param header
     * @param message
     */
    private static void showCustomAlert(String title, String header, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        //Image image = new Image();
        //ImageView imageView = new ImageView(image);
        //alert.setGraphic(imageView);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(LOGO);
        alert.getDialogPane().getScene().getStylesheets().add(STYLESHEET);
        alert.showAndWait();
    }
    
    /**
     * Shared rules for a confirmation alert.
     * @param title
     * @param header
     * @param message 
     */
    private static void showCustomConfirmation(String title, String header, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES);
        alert.setTitle(title);
        alert.setHeaderText(header);
        //Image image = new Image();
        //ImageView imageView = new ImageView(image);
        //alert.setGraphic(imageView);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(LOGO);
        alert.getDialogPane().getScene().getStylesheets().add(STYLESHEET);
        alert.showAndWait();
    }
    

}
