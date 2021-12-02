package adminController;

import Validator.Validator;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Connector;
import network.Protocol;
import persistence.Entity.Department;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DepartmentDeleteBoxController implements Initializable {

    InputStream is;
    OutputStream os;

    private DepartmentListController parentController;
    Protocol protocol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        protocol = Connector.getProtocol();

        Socket socket = Connector.getSocket();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

        } catch (IOException e) { e.printStackTrace(); }

    }

    public void setParentController(DepartmentListController p) {parentController = p;}


    @FXML
    private void delete(ActionEvent event) throws IOException {

    }

    @FXML
    private void cancle(ActionEvent event) throws IOException {

        Node node = (Node) event.getSource();
        Stage dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();

    }







}
