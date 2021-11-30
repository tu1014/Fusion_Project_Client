package studentController;


import javafx.event.ActionEvent;
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


public class MyRegistrationListItemController implements Initializable {

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

    @FXML Label code;
    @FXML Label name;
    @FXML Label credit;
    @FXML Label grade;
    @FXML Label professor;
    @FXML Label time;
    @FXML Label registered;
    @FXML Label capacity;

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
        code.setText(lecture.getSubjectCode() + "-" + lecture.getDividedClass());
        name.setText(lecture.getSubjectName());
        time.setText(lecture.getTime().toString());
    }

    @FXML
    private void delete(ActionEvent event) {

        // studentListController.vBox.getChildren().remove(index);
        // System.out.println(event.getSource().);


    }

    @FXML
    private void register() {

        String studentId = parent.parentController.currentUser.getStudentId();
        String code = lecture.getSubjectCode() + "-" + lecture.getDividedClass();

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.CREATE, Protocol.REGISTRATION);

        protocol.addBodyStringData(code.getBytes());
        protocol.addBodyStringData(studentId.getBytes());

        try {
            os.write(protocol.getPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Connector.read();

        byte[] header = Connector.getHeader();

        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
            String message = Connector.readString();
            parent.parentController.showMessage(message);
            // return;
        }

        else {

            parent.parentController.showMessage("수강 신청에 성공하였습니다.");
            // parent.search();

        }




    }


}
