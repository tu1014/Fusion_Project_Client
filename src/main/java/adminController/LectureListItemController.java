package adminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import network.Connector;
import network.Protocol;
import persistence.Entity.OpeningSubject;
import persistence.Entity.Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class LectureListItemController implements Initializable {

    @FXML Label code;
    @FXML Label name;
    @FXML Label credit;
    @FXML Label grade;
    @FXML Label professor;
    @FXML Label time;
    @FXML Label Registered;
    @FXML Label Capacity;

    private OpeningSubject lecture;
    private LectureListController parent;

    InputStream is;
    OutputStream os;
    Protocol protocol;

    public void setLectureListController(LectureListController con) {
        this.parent = con;
    }

    public void setStudent(OpeningSubject lecture) { this.lecture = lecture; }
    public void setText() {
        lecture.setText(lecture.getStudentId());
        name.setText(lecture.getName());
        department.setText(lecture.getDepartmentName());
        grade.setText(lecture.getGrade() + "학년");
    }

    @FXML
    private void delete(ActionEvent event) {

        // studentListController.vBox.getChildren().remove(index);
        // System.out.println(event.getSource().);


    }


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
}
