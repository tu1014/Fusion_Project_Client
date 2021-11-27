package adminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfessorListController implements Initializable {

    @FXML ComboBox<String> filter;
    @FXML TextField keyWord;
    @FXML AnchorPane panel;

    AdminMainController parentController;

    void setParentController(AdminMainController con) { parentController = con; }
    
    String searchKeyWord = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filter.getItems().add("department");
        filter.getItems().add("name");
        filter.setOnAction(this::setSearchGrade);
    }

    public void setSearchGrade(ActionEvent event) {

        String choice = filter.getValue();

        searchKeyWord = choice;

        parentController.showMessage("검색 필터 : " + choice);

    }



}
