package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


public class AdminMainController {

    @FXML Label userId;
    @FXML Label name;
    @FXML AnchorPane mainPanel;
    @FXML HBox messageBox;
    @FXML Label message;
    @FXML Button timeLimit;
    @FXML Button studentList;
    @FXML Button professorList;
    @FXML Button lectureList;

    @FXML
    private void logout() throws IOException {

        System.out.println("로그아웃 확인 다이아로그박스 실행");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/logoutDialog.fxml"));
        AnchorPane dialogBox = fxmlLoader.load();

        Stage dialogStage = new Stage();
        Scene scene = new Scene(dialogBox);
        dialogStage.setScene(scene);
        dialogStage.initOwner(name.getScene().getWindow());
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setResizable(false);

        dialogStage.show();

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

    @FXML
    private void setTimeLimit() {

    }

    @FXML
    private void studentList() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/studentList.fxml"));
        AnchorPane stdList = null;

        try {

            stdList = fxmlLoader.load();
            StudentListController studentListController = fxmlLoader.getController();
            studentListController.setParentController(this);

            mainPanel.getChildren().setAll(stdList);

        } catch (IOException e) { e.printStackTrace(); }

    }

    @FXML
    private void professorList() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/professorList.fxml"));
        AnchorPane stdList = null;

        try {

            stdList = fxmlLoader.load();
            ProfessorListController professorListController = fxmlLoader.getController();
            professorListController.setParentController(this);

            mainPanel.getChildren().setAll(stdList);

        } catch (IOException e) { e.printStackTrace(); }

    }

    @FXML
    private void lectureList() {

    }


}
