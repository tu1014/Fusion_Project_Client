package adminController;

import Validator.Validator;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Connector;
import network.Protocol;
import persistence.Entity.Department;
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

public class AddDepartmentBoxController implements Initializable {

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

    @FXML Button addBtn;
    @FXML TextField departmentName;
    @FXML HBox messageBox;
    @FXML Label message;

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
    private void addDepartment() throws IOException {

        String departmentName = this.departmentName.getText();

        if(Validator.isEmpty(departmentName)) {
            showMessage("학과명을 입력해주세요");
            return;
        }

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.CREATE, Protocol.DEPARTMENT);
        protocol.addBodyStringData(departmentName.getBytes());

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

            showMessage("학과명 중복");
            return;

        }

        else {

            int id = Connector.readInt();
            departmentName = Connector.readString();

            Department department = new Department(id, departmentName);

            System.out.println(department);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/departmentListItem.fxml"));
            VBox item = fxmlLoader.load();
            DepartmentListItemController con = fxmlLoader.getController();
            con.setDepartmentListController(parentController);
            con.setDepartment(department);
            con.setText();
            parentController.listBox.getChildren().add(item);
            parentController.parentController.showMessage("학과 추가 성공");
            Stage dialog = (Stage) addBtn.getScene().getWindow();
            dialog.close();


        }

    }







}
