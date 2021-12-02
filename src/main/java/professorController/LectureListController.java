package professorController;

import professorController.LectureListItemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.Connector;
import network.Protocol;
import persistence.Entity.LectureTimeTable;
import persistence.Entity.OpeningSubject;
import persistence.Entity.Student;
import persistence.Enum.Day;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LectureListController implements Initializable {

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

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.PROFESSOR_TIME_TABLE);
        protocol.addBodyStringData("lectureList".getBytes());
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
        }

        else {

            parentController.showMessage("담당 강의 정보를 로드하였습니다");
            readOpeningSubject();

        }

    }

    private void readOpeningSubject() {

        int count = Connector.readInt();

        LectureListItemController tmpCon = new LectureListItemController();
        OpeningSubject tmpOs = new OpeningSubject();

        for(int i=0; i<count; i++) {

            OpeningSubject os = new OpeningSubject();

            boolean isDuplicated = false;

            os.setOpeningSubjectId(Connector.readInt());
            os.setSubjectCode(Connector.readString());
            os.setDividedClass(Connector.readString());
            os.setSubjectName(Connector.readString());
            os.setGrade(Connector.readInt());
            os.setCredit(Connector.readInt());
            os.setProfessorName(Connector.readString());

            System.out.println("new OS id : " + os.getOpeningSubjectId());
            System.out.println("prev OS id : " + tmpOs.getOpeningSubjectId());


            if(os.getOpeningSubjectId() == tmpOs.getOpeningSubjectId()){
                isDuplicated = true;
            }


            LectureTimeTable time = new LectureTimeTable();
            String d = Connector.readString();
            Day day = Day.getValue(d);

            int startPeriod = Connector.readInt();
            int closePeriod = Connector.readInt();
            time.setDay(day);
            time.setStartPeriod(startPeriod);
            time.setClosePeriod(closePeriod);
            time.setLectureRoomNumber(Connector.readString());

            if(isDuplicated) {
                tmpCon.getLecture().getLttList().add(time);
                tmpCon.setText();
            }

            else {
                os.getLttList().add(time);
            }


            os.setRegistered(Connector.readInt());
            os.setCapacity(Connector.readInt());

            if(isDuplicated == false) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/professor/lectureListItem.fxml"));
                VBox item = null;

                try { item = fxmlLoader.load(); }
                catch (IOException e) { e.printStackTrace(); }

                LectureListItemController con = fxmlLoader.getController();
                con.setLectureListController(this);
                con.setLecture(os);
                con.setText();
                listBox.getChildren().add(item);

                tmpOs = os;
                tmpCon = con;

            }
        }

    }



}
