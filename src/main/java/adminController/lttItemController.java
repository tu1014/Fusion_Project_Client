package adminController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import persistence.Entity.Department;
import persistence.Entity.LectureTimeTable;

import java.io.IOException;


public class lttItemController {

    @FXML Label day;
    @FXML Label period;
    @FXML Label room;

    private LectureTimeTable ltt;

    public void setLtt(LectureTimeTable ltt) { this.ltt = ltt; }
    public void setText() {
        day.setText(ltt.getDay().label());
        period.setText(
                ltt.getStartPeriod()+"교시 ~ " + ltt.getClosePeriod()+"교시"
        );
        room.setText(ltt.getLectureRoomNumber());
    }

    @FXML
    private void delete() {

    }



}
