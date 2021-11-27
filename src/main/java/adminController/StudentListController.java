package adminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentListController implements Initializable {

    @FXML ComboBox<String> filter;
    @FXML TextField keyWord;
    @FXML AnchorPane panel;
    @FXML Button addBtn;
    @FXML VBox listBox;

    AdminMainController parentController;

    void setParentController(AdminMainController con) { parentController = con; }
    
    String searchKeyWord = " ";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filter.getItems().add("grade");
        filter.getItems().add("name");
        filter.getItems().add("student number");

        filter.setOnAction(this::setSearchGrade);
    }

    public void setSearchGrade(ActionEvent event) {

        String choice = filter.getValue();
        searchKeyWord = choice;
        parentController.showMessage("검색 필터 : " + choice);

    }

    public void addStudent(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/admin/addStudentBox.fxml"));
        AnchorPane dialogBox = fxmlLoader.load();

        Stage dialogStage = new Stage();
        Scene scene = new Scene(dialogBox);
        dialogStage.setScene(scene);
        dialogStage.initOwner(panel.getScene().getWindow());
        dialogStage.initModality(Modality.WINDOW_MODAL);

        dialogStage.show();

        AddStudentBoxController addStudentBoxController = fxmlLoader.getController();
        addStudentBoxController.setParentController(this);

    }



}
