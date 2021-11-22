package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfessorListController implements Initializable {

    @FXML ComboBox<String> gradeBox;
    @FXML TextField keyWord;
    @FXML AnchorPane panel;

    AdminMainController parentController;

    void setParentController(AdminMainController con) { parentController = con; }

    String searchDepartment = "";
    String searchKeyWord = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gradeBox.getItems().add("All department");
        gradeBox.getItems().add("컴퓨터소프트웨어공학과");
        gradeBox.getItems().add("기계공학과");
        gradeBox.getItems().add("전자공학부");
        gradeBox.getItems().add("기계시스템공학과");
        gradeBox.setOnAction(this::setSearchGrade);
    }

    public void setSearchGrade(ActionEvent event) {

        String choice = gradeBox.getValue();

        if (choice.equals("All department")) searchDepartment = "";
        else searchDepartment = choice;

        parentController.showMessage("검색 학과 : " + choice);

    }



}
