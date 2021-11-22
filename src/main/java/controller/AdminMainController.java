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


    Button lastSelected;


    /*@FXML
    private void exit() throws IOException {

        System.out.println("종료 확인 다이아로그박스 실행");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/exitDialog.fxml"));
        AnchorPane dialogBox = fxmlLoader.load();

        Stage dialogStage = new Stage(StageStyle.UNDECORATED);
        Scene scene = new Scene(dialogBox);
        dialogStage.setScene(scene);
        dialogStage.initOwner(closeBtn.getScene().getWindow());
        dialogStage.initModality(Modality.WINDOW_MODAL);

        dialogStage.show();

    }*/

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
    private void studentList() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/studentList.fxml"));
        AnchorPane stdList = fxmlLoader.load();

        StudentListController studentListController = fxmlLoader.getController();
        studentListController.setParentController(this);

        /*for(Student std : stdDB.getList()) {

            fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/studentItem.fxml"));
            HBox item = fxmlLoader.load();
            StudentItemController studentItemController = fxmlLoader.getController();
            studentItemController.id.setText(std.getStdNumber());
            studentItemController.name.setText(std.getName());
            studentListController.vBox.getChildren().add(item);

        }*/

//        for(int i=0; i<20; i++) {
//
//            fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/studentItem.fxml"));
//            HBox item = fxmlLoader.load();
//            studentListController.vBox.getChildren().add(item);
//
//        }

        mainPanel.getChildren().setAll(stdList);

    }

    @FXML
    private void professorList() {

    }

    @FXML
    private void lectureList() {

    }


}
