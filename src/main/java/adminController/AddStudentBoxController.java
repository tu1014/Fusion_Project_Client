package adminController;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Connector;
import network.Protocol;
import persistence.DTO.AdminDTO;
import persistence.DTO.StudentDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddStudentBoxController implements Initializable {

    InputStream is;
    OutputStream os;

    private StudentListController parentController;
    Protocol protocol = Connector.getProtocol();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        departmentBox.getItems().add("컴퓨터소프트웨어공학과");
        departmentBox.getItems().add("기계공학과");
        departmentBox.getItems().add("인공지능학과");
        departmentBox.setOnAction(this::setDepartment);

        gradeBox.getItems().add("1학년");
        gradeBox.getItems().add("2학년");
        gradeBox.getItems().add("3학년");
        gradeBox.getItems().add("4학년");
        departmentBox.setOnAction(this::setGrade);

        Socket socket = Connector.getSocket();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

        } catch (IOException e) { e.printStackTrace(); }

    }

    public void setGrade(ActionEvent event) {
        String choice = departmentBox.getValue();
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

    String department = "컴퓨터소프트웨어공학과";
    int grade = 1;

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
            int departmentId = Connector.readInt();
            int grade = Connector.readInt();

            StudentDTO studentDTO = new StudentDTO(
                    userID,
                    name,
                    pw,
                    phoneNumber,
                    studentID,
                    departmentId,
                    grade
            );

            System.out.println(studentDTO);
            /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/studentItem.fxml"));
            HBox item = fxmlLoader.load();
            StudentItemController studentItemController = fxmlLoader.getController();
            studentItemController.setStudentListController(parentController);
            studentItemController.id.setText(newStd.getStdNumber());
            studentItemController.name.setText(newStd.getName());
            parentController.vBox.getChildren().add(item);*/

            parentController.parentController.showMessage("학생 추가 성공");
            Stage dialog = (Stage) addBtn.getScene().getWindow();
            dialog.close();


        }

    }







}
