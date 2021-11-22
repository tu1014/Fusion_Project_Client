package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LogoutDialogController {

    @FXML AnchorPane anchor;

    @FXML
    private void exit(ActionEvent event) throws IOException {

        System.out.println("로그아웃 합니다.");

        Stage dialog = (Stage) anchor.getScene().getWindow();
        Stage rootWindow = (Stage) dialog.getOwner();
        dialog.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/authentication.fxml"));
        Parent root = fxmlLoader.load();
        rootWindow.setScene(new Scene(root));

        AuthenticationController authenticationController = fxmlLoader.getController();
        authenticationController.loadSignInBox();

    }

    @FXML
    private void close(ActionEvent event) {

        System.out.println("로그아웃을 취소합니다.");

        Node node = (Node) event.getSource();
        Stage dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();

    }



}
