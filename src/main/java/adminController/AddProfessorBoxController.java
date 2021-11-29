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
import persistence.Entity.Professor;
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

public class AddProfessorBoxController implements Initializable {

    // 디폴트값, 셋온액션에서 맵 사용하여 값 바꿔주기
    // 그럼 서버에서 학과 이름 검색 후 아이디 넣어주던 부분 필요 없음 여기서 아이디로 보내니까

    InputStream is;
    OutputStream os;

    private ProfessorListController parentController;
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
                // return;

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

    }

    public void setDepartment(ActionEvent event) {
        String choice = departmentBox.getValue();
        department = choice;
    }

    public void setParentController(ProfessorListController p) {parentController = p;}

    @FXML Button addBtn;
    @FXML TextField studentId;
    @FXML TextField name;
    @FXML TextField phoneNumber;
    @FXML PasswordField pw;
    @FXML ComboBox<String> departmentBox;
    @FXML HBox messageBox;
    @FXML Label message;

    String department = "";


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
    private void addProfessor() throws IOException {

        String id = this.studentId.getText();
        String pw = this.pw.getText();
        String name = this.name.getText();
        String phoneNumber = this.phoneNumber.getText();

        if(Validator.isEmpty(id)) {
            showMessage("올바르지 않은 교번 입력");
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

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.CREATE, Protocol.PROFESSOR);
        protocol.addBodyStringData(name.getBytes());
        protocol.addBodyStringData(pw.getBytes());
        protocol.addBodyStringData(phoneNumber.getBytes());
        protocol.addBodyStringData(department.getBytes());
        protocol.addBodyStringData(id.getBytes());

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

            showMessage("교번 또는 전화번호 중복");
            return;

        }

        else {

            // 학생 추가에서 데이터 받을 필요 없다?
            int userID = Connector.readInt();
            name = Connector.readString();
            pw = Connector.readString();
            phoneNumber = Connector.readString();
            String departmentName = Connector.readString();
            id = Connector.readString();

            Professor pf = new Professor(
                    userID,
                    name,
                    pw,
                    phoneNumber,
                    id,
                    departmentName
            );

            System.out.println(pf);

            /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/studentListItem.fxml"));
            VBox item = fxmlLoader.load();
            StudentListItemController studentItemController = fxmlLoader.getController();
            studentItemController.setStudentListController(parentController);
            studentItemController.setStudent(student);
            studentItemController.setText();
            parentController.listBox.getChildren().add(item);*/

            // parentController.search();

            parentController.search();

            parentController.parentController.showMessage("교수 추가 성공");
            Stage dialog = (Stage) addBtn.getScene().getWindow();
            dialog.close();


        }

    }







}
