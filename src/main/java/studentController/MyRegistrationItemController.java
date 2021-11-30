package studentController;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import network.Connector;
import network.Protocol;
import persistence.Entity.OpeningSubject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class MyRegistrationItemController implements Initializable {

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

    @FXML Label name;
    @FXML Label credit;
    @FXML Label code;

    private OpeningSubject lecture;
    private LectureListController parent;

    InputStream is;
    OutputStream os;
    Protocol protocol;

    public OpeningSubject getLecture() { return lecture; }

    public void setLectureListController(LectureListController con) {
        this.parent = con;
    }

    public void setLecture(OpeningSubject lecture) { this.lecture = lecture; }
    public void setText() {
        name.setText(lecture.getSubjectName());
        credit.setText(lecture.getCredit() + "학점");
        code.setText(lecture.getSubjectCode()+"-"+lecture.getDividedClass());
    }

    @FXML
    private void delete() {

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.DELETE, Protocol.REGISTRATION);

        String studentId = parent.parentController.currentUser.getStudentId();

        protocol.addBodyStringData(code.getText().getBytes());
        protocol.addBodyStringData(studentId.getBytes());

        try {
            os.write(protocol.getPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }


        Connector.read();

        byte[] header = Connector.getHeader();

        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
            parent.parentController.showMessage("수강 취소 실패");
        }

        else {

            parent.parentController.showMessage("수강 신청을 취소하였습니다");

        }

        parent.myRegistrationBox.getChildren().clear();
        parent.readMyRegistration();

    }



}
