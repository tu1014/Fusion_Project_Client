package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import network.Connector;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Socket socket = Connector.getSocket();

        try {

            /*bi = new BufferedInputStream(socket.getInputStream());
            bo = new BufferedOutputStream(socket.getOutputStream());*/
            is = socket.getInputStream();
            os = socket.getOutputStream();

        } catch (IOException e) { e.printStackTrace(); }
    }

    InputStream is;
    OutputStream os;

    @FXML VBox vBox;
    @FXML TextField id;
    @FXML PasswordField pw;

    private AuthenticationController parentController;
    void setParentController(AuthenticationController controller) { parentController = controller; }

    @FXML
    private void moveToLogin(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/signInBox.fxml"));
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

        if (id.length()==0 || pw.length()==0) {
            parentController.showMessage("ID, PW를 다시 확인해주세요");
            System.out.println("공백 오류");
        }

        else moveToLogin(event);

    }









}
