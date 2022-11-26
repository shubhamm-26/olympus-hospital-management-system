package UI.Controllers.Nurse;

import UI.Elements.PopUpBox;
import UI.Functions.JumpScene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class HomeControllerNurse {

    @FXML
    private BorderPane homePane;

    @FXML
    private Button patientButton;

    @FXML
    void gotoAdmittedTab(MouseEvent event) {

    }

    @FXML
    void gotoPatientTabNurse(MouseEvent event) {

    }

    @FXML
    void gotoSettingsTab(MouseEvent event) throws IOException {
        JumpScene.changeScene(homePane,"UI/settingsTab.fxml",event);
    }

    @FXML
    void logout(MouseEvent event) throws IOException {
        PopUpBox.logout("Confirm Logout?");
        JumpScene.changeScene(homePane,"UI/login_staff.fxml",event);
    }

}
