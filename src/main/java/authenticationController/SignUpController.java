package authenticationController;

import Validator.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import network.Connector;
import network.Protocol;
import persistence.Entity.AdminDTO;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Socket socket = Connector.getSocket();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

        } catch (IOException e) { e.printStackTrace(); }
    }

    InputStream is;
    OutputStream os;

    @FXML VBox vBox;
    @FXML TextField id;
    @FXML PasswordField pw;
    @FXML TextField phoneNumber;
    @FXML TextField name;

    Protocol protocol = Connector.getProtocol();


    private AuthenticationController parentController;
    void setParentController(AuthenticationController controller) { parentController = controller; }

    @FXML
    private void moveToLogin(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/authentication/signInBox.fxml"));
            VBox vbox = fxmlLoader.load();
            SignInController controller = fxmlLoader.getController();
            controller.setParentController(parentController);
            vBox.getChildren().setAll(vbox);

        }

        catch (IOException e) { e.printStackTrace(); }

    }

    @FXML
    private void signUp(ActionEvent event) {

        String id = this.id.getText();
        String pw = this.pw.getText();
        String name = this.name.getText();
        String phoneNumber = this.phoneNumber.getText();

        if(Validator.isEmpty(id)) {
            parentController.showMessage("id를 입력해주세요.");
            return;
        }
        if(Validator.isEmpty(pw)) {
            parentController.showMessage("비밀번호를 입력해주세요.");
            return;
        }
        if(Validator.isEmpty(name)) {
            parentController.showMessage("올바르지 않은 이름 입력");
            return;
        }
        if(Validator.isValidPhoneNumber(phoneNumber) == false) {
            parentController.showMessage("올바르지 않은 전화번호 입력");
            return;
        }

        protocol.init();
        protocol.setHeader(
                Protocol.REQUEST,
                Protocol.CREATE,
                Protocol.ADMIN
        );
        protocol.addBodyStringData(name.getBytes());
        protocol.addBodyStringData(pw.getBytes());
        protocol.addBodyStringData(phoneNumber.getBytes());
        protocol.addBodyStringData(id.getBytes());

        ArrayList<byte[]> packetList = protocol.getAllPacket();

        packetList.stream().forEach(v -> {
            try {
                os.write(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Connector.read();

        byte[] header = Connector.getHeader();

        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {

            parentController.showMessage("ID 또는 전화번호 중복 발생");
            return;

        }

        else {

            int userId = Connector.readInt();
            name = Connector.readString();
            pw = Connector.readString();
            phoneNumber = Connector.readString();
            id = Connector.readString();


            AdminDTO admin = new AdminDTO(userId, name, pw, phoneNumber, id);
            System.out.println(admin);

            parentController.showMessage("회원가입 성공!!");
            moveToLogin(event);



        }

    }









}
