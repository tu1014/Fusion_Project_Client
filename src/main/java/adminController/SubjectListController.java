package adminController;

import Validator.Validator;
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
import persistence.Entity.Student;
import persistence.Entity.Subject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class SubjectListController implements Initializable {

    @FXML ComboBox<String> filter;
    @FXML TextField keyWord;
    @FXML AnchorPane panel;
    @FXML VBox listBox;

    AdminMainController parentController;

    void setParentController(AdminMainController con) { parentController = con; }

    String searchKeyWord = "";
    InputStream is;
    OutputStream os;
    Protocol protocol;

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
        filter.getItems().add("grade");
        filter.getItems().add("subject name");
        filter.getItems().add("subject code");
        filter.setOnAction(this::setKeyWord);
    }

    String subjectCode = "";
    String subjectName = "";
    int grade = 0;

    private void initKey() {
        subjectCode = "";
        subjectName = "";
        grade = 0;
    }

    public void setKeyWord(ActionEvent event) {

        keyWord.setText("");
        String choice = filter.getValue();
        searchKeyWord = choice;
        parentController.showMessage("검색 필터 : " + choice);

    }

    @FXML
    private void search() {

        listBox.getChildren().clear();

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.SUBJECT);

        String input = keyWord.getText();

        initKey();

        if (searchKeyWord.equals("subject code")) {
            subjectCode = '%' + input + '%';
        }

        if (searchKeyWord.equals("subject name")) {
            subjectName = '%' + input + '%';
        }

        if (searchKeyWord.equals("grade")) {
            if(Validator.isDigit(input) == false) {
                parentController.showMessage("올바른 학년을 입력하세요");
                return;
            }
            grade = Integer.parseInt(input);
        }

        protocol.addBodyStringData(subjectCode.getBytes());
        protocol.addBodyStringData(subjectName.getBytes());
        protocol.addBodyIntData(grade);

        try {
            os.write(protocol.getPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Connector.read();

        byte[] header = Connector.getHeader();
        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
            parentController.showMessage("교과목이 존재하지 않습니다.");
            // return;
        }

        else {

            parentController.showMessage("교과목 정보를 로드하였습니다");
            readSubject();

        }


    }

    public void readSubject() {

        int count = Connector.readInt();

        for(int i=0; i<count; i++) {
            int id = Connector.readInt();
            String code = Connector.readString();
            String name = Connector.readString();
            int grade = Connector.readInt();
            int semester = Connector.readInt();
            int credit = Connector.readInt();

            Subject subject = new Subject(id, code, name, grade, semester, credit);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/subjectListItem.fxml"));
            VBox item = null;

            try { item = fxmlLoader.load(); }
            catch (IOException e) { e.printStackTrace(); }

            SubjectListItemController con = fxmlLoader.getController();
            con.setSubjectListController(this);
            con.setSubject(subject);
            con.setText();
            listBox.getChildren().add(item);
        }
    }



}
