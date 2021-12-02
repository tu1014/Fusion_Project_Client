package adminController;

import Validator.Validator;
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
import persistence.Entity.Professor;
import persistence.Entity.Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfessorListController implements Initializable {

    @FXML ComboBox<String> filter;
    @FXML TextField keyWord;
    @FXML AnchorPane panel;
    @FXML VBox listBox;

    InputStream is;
    OutputStream os;
    Protocol protocol;

    AdminMainController parentController;

    void setParentController(AdminMainController con) { parentController = con; }
    
    String searchKeyWord = "";

    String pfId;
    String pfName;
    String department;

    private void initKey() {
        pfId = "";
        pfName = "";
        department = "";
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

        filter.getItems().add("No Filter");
        filter.getItems().add("department");
        filter.getItems().add("name");
        filter.getItems().add("professorId");
        filter.setOnAction(this::setSearchKeyWord);
    }

    public void setSearchKeyWord(ActionEvent event) {

        keyWord.setText("");

        String choice = filter.getValue();
        searchKeyWord = choice;
        parentController.showMessage("검색 필터 : " + choice);

    }

    @FXML
    public void addProfessor() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/addProfessorBox.fxml"));
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

        AddProfessorBoxController con = fxmlLoader.getController();
        con.setParentController(this);

    }

    @FXML
    public void search() throws IOException {

        listBox.getChildren().clear();

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.PROFESSOR);

        String input = keyWord.getText();

        initKey();

        if (searchKeyWord.equals("department")) {
            department = input;
        }

        if (searchKeyWord.equals("professorId")) {
            pfId = input;
        }

        if (searchKeyWord.equals("name")) {
            pfName = input;
        }

        protocol.addBodyStringData(pfId.getBytes());
        protocol.addBodyStringData(pfName.getBytes());
        protocol.addBodyStringData(department.getBytes());

        os.write(protocol.getPacket());

        Connector.read();

        byte[] header = Connector.getHeader();
        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
            parentController.showMessage("교수가 존재하지 않습니다.");
            // return;
        }

        else {

            parentController.showMessage("교수 정보를 로드하였습니다");

            readProfessor();

        }
    }

    public void readProfessor() {

        int count = Connector.readInt();
        System.out.println("size : " + count);

        for(int i=0; i<count; i++) {
            int userId = Connector.readInt();
            System.out.println("+++++++ : " + userId);
            String name = Connector.readString();
            String password = Connector.readString();
            String phoneNumber = Connector.readString();
            String department = Connector.readString();
            String pfId = Connector.readString();

            Professor pf = new Professor(
                    userId,
                    name,
                    password,
                    phoneNumber,
                    department,
                    pfId
            );

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/professorListItem.fxml"));
            VBox item = null;

            try { item = fxmlLoader.load(); }
            catch (IOException e) { e.printStackTrace(); }

            ProfessorListItemController con = fxmlLoader.getController();
            con.setProfessorListController(this);
            con.setProfessor(pf);
            con.setText();
            listBox.getChildren().add(item);
        }
    }



}
