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

    public void setLectureListController(LectureListController con) {
        this.parent = con;
    }

    public void setLecture(OpeningSubject lecture) { this.lecture = lecture; }
    public void setText() {
        code.setText(lecture.getSubjectCode() + "-" + lecture.getDividedClass());
        name.setText(lecture.getSubjectName());
        grade.setText(Integer.toString(lecture.getGrade()) + "학년");
        credit.setText(Integer.toString(lecture.getCredit()) + "학점");
        time.setText(lecture.getTime().toString());
        professor.setText(lecture.getProfessorName());
        registered.setText("Registered : " + lecture.getRegistered());
        capacity.setText("Capacity : " + lecture.getCapacity());
    }

    @FXML
    private void delete(ActionEvent event) {

        // studentListController.vBox.getChildren().remove(index);
        // System.out.println(event.getSource().);


    }

}
