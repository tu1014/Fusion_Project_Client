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
import persistence.Entity.Subject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AddLectureBoxController implements Initializable {

    // 디폴트값, 셋온액션에서 맵 사용하여 값 바꿔주기
    // 그럼 서버에서 학과 이름 검색 후 아이디 넣어주던 부분 필요 없음 여기서 아이디로 보내니까

    InputStream is;
    OutputStream os;

    @FXML HBox messageBox;
    @FXML Label message;

    @FXML ComboBox<String> subject;
    @FXML ComboBox<String> professor;
    @FXML ComboBox<String> dividedClass;
    @FXML TextField capacity;

    @FXML ComboBox<String> roomNumber;
    @FXML ComboBox<String> day;
    @FXML ComboBox<String> startPeriod;
    @FXML ComboBox<String> closePeriod;

    private LectureListController parentController;
    Protocol protocol;

    Map<String, Integer> professorMap;
    Map<String, Integer> subjectMap;
    Map<String, Integer> roomNumberMap;

    String professorId = "";
    int subjectId = 0;
    int roomNumberId = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        professorMap = new HashMap<>();
        subjectMap = new HashMap<>();
        roomNumberMap = new HashMap<>();

        protocol = Connector.getProtocol();

        Socket socket = Connector.getSocket();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

            protocol.init();
            protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.PROFESSOR);
            os.write(protocol.getPacket());

            Connector.read();

            byte[] header = Connector.getHeader();
            if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {

                showMessage("교수가 존재하지 않습니다.");
                // return;

            }

            else {

                int count = Connector.readInt();
                System.out.println("size : " + count);

                for(int i=0; i<count; i++) {
                    int userId = Connector.readInt();
                    String name = Connector.readString();
                    String password = Connector.readString();
                    String phoneNumber = Connector.readString();
                    String department = Connector.readString();
                    String pfId = Connector.readString();

                    Professor pf = new Professor(
                            userId,
                            name,
                            password,
                            phoneNumber,
                            department,
                            pfId
                    );

                    professorMap.put(pf.getProfessorId(), pf.getUserId());
                    professor.getItems().add(pf.getProfessorId());

                }

            }

            professor.setOnAction(this::setProfessorId);

            protocol.init();
            protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.SUBJECT);

            protocol.addBodyStringData("".getBytes());
            protocol.addBodyStringData("".getBytes());
            protocol.addBodyIntData(0);

            os.write(protocol.getPacket());

            Connector.read();

            header = Connector.getHeader();
            if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
                showMessage("교과목이 존재하지 않습니다.");
                // return;
            }

            else {

                showMessage("교과목 정보를 로드하였습니다");

                int count = Connector.readInt();

                for (int i = 0; i < count; i++) {
                    int id = Connector.readInt();
                    String code = Connector.readString();
                    String name = Connector.readString();
                    int grade = Connector.readInt();
                    int semester = Connector.readInt();
                    int credit = Connector.readInt();

                    Subject s = new Subject(id, code, name, grade, semester, credit);

                    subjectMap.put(s.getSubjectCode(), s.getId());
                    subject.getItems().add(s.getSubjectCode());

                }

                subject.setOnAction(this::setSubjectId);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    } // end of initializable

    public void setLectureListController(LectureListController p) {parentController = p;}

    public void setProfessorId(ActionEvent event) {
        String choice = professor.getValue();
        professorId = choice;
    }

    public void setSubjectId(ActionEvent event) {
        String choice = subject.getValue();
        int id = subjectMap.get(choice);
        subjectId = id;
    }

    @FXML
    private void addLecture() throws IOException {

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







}
