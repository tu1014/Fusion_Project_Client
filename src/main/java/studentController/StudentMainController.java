package studentController;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import persistence.Entity.Student;


import java.io.IOException;


public class StudentMainController {

    @FXML Label userId;
    @FXML Label name;
    @FXML AnchorPane mainPanel;
    @FXML HBox messageBox;
    @FXML Label message;

    Student currentUser;

    public void setCurrentUser(Student stdDTO) {
        this.currentUser = stdDTO;
        setUserInfo();
    }

    public void setUserInfo() {
        userId.setText(currentUser.getStudentId());
        name.setText(currentUser.getName());
    }


    @FXML
    private void logout() throws IOException {

        System.out.println("로그아웃 확인 다이아로그박스 실행");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/authentication/logoutDialog.fxml"));
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
    private void professorList() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/student/professorList.fxml"));
        AnchorPane professorList = null;

        try {

            professorList = fxmlLoader.load();
            ProfessorListController professorListController = fxmlLoader.getController();
            professorListController.setParentController(this);

            mainPanel.getChildren().setAll(professorList);

        } catch (IOException e) { e.printStackTrace(); }

    }
    @FXML
    private void lectureList() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/student/lectureList.fxml"));
        AnchorPane lectureList = null;

        try {

            lectureList = fxmlLoader.load();
            LectureListController lectureListController = fxmlLoader.getController();
            lectureListController.setParentController(this);

            mainPanel.getChildren().setAll(lectureList);

        } catch (IOException e) { e.printStackTrace(); }

    }
    @FXML
    private void subjectList() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/student/subjectList.fxml"));
        AnchorPane subjectList = null;

        try {

            subjectList = fxmlLoader.load();
            SubjectListController subjectListController = fxmlLoader.getController();
            subjectListController.setParentController(this);

            mainPanel.getChildren().setAll(subjectList);

        } catch (IOException e) { e.printStackTrace(); }

    }

    @FXML
    private void timeTable() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/student/timeTable.fxml"));
        AnchorPane subjectList = null;

        try {

            subjectList = fxmlLoader.load();
            TimeTableController con = fxmlLoader.getController();
            con.setParentController(this);

            mainPanel.getChildren().setAll(subjectList);

        } catch (IOException e) { e.printStackTrace(); }

    }



}
