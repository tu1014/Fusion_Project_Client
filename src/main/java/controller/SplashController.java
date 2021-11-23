package controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML VBox vBox;
    Socket socket = null;

    boolean isConnected = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            socket = new Socket("127.0.0.1", 3000);
            System.out.println("서버 접속 성공");
            isConnected = true;
        }

        catch(IOException ioe) {ioe.printStackTrace();}


        FadeTransition fadeIn = new FadeTransition(Duration.seconds(4), vBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        fadeIn.setOnFinished( (e) -> {

            moveToAuthentication();


        });

    }

    private void moveToAuthentication() {

        if (isConnected == false) {

            System.out.println("연결 실패");
            return;

        }

        Stage stage = (Stage) vBox.getScene().getWindow();

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/authentication.fxml"));
            Parent root = fxmlLoader.load();
            AuthenticationController con = fxmlLoader.getController();
            con.setSocket(socket);
            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }










}
