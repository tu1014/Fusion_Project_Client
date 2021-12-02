package authenticationController;

import adminController.AdminMainController;
import professorController.ProfessorMainController;
import studentController.StudentMainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.Connector;
import network.Protocol;
import persistence.Entity.AdminDTO;
import persistence.Entity.Professor;
import persistence.Entity.Student;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    InputStream is;
    OutputStream os;

    @FXML RadioButton admin, student, professor;
    @FXML TextField id;
    @FXML PasswordField pw;
    @FXML VBox vBox;

    Protocol protocol = Connector.getProtocol();

    private AuthenticationController parentController;

    void setParentController(AuthenticationController controller) { parentController = controller; }

    private void adminLogin(String id, String pw) throws IOException {

        FXMLLoader fxmlLoader;
        System.out.println("관리자 로그인 시도");
        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.LOGIN, Protocol.ADMIN);

        protocol.addBodyStringData(id.getBytes());
        protocol.addBodyStringData(pw.getBytes());
        protocol.setBodyLength();

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

            parentController.showMessage("ID가 존재하지 않거나 PW가 틀렸습니다.");
            return;

        }

        else {

            int userId = Connector.readInt();
            String name = Connector.readString();
            String password = Connector.readString();
            String phoneNumber = Connector.readString();
            String adminId = Connector.readString();

            AdminDTO adminDTO = new AdminDTO(userId, name, password, phoneNumber, adminId);

            fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/adminMain.fxml"));
            Parent mainPage = fxmlLoader.load();
            AdminMainController con = fxmlLoader.getController();
            con.setCurrentUser(adminDTO);

            Stage stage = (Stage) vBox.getParent().getScene().getWindow();
            stage.setScene(new Scene(mainPage));

        }

    }

    private void studentLogin(String id, String pw) throws IOException {

        FXMLLoader fxmlLoader;
        System.out.println("학생 로그인 시도");
        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.LOGIN, Protocol.STUDENT);

        protocol.addBodyStringData(id.getBytes());
        protocol.addBodyStringData(pw.getBytes());
        protocol.setBodyLength();

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

            parentController.showMessage("ID가 존재하지 않거나 PW가 틀렸습니다.");
            return;

        }

        else {

            int userId = Connector.readInt();
            String name = Connector.readString();
            String password = Connector.readString();
            String phoneNumber = Connector.readString();
            String studentId = Connector.readString();
            String departmentName = Connector.readString();
            int grade = Connector.readInt();

            Student student = new Student(userId, name, password, phoneNumber, studentId, departmentName, grade);
            System.out.println(student);

            fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/student/studentMain.fxml"));
            Parent mainPage = fxmlLoader.load();
            StudentMainController con = fxmlLoader.getController();
            con.setCurrentUser(student);

            Stage stage = (Stage) vBox.getParent().getScene().getWindow();
            stage.setScene(new Scene(mainPage));

        }

    }

    private void professorLogin(String id, String pw) throws IOException {

        FXMLLoader fxmlLoader;
        System.out.println("교수 로그인 시도");
        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.LOGIN, Protocol.PROFESSOR);

        protocol.addBodyStringData(id.getBytes());
        protocol.addBodyStringData(pw.getBytes());
        protocol.setBodyLength();

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

            parentController.showMessage("ID가 존재하지 않거나 PW가 틀렸습니다.");
            return;

        }

        else {

            int userId = Connector.readInt();
            String name = Connector.readString();
            String password = Connector.readString();
            String phoneNumber = Connector.readString();
            String departmentName = Connector.readString();
            String professorId = Connector.readString();

            Professor professor = new Professor(userId, name, password, phoneNumber, departmentName, professorId);
            System.out.println(professor);

            fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/professor/professorMain.fxml"));
            Parent mainPage = fxmlLoader.load();
            ProfessorMainController con = fxmlLoader.getController();
            con.setCurrentUser(professor);

            Stage stage = (Stage) vBox.getParent().getScene().getWindow();
            stage.setScene(new Scene(mainPage));

        }

    }


    @FXML
    private void login(ActionEvent event) {

        String id = this.id.getText();
        String pw = this.pw.getText();
        FXMLLoader fxmlLoader;

        try {

            if (admin.isSelected()) { adminLogin(id, pw); }
            else if (student.isSelected()) { studentLogin(id, pw); }
            else if(professor.isSelected()) { professorLogin(id, pw); }
            else {
                parentController.showMessage("사용자 유형을 선택해주세요");
                System.out.println("사용자 유형을 선택해주세요.");
            }

        }

        catch(IOException e) { e.printStackTrace(); }

    }

    @FXML
    private void moveToSignup(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/authentication/signUpBox.fxml"));
            VBox vbox = fxmlLoader.load();
            SignUpController controller = fxmlLoader.getController();
            controller.setParentController(parentController);
            this.vBox.getChildren().setAll(vbox);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Socket socket = Connector.getSocket();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

        } catch (IOException e) { e.printStackTrace(); }
    }
}
