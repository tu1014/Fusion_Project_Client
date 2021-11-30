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

    @FXML Label name;
    @FXML Label time;

    private OpeningSubject lecture;
    private TimeTableController parent;

    InputStream is;
    OutputStream os;
    Protocol protocol;

    public OpeningSubject getLecture() { return lecture; }

    public void setTimeTableController(TimeTableController con) {
        this.parent = con;
    }

    public void setLecture(OpeningSubject lecture) { this.lecture = lecture; }
    public void setText() {
        name.setText(lecture.getSubjectName());
        time.setText(lecture.getTime().toString());
    }


}
