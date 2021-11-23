package controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthenticationController implements Initializable {

    @FXML VBox inputBox;
    @FXML Label message;
    @FXML HBox messageBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSignInBox();
    }

    void loadSignInBox() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/signInBox.fxml"));
            VBox inputBox = fxmlLoader.load();
            SignInController controller = fxmlLoader.getController();
            controller.setParentController(this);
            this.inputBox.getChildren().setAll(inputBox);

        }

        catch (IOException e) { e.printStackTrace(); }

    }

    void showMessage(String message) {

        this.message.setText(message);
        messageBox.setVisible(true);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), messageBox);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);
        fadeOut.play();

        fadeOut.setOnFinished( (e) -> { messageBox.setVisible(false); });
    }





}
