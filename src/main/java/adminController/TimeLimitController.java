package adminController;

import Validator.Validator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import network.Connector;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TimeLimitController implements Initializable {

    String startDateTime;
    String closeDateTime;
    int selectedGrade;

    AdminMainController parentController;

    InputStream is;
    OutputStream os;
    Protocol protocol;

    @FXML RadioButton grade1;
    @FXML RadioButton grade2;
    @FXML RadioButton grade3;
    @FXML RadioButton grade4;

    @FXML DatePicker registerStartDate;
    @FXML TextField registerStartTime;
    @FXML TextField registerStartMinute;

    @FXML DatePicker registerCloseDate;
    @FXML TextField registerCloseTime;
    @FXML TextField registerCloseMinute;

    @FXML DatePicker syllabusWriteStartDate;
    @FXML TextField syllabusWriteStartTime;
    @FXML TextField syllabusWriteStartMinute;

    @FXML DatePicker syllabusWriteCloseDate;
    @FXML TextField syllabusWriteCloseTime;
    @FXML TextField syllabusWriteCloseMinute;

    void setParentController(AdminMainController con) { parentController = con; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        protocol = Connector.getProtocol();
        Socket socket = Connector.getSocket();

        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
        }

        catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void setRegistrationTime() {

        startDateTime = "";
        closeDateTime = "";

        LocalDate startDate = registerStartDate.getValue();
        LocalDate closeDate = registerCloseDate.getValue();

        if(startDate == null || closeDate == null) {
            parentController.showMessage("날짜를 선택해주세요");
            return;
        }

        startDateTime = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        closeDateTime = closeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(
                Validator.isValidTime(registerStartTime.getText()) == false ||
                        Validator.isValidTime(registerCloseTime.getText()) == false

        ) {
            parentController.showMessage("올바른 시간을 입력해주세요");
            return;
        }

        if(
                Validator.isValidMinute(registerCloseMinute.getText()) == false ||
                        Validator.isValidMinute(registerStartMinute.getText()) == false

        ) {
            parentController.showMessage("올바른 시간을 입력해주세요");
            return;
        }

        int targetGrade = 0;
        if(grade1.isSelected()) {
            targetGrade = 1;
        }
        else if(grade2.isSelected()) {
            targetGrade = 2;
        }
        else if (grade3.isSelected()) {
            targetGrade=3;
        }else if(grade4.isSelected()) {
            targetGrade=4;
        }
        else {
            parentController.showMessage("대상 학년을 선택하세요");
            return;
        }

        startDateTime = startDateTime + "-" + registerStartTime.getText() + "-" + registerStartMinute.getText();
        closeDateTime = closeDateTime + "-" + registerCloseTime.getText() + "-" + registerCloseMinute.getText();

        System.out.println(startDateTime);
        System.out.println(closeDateTime);

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.UPDATE, Protocol.OPENING_SUBJECT);

        protocol.addBodyStringData("registerTime".getBytes());
        protocol.addBodyIntData(targetGrade);
        protocol.addBodyStringData(startDateTime.getBytes());
        protocol.addBodyStringData(closeDateTime.getBytes());

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
            parentController.showMessage("변경에 실패하였습니다");
        }
        else {
            parentController.showMessage("수강신청 기간을 설정하였습니다");
        }

    }

    @FXML
    private void setSyllabusWriteTime() {

        startDateTime = "";
        closeDateTime = "";

        LocalDate startDate = syllabusWriteStartDate.getValue();
        LocalDate closeDate = syllabusWriteCloseDate.getValue();

        if(startDate == null || closeDate == null) {
            parentController.showMessage("날짜를 선택해주세요");
            return;
        }

        startDateTime = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        closeDateTime = closeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(
                Validator.isValidTime(syllabusWriteStartTime.getText()) == false ||
                        Validator.isValidTime(syllabusWriteCloseTime.getText()) == false

        ) {
            parentController.showMessage("올바른 시간을 입력해주세요");
            return;
        }

        if(
                Validator.isValidMinute(syllabusWriteStartMinute.getText()) == false ||
                        Validator.isValidMinute(syllabusWriteCloseMinute.getText()) == false

        ) {
            parentController.showMessage("올바른 시간을 입력해주세요");
            return;
        }

        startDateTime = startDateTime + "-" + syllabusWriteStartTime.getText() + "-" + syllabusWriteStartMinute.getText();
        closeDateTime = closeDateTime + "-" + syllabusWriteCloseTime.getText() + "-" + syllabusWriteCloseMinute.getText();

        System.out.println(startDateTime);
        System.out.println(closeDateTime);

        protocol.init();
        protocol.setHeader(Protocol.REQUEST, Protocol.UPDATE, Protocol.OPENING_SUBJECT);

        protocol.addBodyStringData("syllabusWriteTime".getBytes());
        protocol.addBodyStringData(startDateTime.getBytes());
        protocol.addBodyStringData(closeDateTime.getBytes());

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
            parentController.showMessage("변경에 실패하였습니다");
        }
        else {
            parentController.showMessage("강의계획서 입력 기간을 설정하였습니다");
        }

    }


}
