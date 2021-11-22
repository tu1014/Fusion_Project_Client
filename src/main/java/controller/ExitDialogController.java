package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class ExitDialogController {

    @FXML
    private void exit(ActionEvent event) {

        System.out.println("프로그램을 종료합니다.");

        Node node = (Node) event.getSource();
        Stage dialogStage = (Stage) node.getScene().getWindow();
        Stage parentStage = (Stage) dialogStage.getOwner();
        parentStage.close();

    }

    @FXML
    private void close(ActionEvent event) {

        System.out.println("종료를 취소합니다.");
        
        Node node = (Node) event.getSource();
        Stage dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();

    }


}
