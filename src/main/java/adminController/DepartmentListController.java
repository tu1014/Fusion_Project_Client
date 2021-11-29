package adminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import network.Connector;
import network.Protocol;
import persistence.Entity.Department;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    @FXML AnchorPane panel;
    @FXML VBox listBox;
    Protocol protocol;
    InputStream is;
    OutputStream os;

    AdminMainController parentController;

    void setParentController(AdminMainController con) { parentController = con; }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Socket socket = Connector.getSocket();
        protocol = Connector.getProtocol();

        try {

            is = socket.getInputStream();
            os = socket.getOutputStream();

            readDepartment();

        } catch (IOException e) { e.printStackTrace(); }
    }



    private void readDepartment() throws IOException {

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.READ, Protocol.DEPARTMENT);
        os.write(protocol.getPacket());

        Connector.read();

        byte[] header = Connector.getHeader();
        if(header[Protocol.INDEX_CODE] == Protocol.FAIL) {

            parentController.showMessage("학과가 존재하지 않습니다.");
            // return;

        }

        else {

            int count = Connector.readInt();

            for(int i=0; i<count; i++) {

                int id = Connector.readInt();
                String name = Connector.readString();
                Department department = new Department(id, name);

                System.out.println(department);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/admin/departmentListItem.fxml"));
                VBox item = loader.load();
                DepartmentListItemController con = loader.getController();
                con.setDepartmentListController(this);
                con.setDepartment(department);
                con.setText();
                listBox.getChildren().add(item);

            }
        }

    }



}
