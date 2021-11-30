package professorController;

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

    @FXML AnchorPane panel;
    @FXML VBox listBox;

    ProfessorMainController parentController;

    InputStream is;
    OutputStream os;
    Protocol protocol;

    void setParentController(ProfessorMainController con) { parentController = con; }

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
    public void search() {

        listBox.getChildren().clear();

        // initKey();

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.PROFESSOR_TIME_TABLE);

        protocol.addBodyStringData("timeTable".getBytes());
        protocol.addBodyStringData(parentController.currentUser.getProfessorId().getBytes());

        try {
            os.write(protocol.getPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Connector.read();

        byte[] header = Connector.getHeader();
        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
            parentController.showMessage("담당 강의가 존재하지 않습니다.");
            // return;
        }

        else {

            parentController.showMessage("강의시간표를 로드하였습니다");
            readTimeTable();

        }

    }

    private void readTimeTable() {

        int count = Connector.readInt();

        for(int i=0; i<count; i++) {

            String subjectName = Connector.readString();
            String subjectCode = Connector.readString();
            String dividedClass = Connector.readString();
            String d = Connector.readString();
            Day day = Day.getValue(d);
            int startPeriod = Connector.readInt();
            int closePeriod = Connector.readInt();
            String roomNumber = Connector.readString();

            LectureTimeTable ltt = new LectureTimeTable();
            ltt.setDay(day);
            ltt.setStartPeriod(startPeriod);
            ltt.setClosePeriod(closePeriod);
            ltt.setLectureRoomNumber(roomNumber);

            OpeningSubject os = new OpeningSubject();
            os.setSubjectName(subjectName);
            os.setSubjectCode(subjectCode);
            os.setDividedClass(dividedClass);
            os.setTime(ltt);


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/professor/timeTableItem.fxml"));
            VBox item = null;

            try { item = fxmlLoader.load(); }
            catch (IOException e) { e.printStackTrace(); }

            TimeTableItemController con = fxmlLoader.getController();
            con.setTimeTableController(this);
            con.setLecture(os);
            con.setText();
            listBox.getChildren().add(item);






        }

    }



}
