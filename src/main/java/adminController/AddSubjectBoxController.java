package adminController;

import Validator.Validator;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Connector;
import network.Protocol;
import persistence.Entity.Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AddSubjectBoxController implements Initializable {

    // 디폴트값, 셋온액션에서 맵 사용하여 값 바꿔주기
    // 그럼 서버에서 학과 이름 검색 후 아이디 넣어주던 부분 필요 없음 여기서 아이디로 보내니까

    InputStream is;
    OutputStream os;

    private SubjectListController parentController;
    Protocol protocol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        protocol = Connector.getProtocol();

        Socket socket = Connector.getSocket();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

        } catch (IOException e) { e.printStackTrace(); }

        // default 값 어케하지?

        gradeBox.getItems().add("1학년");
        gradeBox.getItems().add("2학년");
        gradeBox.getItems().add("3학년");
        gradeBox.getItems().add("4학년");
        gradeBox.setOnAction(this::setGrade);

        semesterBox.getItems().add("1학기");
        semesterBox.getItems().add("2학기");
        semesterBox.setOnAction(this::setSemester);

        creditBox.getItems().add("1학점");
        creditBox.getItems().add("2학점");
        creditBox.getItems().add("3학점");
        creditBox.getItems().add("4학점");
        creditBox.setOnAction(this::setCredit);


    }

    public void setGrade(ActionEvent event) {
        String choice = gradeBox.getValue();
        if (choice.equals("1학년")) grade = 1;
        else if (choice.equals("2학년")) grade = 2;
        else if (choice.equals("3학년")) grade = 3;
        else if (choice.equals("4학년")) grade = 4;
    }

    public void setCredit(ActionEvent event) {
        String choice = creditBox.getValue();
        if (choice.equals("1학점")) credit = 1;
        else if (choice.equals("2학점")) credit = 2;
        else if (choice.equals("3학점")) credit = 3;
        else if (choice.equals("4학점")) credit = 4;
    }

    public void setSemester(ActionEvent event) {
        String choice = semesterBox.getValue();
        if (choice.equals("1학기")) semester = 1;
        else if (choice.equals("2학기")) semester = 2;
    }


    public void setParentController(SubjectListController p) {parentController = p;}

    @FXML Button addBtn;
    @FXML TextField name;
    @FXML TextField code;
    @FXML ComboBox<String> gradeBox;
    @FXML ComboBox<String> semesterBox;
    @FXML ComboBox<String> creditBox;
    @FXML HBox messageBox;
    @FXML Label message;

    int semester = 0;
    int credit = 0;
    int grade = 0;

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
    private void addSubject() throws IOException {

        System.out.println("addSubject");
        String code = this.code.getText();
        String name = this.name.getText();

        if(Validator.isEmpty(code)) {
            showMessage("올바르지 않은 코드 입력");
            return;
        }
        if(Validator.isEmpty(name)) {
            showMessage("올바르지 않은 과목명 입력");
            return;
        }
        if(Validator.isZero(semester)) {
            showMessage("학기를 선택하세요");
            return;
        }
        if(Validator.isZero(credit)) {
            showMessage("학점을 선택하세요");
            return;
        }
        if(Validator.isZero(grade)) {
            showMessage("대상 학년을 선택하세요");
            return;
        }


        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.CREATE, Protocol.SUBJECT);

        protocol.addBodyStringData(code.getBytes());
        protocol.addBodyStringData(name.getBytes());
        protocol.addBodyIntData(grade);
        protocol.addBodyIntData(semester);
        protocol.addBodyIntData(credit);

        ArrayList<byte[]> packetList = protocol.getAllPacket();

        packetList.stream().forEach(v -> {
            try {
                os.write(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Connector.read();

        byte[] header = Connector.getHeader();
        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {

            showMessage("교과목 코드 중복");
            return;

        }

        else {

            parentController.parentController.showMessage("교과목 생성 성공");
            Stage dialog = (Stage) addBtn.getScene().getWindow();
            dialog.close();

        }

    }







}
