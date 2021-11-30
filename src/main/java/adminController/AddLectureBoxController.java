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
import persistence.Entity.LectureTimeTable;
import persistence.Entity.Professor;
import persistence.Entity.Subject;
import persistence.Enum.Day;

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

    @FXML VBox listBox;

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

    Map<String, Integer> subjectMap;
    Map<String, Integer> roomNumberMap;

    String professorId = "";
    int subjectId = 0;
    int roomNumberId = 0;
    String roomNum = "";
    int sp = 0;
    int cp = 0;
    String d = "";
    String divided = "";
    ArrayList<LectureTimeTable> list = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        subjectMap = new HashMap<>();
        roomNumberMap = new HashMap<>();

        protocol = Connector.getProtocol();

        Socket socket = Connector.getSocket();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

            protocol.init();
            protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.PROFESSOR);

            protocol.addBodyStringData("".getBytes());
            protocol.addBodyStringData("".getBytes());
            protocol.addBodyStringData("".getBytes());

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

                    professor.getItems().add(pf.getProfessorId() + " " + pf.getName());

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
                    subject.getItems().add(s.getSubjectCode() + " " + s.getSubjectName());

                }

                subject.setOnAction(this::setSubjectId);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        startPeriod.getItems().add("1교시");
        startPeriod.getItems().add("2교시");
        startPeriod.getItems().add("3교시");
        startPeriod.getItems().add("4교시");
        startPeriod.getItems().add("5교시");
        startPeriod.getItems().add("6교시");
        startPeriod.getItems().add("7교시");
        startPeriod.getItems().add("8교시");
        startPeriod.setOnAction(this::setStartPeriod);

        closePeriod.getItems().add("1교시");
        closePeriod.getItems().add("2교시");
        closePeriod.getItems().add("3교시");
        closePeriod.getItems().add("4교시");
        closePeriod.getItems().add("5교시");
        closePeriod.getItems().add("6교시");
        closePeriod.getItems().add("7교시");
        closePeriod.getItems().add("8교시");
        closePeriod.setOnAction(this::setClosePeriod);

        roomNumber.getItems().add("D324");
        roomNumber.getItems().add("D327");
        roomNumber.getItems().add("D329");
        roomNumber.getItems().add("D330");
        roomNumber.getItems().add("D331");
        roomNumber.setOnAction(this::setRoomNumberId);

        dividedClass.getItems().add("1분반");
        dividedClass.getItems().add("2분반");
        dividedClass.getItems().add("3분반");
        dividedClass.getItems().add("4분반");
        dividedClass.setOnAction(this::setDivided);

        day.getItems().add("월");
        day.getItems().add("화");
        day.getItems().add("수");
        day.getItems().add("목");
        day.getItems().add("금");
        day.setOnAction(this::setD);



    } // end of initializable

    public void setLectureListController(LectureListController p) {parentController = p;}

    public void setProfessorId(ActionEvent event) {
        String choice = professor.getValue().split(" ")[0];
        professorId = choice;
        System.out.println("professor Id : " + professorId);
    }

    public void setDivided(ActionEvent event) {
        String choice = dividedClass.getValue();

        if(choice.equals("1분반")) divided = "01";
        if(choice.equals("2분반")) divided = "02";
        if(choice.equals("3분반")) divided = "03";
        if(choice.equals("4분반")) divided = "04";

    }

    public void setD(ActionEvent event) {
        String choice = day.getValue();
        d = choice;
    }

    public void setSubjectId(ActionEvent event) {
        String choice = subject.getValue().split(" ")[0];
        int id = subjectMap.get(choice);
        subjectId = id;
        System.out.println("교과목 id : " + subjectId);
    }

    public void setRoomNumberId(ActionEvent event) {
        String choice = roomNumber.getValue();
        int id = 0;
        if(choice.equals("D324")) id = 9;
        if(choice.equals("D327")) id = 7;
        if(choice.equals("D329")) id = 12;
        if(choice.equals("D330")) id = 3;
        if(choice.equals("D331")) id = 8;

        roomNum = choice;
        roomNumberId = id;
    }

    public void setStartPeriod(ActionEvent event) {
        String choice = startPeriod.getValue();
        int rs = 0;
        if(choice.equals("1교시")) rs = 1;
        if(choice.equals("2교시")) rs = 2;
        if(choice.equals("3교시")) rs = 3;
        if(choice.equals("4교시")) rs = 4;
        if(choice.equals("5교시")) rs = 5;
        if(choice.equals("6교시")) rs = 6;
        if(choice.equals("7교시")) rs = 7;
        if(choice.equals("8교시")) rs = 8;
        sp = rs;
    }

    public void setClosePeriod(ActionEvent event) {
        String choice = closePeriod.getValue();
        int rs = 0;
        if(choice.equals("1교시")) rs = 1;
        if(choice.equals("2교시")) rs = 2;
        if(choice.equals("3교시")) rs = 3;
        if(choice.equals("4교시")) rs = 4;
        if(choice.equals("5교시")) rs = 5;
        if(choice.equals("6교시")) rs = 6;
        if(choice.equals("7교시")) rs = 7;
        if(choice.equals("8교시")) rs = 8;
        cp = rs;
    }

    @FXML
    private void addLecture() throws IOException {

        if(Validator.isZero(subjectId)) {
            showMessage("교과목을 선택해주세요");
            return;
        }

        if(Validator.isEmpty(professorId)) {
            showMessage("담당 교수를 선택해주세요");
            return;
        }

        if(Validator.isEmpty(divided)) {
            showMessage("분반을 선택해주세요");
            return;
        }

        String tmp = capacity.getText();
        if(Validator.isDigit(tmp) == false) {
            showMessage("올바른 최대 수강인원을 입력하세요");
            return;
        }

        int cap = Integer.parseInt(tmp);

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.CREATE, Protocol.OPENING_SUBJECT);

        protocol.addBodyIntData(subjectId);
        protocol.addBodyStringData(professorId.getBytes());
        protocol.addBodyStringData(divided.getBytes());
        protocol.addBodyIntData(cap);

        System.out.println("**********************8");
        System.out.println(subjectId);
        System.out.println(professorId);
        System.out.println(divided);
        System.out.println(cap);
        System.out.println("**********************8");

        protocol.addBodyIntData(list.size());

        for(int i=0; i<list.size(); i++) {

            LectureTimeTable ltt = list.get(i);
            protocol.addBodyIntData(ltt.getLectureRoomId());
            protocol.addBodyStringData(ltt.getDay().label().getBytes());
            protocol.addBodyIntData(ltt.getStartPeriod());
            protocol.addBodyIntData(ltt.getClosePeriod());

        }

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
            showMessage("해당 교과목에 대한 분반이 이미 존재합니다");
            // return;
        }

        else {
            parentController.parentController.showMessage("강좌를 개설하였습니다");
            // parentController.search();
            Stage dialog = (Stage) listBox.getScene().getWindow();
            dialog.close();
        }

    }

    @FXML
    private void addTime() throws IOException {

        System.out.println("add Time 실행");
        LectureTimeTable ltt = new LectureTimeTable();

        if (Validator.isZero(roomNumberId)) {
            showMessage("강의실을 선택해주세요");
            return;
        }
        ltt.setLectureRoomId(roomNumberId);
        ltt.setLectureRoomNumber(roomNum);

        if (Validator.isEmpty(d)) {
            showMessage("요일을 선택해주세요");
            return;
        }
        ltt.setDay(Day.getValue(d));

        if (Validator.isZero(sp)) {
            showMessage("시작 교시를 선택해주세요");
            return;
        }
        ltt.setStartPeriod(sp);

        if (Validator.isZero(cp) || cp < sp) {
            showMessage("올바른 종료 교시를 선택해주세요");
            return;
        }
        ltt.setClosePeriod(cp);

        list.add(ltt);
        System.out.println(ltt);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/lttItem.fxml"));
        VBox item = fxmlLoader.load();
        lttItemController con = fxmlLoader.getController();
        con.setLtt(ltt);
        con.setText();
        listBox.getChildren().add(item);
        System.out.println("add Time 종료");

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
