package adminController;

import Validator.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.Connector;
import network.Protocol;
import persistence.Entity.Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentListController implements Initializable {

    InputStream is;
    OutputStream os;
    Protocol protocol;

    @FXML ComboBox<String> filter;
    @FXML TextField keyWord;
    @FXML AnchorPane panel;
    @FXML Button addBtn;
    @FXML VBox listBox;

    AdminMainController parentController;

    void setParentController(AdminMainController con) { parentController = con; }
    
    String searchKeyWord = "";

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
        filter.getItems().add("name");
        filter.getItems().add("student number");
        filter.getItems().add("grade");
        filter.getItems().add("department");

        filter.setOnAction(this::setFilter);
    }

    public void setFilter(ActionEvent event) {

        keyWord.setText("");

        String choice = filter.getValue();
        searchKeyWord = choice;
        parentController.showMessage("검색 필터 : " + choice);


    }

    @FXML
    public void addStudent() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/addStudentBox.fxml"));
        AnchorPane dialogBox = fxmlLoader.load();

        Stage dialogStage = new Stage();
        Scene scene = new Scene(dialogBox);
        dialogStage.setScene(scene);
        dialogStage.initOwner(panel.getScene().getWindow());
        dialogStage.initModality(Modality.WINDOW_MODAL);

        dialogStage.show();

        AddStudentBoxController addStudentBoxController = fxmlLoader.getController();
        addStudentBoxController.setParentController(this);

    }

    String department = "";
    String studentId = "";
    String name = "";
    int grade = 0;

    private void initKey() {
        department = "";
        studentId = "";
        name = "";
        grade = 0;
    }


    @FXML
    public void search() throws IOException {

        listBox.getChildren().clear();

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.STUDENT);

        String input = keyWord.getText();

        initKey();

        // 필터 설정에 따라 숫자 넣어줘야 한다
        if (searchKeyWord.equals("department")) {
            department = input;
        }

        if (searchKeyWord.equals("grade")) {
            grade = Integer.parseInt(input);
        }

        if (searchKeyWord.equals("name")) {
            name = input;
        }

        if (searchKeyWord.equals("student number")) {
            studentId = input;
        }

        protocol.addBodyStringData(name.getBytes());
        protocol.addBodyStringData(studentId.getBytes());
        protocol.addBodyIntData(grade);
        protocol.addBodyStringData(department.getBytes());

        os.write(protocol.getPacket());

        Connector.read();

        byte[] header = Connector.getHeader();
        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
            parentController.showMessage("학생이 존재하지 않습니다.");
            // return;
        }

        else {

            parentController.showMessage("학생 정보를 로드하였습니다");

            /*if(header[Protocol.INDEX_FRAG] == Protocol.USED) {}

            else {
                readStudent();
            }*/

            readStudent();

        }
    }

    public void readStudent() {

        int count = Connector.readInt();

        for(int i=0; i<count; i++) {
            int userId = Connector.readInt();
            String name = Connector.readString();
            String password = Connector.readString();
            String phoneNumber = Connector.readString();
            String studentId = Connector.readString();
            String department = Connector.readString();
            int grade = Connector.readInt();

            Student student = new Student(
                    userId,
                    name,
                    password,
                    phoneNumber,
                    studentId,
                    department,
                    grade
            );

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/studentListItem.fxml"));
            VBox item = null;

            try { item = fxmlLoader.load(); }
            catch (IOException e) { e.printStackTrace(); }

            StudentListItemController studentItemController = fxmlLoader.getController();
            studentItemController.setStudentListController(this);
            studentItemController.setStudent(student);
            studentItemController.setText();
            listBox.getChildren().add(item);
        }
    }



}
