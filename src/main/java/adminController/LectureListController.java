package adminController;

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
import java.util.List;
import java.util.ResourceBundle;

public class LectureListController implements Initializable {

    @FXML ComboBox<String> gradeBox;
    @FXML ComboBox<String> filter;
    @FXML TextField keyWord;
    @FXML AnchorPane panel;
    @FXML VBox listBox;

    AdminMainController parentController;

    InputStream is;
    OutputStream os;
    Protocol protocol;

    void setParentController(AdminMainController con) { parentController = con; }

    private void initKey() {
        // grade = 0;
        professorName = "";
        subjectCode = "";
        dividedClass = "";
        subjectName = "";
    }

    String searchKeyWord = "";

    int grade;
    String professorName;
    String subjectCode;
    String dividedClass;
    String subjectName;

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

        initKey();
        grade = 0;
    }

    public void setSearchGrade(ActionEvent event) {

        String choice = gradeBox.getValue();
        System.out.println("choice : " + choice);

        if (choice.equals("1학년")) grade = 1;
        else if (choice.equals("2학년")) grade = 2;
        else if (choice.equals("3학년")) grade = 3;
        else if (choice.equals("4학년")) grade = 4;
        else grade = 0;

        System.out.println("grade : " + grade);
        parentController.showMessage("검색 키워드 : " + choice);

    }

    public void setSearchFilter(ActionEvent event) {

        keyWord.setText("");
        initKey();
        String choice = filter.getValue();
        System.out.println("choice : " + choice);

        searchKeyWord = choice;
        System.out.println("searchKeyWord : " + searchKeyWord);

        /*if (choice.equals("교수 이름")) gradeBox.setVisible(true);
        else { gradeBox.setVisible(false); }*/

        parentController.showMessage("검색 필터 : " + choice);

    }

    @FXML
    private void addLecture() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/addLectureBox.fxml"));
        AnchorPane dialogBox = null;
        try {
            dialogBox = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage dialogStage = new Stage();
        Scene scene = new Scene(dialogBox);
        dialogStage.setScene(scene);
        dialogStage.initOwner(panel.getScene().getWindow());
        dialogStage.initModality(Modality.WINDOW_MODAL);

        dialogStage.show();

        AddLectureBoxController con = fxmlLoader.getController();
        con.setLectureListController(this);

    }

    @FXML
    public void search() {

        listBox.getChildren().clear();

        // initKey();

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.OPENING_SUBJECT);

        String input = keyWord.getText();

        System.out.println("*");
        System.out.println("Filter : " + searchKeyWord);
        System.out.println("Input : " + input);
        System.out.println("Grade : " + grade);
        System.out.println("*");

        if (searchKeyWord.equals("No Filter")) {
            // initKey();
            // subjectName = input;
        }

        if (searchKeyWord.equals("Subject Code")) {
            // initKey();
            String[] arr = input.split("-");
            subjectCode = arr[0];
            if (arr.length > 1) dividedClass = arr[1];
        }

        if (searchKeyWord.equals("Subject Name")) {
            // initKey();
            subjectName = input;
        }

        if (searchKeyWord.equals("Professor Name")) {
            // initKey();
            professorName = input;
        }

        protocol.addBodyIntData(grade);
        protocol.addBodyStringData(professorName.getBytes());
        protocol.addBodyStringData(subjectCode.getBytes());
        protocol.addBodyStringData(dividedClass.getBytes());
        protocol.addBodyStringData(subjectName.getBytes());

        System.out.println("===============");
        System.out.println(grade);
        System.out.println(professorName);
        System.out.println(subjectCode);
        System.out.println(dividedClass);
        System.out.println(subjectName);
        System.out.println("===============");

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

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/lectureListItem.fxml"));
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
