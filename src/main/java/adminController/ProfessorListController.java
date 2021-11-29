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
        if(choice.equals("No Filter")) searchKeyWord = "";

        else {
            searchKeyWord = choice;
            parentController.showMessage("검색 필터 : " + choice);
        }

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
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.CREATE);

        // 필터 설정에 따라 숫자 넣어줘야 한다
        if(searchKeyWord.length() == 0) protocol.addBodyIntData(0);
        else {

            protocol.addBodyIntData(1); // 검색 필터 개수
            protocol.addBodyStringData(searchKeyWord.getBytes());

            String input = keyWord.getText();

            if (searchKeyWord.equals("grade")) {
                // 숫자 맞는지 확인 필요
                if (Validator.isValidGrade(input)) protocol.addBodyIntData(Integer.parseInt(input));
                else {
                    parentController.showMessage("올바른 학년을 입력하세요");
                    return;
                }
            }

            else protocol.addBodyStringData(input.getBytes());

        }

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

            // readStudent();

        }
    }



}
