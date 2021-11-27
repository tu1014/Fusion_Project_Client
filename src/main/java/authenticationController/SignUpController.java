package authenticationController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.Connector;
import network.Protocol;
import persistence.DTO.AdminDTO;
import persistence.DTO.StudentDTO;
import studentController.StudentMainController;

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

        if (id.length()==0 || pw.length()==0) {
            parentController.showMessage("ID, PW를 다시 확인해주세요");
            System.out.println("공백 오류");
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
