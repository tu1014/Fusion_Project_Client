package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


public class AdminMainController {

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

    /*@FXML
    private void logout() throws IOException {

        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        System.out.println("로그아웃 확인 다이아로그박스 실행");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/logoutDialog.fxml"));
        AnchorPane dialogBox = fxmlLoader.load();

        Stage dialogStage = new Stage(StageStyle.UNDECORATED);
        Scene scene = new Scene(dialogBox);
        dialogStage.setScene(scene);
        dialogStage.initOwner(closeBtn.getScene().getWindow());
        dialogStage.initModality(Modality.WINDOW_MODAL);

        dialogStage.show();

        LogoutDialogController logoutDialogController = fxmlLoader.getController();
        logoutDialogController.setDB(adminDB,stdDB,pfDB);

        dialogBox.setOnMousePressed( (MouseEvent event) -> {

            xOffset.set(event.getSceneX());
            yOffset.set(event.getSceneY());

        });


        Stage box = (Stage) logoutDialogController.anchor.getScene().getWindow();

        dialogBox.setOnMouseDragged( (MouseEvent event) -> {

            box.setX(event.getScreenX() - xOffset.get());
            box.setY(event.getScreenY() - yOffset.get());

        });

    }*/

    /*@FXML
    private void setTimeLimit() {
        currentMenu.setText("Set time limit");

    }

    @FXML
    private void studentList() throws IOException {

        currentMenu.setText("Student list");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/studentList.fxml"));
        VBox stdList = fxmlLoader.load();

        StudentListController studentListController = fxmlLoader.getController();
        studentListController.setDB(adminDB, stdDB, pfDB);

        for(Student std : stdDB.getList()) {

            fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/studentItem.fxml"));
            HBox item = fxmlLoader.load();
            StudentItemController studentItemController = fxmlLoader.getController();
            studentItemController.id.setText(std.getStdNumber());
            studentItemController.name.setText(std.getName());
            studentListController.vBox.getChildren().add(item);

        }




//        for(int i=0; i<20; i++) {
//
//            fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/studentItem.fxml"));
//            HBox item = fxmlLoader.load();
//            studentListController.vBox.getChildren().add(item);
//
//        }

        mainBox.getChildren().setAll(stdList);

    }*/

    /*@FXML
    private void professorList() {
        currentMenu.setText("Professor list");
    }

    @FXML
    private void lectureList() {
        currentMenu.setText("Lecture list");
    }*/


}
