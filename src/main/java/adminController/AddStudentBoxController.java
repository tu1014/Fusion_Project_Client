package adminController;

import Validator.Validator;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Connector;
import network.Protocol;
import persistence.Entity.Department;
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

public class AddStudentBoxController implements Initializable {

    InputStream is;
    OutputStream os;

    private StudentListController parentController;
    Protocol protocol;
    Map<String, Integer> departmentMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        departmentMap = new HashMap<>();
        protocol = Connector.getProtocol();

        Socket socket = Connector.getSocket();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

            protocol.init();
            protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.DEPARTMENT);
            os.write(protocol.getPacket());

            Connector.read();

            byte[] header = Connector.getHeader();
            if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {

                showMessage("학과가 존재하지 않습니다.");

            }

            else {

                int count = Connector.readInt();

                for(int i=0; i<count; i++) {

                    int id = Connector.readInt();
                    String name = Connector.readString();
                    departmentMap.put(name, id);
                    departmentBox.getItems().add(name);

                }
            }

        } catch (IOException e) { e.printStackTrace(); }

        // default 값 어케하지?
        departmentBox.setOnAction(this::setDepartment);

        gradeBox.getItems().add("1학년");
        gradeBox.getItems().add("2학년");
        gradeBox.getItems().add("3학년");
        gradeBox.getItems().add("4학년");
        gradeBox.setOnAction(this::setGrade);

    }

    public void setGrade(ActionEvent event) {
        String choice = gradeBox.getValue();
        if (choice.equals("1학년")) grade = 1;
        else if (choice.equals("2학년")) grade = 2;
        else if (choice.equals("3학년")) grade = 3;
        else if (choice.equals("4학년")) grade = 4;
    }

    public void setDepartment(ActionEvent event) {
        String choice = departmentBox.getValue();
        department = choice;
    }

    public void setParentController(StudentListController p) {parentController = p;}

    @FXML Button addBtn;
    @FXML TextField studentId;
    @FXML TextField name;
    @FXML TextField phoneNumber;
    @FXML PasswordField pw;
    @FXML ComboBox<String> gradeBox;
    @FXML ComboBox<String> departmentBox;
    @FXML HBox messageBox;
    @FXML Label message;

    String department = "";
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
    private void addStd() throws IOException {

        String studentID = this.studentId.getText();
        String pw = this.pw.getText();
        String name = this.name.getText();
        String phoneNumber = this.phoneNumber.getText();

        if(Validator.isValidStudentId(studentID) == false) {
            showMessage("올바르지 않은 학번 입력");
            return;
        }
        if(Validator.isValidBirthDay(pw) == false) {
            showMessage("올바르지 않은 생년월일(password) 입력");
            return;
        }
        if(Validator.isEmpty(name)) {
            showMessage("올바르지 않은 이름 입력");
            return;
        }
        if(Validator.isValidPhoneNumber(phoneNumber) == false) {
            showMessage("올바르지 않은 전화번호 입력");
            return;
        }

        if(Validator.isEmpty(department)) {
            showMessage("학과를 선택해주세요");
            return;
        }

        if(Validator.isZero(grade)) {
            showMessage("학년을 선택해주세요");
            return;
        }

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.CREATE, Protocol.STUDENT);
        protocol.addBodyStringData(name.getBytes());
        protocol.addBodyStringData(pw.getBytes());
        protocol.addBodyStringData(phoneNumber.getBytes());
        protocol.addBodyStringData(studentID.getBytes());
        protocol.addBodyStringData(department.getBytes());
        protocol.addBodyIntData(grade);

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

            showMessage("학번 또는 전화번호 중복");
            return;

        }

        else {

            int userID = Connector.readInt();
            name = Connector.readString();
            pw = Connector.readString();
            phoneNumber = Connector.readString();
            studentID = Connector.readString();
            String departmentName = Connector.readString();
            int grade = Connector.readInt();

            Student student = new Student(
                    userID,
                    name,
                    pw,
                    phoneNumber,
                    studentID,
                    departmentName,
                    grade
            );

            System.out.println(student);

            parentController.parentController.showMessage("학생 추가 성공");
            Stage dialog = (Stage) addBtn.getScene().getWindow();
            dialog.close();


        }

    }







}
