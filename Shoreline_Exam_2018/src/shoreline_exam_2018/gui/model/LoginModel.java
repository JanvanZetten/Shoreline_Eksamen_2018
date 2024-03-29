/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.gui.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import shoreline_exam_2018.be.LogType;
import shoreline_exam_2018.bll.BLLException;
import shoreline_exam_2018.bll.BLLFacade;
import shoreline_exam_2018.bll.BLLManager;
import shoreline_exam_2018.bll.LoggingHelper;

/**
 *
 * @author alexl
 */
public class LoginModel
{

    private BLLFacade bll;

    public LoginModel()
    {
        bll = BLLManager.getInstance();
    }

    /**
     * Logs the user in. Displays an error if a wrong login atempt is made.
     */
    public void attemptLogin(String username, String password, Parent root, Stage loginStage)
    {
        try
        {
            bll.login(username, password);
            openMainView(root, loginStage);
        }
        catch (BLLException ex)
        {
            AlertFactory.showError("Wrong information", "The username and password combination doesn't exist. Please try again.");
        }
    }

    /**
     * Opens the MainView on login. Closes the LoginView. Sets the user that has
     * logged in. Also writes a log when succesfully logging in.
     * @param root
     * @param loginStage
     */
    private void openMainView(Parent root, Stage loginStage)
    {
        try
        {
            Scene mainScene = new Scene(root);
            Stage mainStage = new Stage();
            mainStage.setScene(mainScene);
            mainStage.setTitle("Shoreline MappingTool");
            mainStage.getIcons().add(new Image("shoreline_exam_2018/logo.png"));
            mainStage.show();
            mainStage.setScene(mainScene);
            mainStage.centerOnScreen();

            String ipaddress = InetAddress.getLocalHost().toString();
            String[] split = ipaddress.split("/");

            bll.addLog(LogType.LOGIN, "User " + bll.getcurrentUser().getName() + " has logged in from the IP address " + split[1]);

            loginStage.close();
        }
        catch (BLLException | UnknownHostException ex)
        {
            LoggingHelper.logException(ex);
            AlertFactory.showError("Could not open main view", ex.getMessage());
        }
    }
}
