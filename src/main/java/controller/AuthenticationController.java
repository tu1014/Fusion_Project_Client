package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthenticationController implements Initializable {

    @FXML VBox inputBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSignInBox();
    }

    void loadSignInBox() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/signInBox.fxml"));
            VBox inputBox = fxmlLoader.load();
            this.inputBox.getChildren().setAll(inputBox);

        }

        catch (IOException e) { e.printStackTrace(); }

    }





}
