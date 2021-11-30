package professorController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import network.Connector;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class LectureListController implements Initializable {

    @FXML ComboBox<String> gradeBox;
    @FXML ComboBox<String> filter;
    @FXML TextField keyWord;
    @FXML AnchorPane panel;
    @FXML VBox listBox;

    ProfessorMainController parentController;

    InputStream is;
    OutputStream os;
    Protocol protocol;

    void setParentController(ProfessorMainController con) { parentController = con; }

    int searchGrade = 0;
    String searchKeyWord = "";

    private void initKey() {
        grade = 0;
        professorName = "";
        subjectCode = "";
        dividedClass = "";
        subjectName = "";
    }

    int grade = 0;
    String professorName = "";
    String subjectCode = "";
    String dividedClass = "";
    String subjectName = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        protocol = Connector.getProtocol();
        Socket socket = Connector.getSocket();

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
        }

        catch (IOException e) { e.printStackTrace(); }

        filter.getItems().add("No Filter");
        filter.getItems().add("Subject Code");
        filter.getItems().add("Subject Name");
        filter.getItems().add("Professor Name");
        filter.setOnAction(this::setSearchFilter);

        gradeBox.getItems().add("All Grades");
        gradeBox.getItems().add("1학년");
        gradeBox.getItems().add("2학년");
        gradeBox.getItems().add("3학년");
        gradeBox.getItems().add("4학년");
        gradeBox.setOnAction(this::setSearchGrade);
    }

    public void setSearchGrade(ActionEvent event) {

        String choice = gradeBox.getValue();

        if (choice.equals("1학년")) searchGrade = 1;
        else if (choice.equals("2학년")) searchGrade = 2;
        else if (choice.equals("3학년")) searchGrade = 3;
        else if (choice.equals("4학년")) searchGrade = 4;
        else searchGrade = 0;

        parentController.showMessage("검색 키워드 : " + choice);

    }

    public void setSearchFilter(ActionEvent event) {

        keyWord.setText("");
        String choice = filter.getValue();
        searchKeyWord = choice;

        /*if (choice.equals("교수 이름")) gradeBox.setVisible(true);
        else { gradeBox.setVisible(false); }*/

        parentController.showMessage("검색 필터 : " + choice);

    }

    @FXML
    public void search() {

        listBox.getChildren().clear();

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.OPENING_SUBJECT);

        String input = keyWord.getText();

        initKey();

        if (searchKeyWord.equals("Subject Code")) {
            String[] arr = input.split("-");
            subjectCode = arr[0];
            if (arr.length > 1) dividedClass = arr[1];
        }

        if (searchKeyWord.equals("Subject Name")) {
            subjectName = input;
        }

        if (searchKeyWord.equals("Professor Name")) {
            professorName = input;
        }

        protocol.addBodyIntData(grade);
        protocol.addBodyStringData(professorName.getBytes());
        protocol.addBodyStringData(subjectCode.getBytes());
        protocol.addBodyStringData(dividedClass.getBytes());
        protocol.addBodyStringData(subjectName.getBytes());

        System.out.println(grade);
        System.out.println(professorName);
        System.out.println(subjectCode);
        System.out.println(dividedClass);
        System.out.println(subjectName);

        try {
            os.write(protocol.getPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Connector.read();

        byte[] header = Connector.getHeader();
        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
            parentController.showMessage("개설강좌가 존재하지 않습니다.");
            // return;
        }

        else {

            parentController.showMessage("개설강좌 정보를 로드하였습니다");
            // readStudent();

        }

    }



}
