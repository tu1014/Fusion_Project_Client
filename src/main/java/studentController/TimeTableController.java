package studentController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import network.Connector;
import network.Protocol;
import persistence.Entity.LectureTimeTable;
import persistence.Entity.OpeningSubject;
import persistence.Enum.Day;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class TimeTableController implements Initializable {

    @FXML VBox myRegistrationBox;

    StudentMainController parentController;

    InputStream is;
    OutputStream os;
    Protocol protocol;

    void setParentController(StudentMainController con) { parentController = con; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        protocol = Connector.getProtocol();
        Socket socket = Connector.getSocket();

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
        }

        catch (IOException e) { e.printStackTrace(); }

    }


    @FXML
    private void readMyRegistration() {

        myRegistrationBox.getChildren().clear();

        String studentId = parentController.currentUser.getStudentId();

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.STUDENT_TIME_TABLE);

        protocol.addBodyStringData(studentId.getBytes());

        try {
            os.write(protocol.getPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Connector.read();

        byte[] header = Connector.getHeader();
        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
            parentController.showMessage("수강 신청 내역이 존재하지 않습니다.");
        }

        else {

            parentController.showMessage("수강 신청 내역 정보를 로드하였습니다");
            readRegistration();

        }


    }


    public void readRegistration() {

        int count = Connector.readInt();

        for(int i=0; i<count; i++) {

            String subjectName = Connector.readString();
            System.out.println("+++" + subjectName);
            String subjectCode = Connector.readString();
            String dividedClass = Connector.readString();
            String roomNumber = Connector.readString();
            String d = Connector.readString();
            System.out.println("************");
            System.out.println(d);
            System.out.println("************");
            Day day = Day.getValue(d);


            int startPeriod = Connector.readInt();
            int closePeriod = Connector.readInt();

            LectureTimeTable ltt = new LectureTimeTable();
            ltt.setLectureRoomNumber(roomNumber);
            ltt.setStartPeriod(startPeriod);
            ltt.setClosePeriod(closePeriod);
            ltt.setDay(day);

            OpeningSubject os = new OpeningSubject();
            os.setSubjectName(subjectName);
            os.setSubjectCode(subjectCode);
            os.setDividedClass(dividedClass);
            os.setTime(ltt);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/student/registrationBoxItem.fxml"));
            VBox item = null;

            try { item = fxmlLoader.load(); }
            catch (IOException e) { e.printStackTrace(); }

            MyRegistrationListItemController con = fxmlLoader.getController();
            con.setTimeTableController(this);
            con.setLecture(os);
            con.setText();
            myRegistrationBox.getChildren().add(item);

        }


    }



}
