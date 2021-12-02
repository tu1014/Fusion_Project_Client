import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

import java.io.*;
import java.net.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // 폰트 적용
        Font.loadFont(getClass().getResource("font/bmjua_ttf.ttf").toString(), 20).getFamily();

        // 스플래시 화면 로딩
        Parent root = FXMLLoader.load(getClass().getResource("fxml/util/splash.fxml"));
        primaryStage.setScene(new Scene(root));


        primaryStage.setTitle("Fusion Project");
        primaryStage.setResizable(false);

        // 종료 이벤트 발생시 해당 이벤트를 날리고 종료 다이아로그 박스 로드
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent evt) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/util/exitDialog.fxml"));
                AnchorPane dialogBox = null;

                try {

                    dialogBox = fxmlLoader.load();
                    Stage dialogStage = new Stage();
                    Scene scene = new Scene(dialogBox);
                    dialogStage.setScene(scene);
                    dialogStage.initOwner(primaryStage.getScene().getWindow());
                    dialogStage.initModality(Modality.WINDOW_MODAL);

                    evt.consume();
                    dialogStage.show();
                }

                catch (IOException e) { e.printStackTrace(); }
            }
        });

        primaryStage.show();

    }

    public static void main(String[] args) { launch(args); }



}
