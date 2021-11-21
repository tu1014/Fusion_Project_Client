package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController {

    @FXML RadioButton admin, student, professor;
    @FXML TextField id;
    @FXML PasswordField pw;
    @FXML VBox vBox;

    private AuthenticationController parentController;

    @FXML
    private void login(ActionEvent event) {

        String id = this.id.getText();
        String pw = this.pw.getText();
        FXMLLoader fxmlLoader;

        if (admin.isSelected()) { System.out.println("관리자 로그인"); }
        else if (student.isSelected()) { System.out.println("학생 로그인"); }
        else if(professor.isSelected()) { System.out.println("교수 로그인"); }
        else { System.out.println("사용자 유형을 선택해주세요."); }

    }

    @FXML
    private void moveToSignup(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/signUpBox.fxml"));
            VBox vbox = fxmlLoader.load();
            this.vBox.getChildren().setAll(vbox);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
