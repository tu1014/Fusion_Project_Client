package adminController;

import Validator.Validator;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Connector;
import network.Protocol;
import persistence.Entity.Student;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class editCapacityBox implements Initializable {

    InputStream is;
    OutputStream os;

    private LectureListItemController parentController;
    Protocol protocol;

    @FXML Label message;
    @FXML HBox messageBox;
    @FXML TextField capacity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        protocol = Connector.getProtocol();

        Socket socket = Connector.getSocket();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

        } catch (IOException e) { e.printStackTrace(); }

    }


    public void setParentController(LectureListItemController p) {parentController = p;}

    void showMessage(String message) {

        this.message.setText(message);
        messageBox.setVisible(true);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), messageBox);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);
        fadeOut.play();
        fadeOut.setOnFinished( (e) -> { messageBox.setVisible(false); });

    }

    @FXML
    private void edit() throws IOException {

        String cap = capacity.getText();

        if(Validator.isDigit(cap) == false) {
            showMessage("올바른 숫자를 입력하세요");
            return;
        }

        int capacity = Integer.parseInt(cap);

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.UPDATE, Protocol.OPENING_SUBJECT);

        protocol.addBodyStringData("capacity".getBytes());
        int id = parentController.getLecture().getOpeningSubjectId();
        protocol.addBodyIntData(id);

        os.write(protocol.getPacket());

        Connector.read();

        byte[] header = Connector.getHeader();
        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {
            parentController.parent.parentController.showMessage("개설강좌가 존재하지 않습니다.");
            // return;
        }

        else {

            parentController.parent.parentController.showMessage("개설강좌 정보를 로드하였습니다");
            parentController.capacity.setText(Integer.toString(capacity));
            Stage dialog = (Stage) messageBox.getScene().getWindow();
            dialog.close();

        }

    }







}
