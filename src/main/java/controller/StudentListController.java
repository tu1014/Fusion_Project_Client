package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentListController implements Initializable {

    @FXML ComboBox<String> gradeBox;
    @FXML TextField keyWord;
    @FXML AnchorPane panel;

    AdminMainController parentController;

    void setParentController(AdminMainController con) { parentController = con; }

    int searchGrade = 0;
    String searchKeyWord = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gradeBox.getItems().add("All Grades");
        gradeBox.getItems().add("1학년");
        gradeBox.getItems().add("2학년");
        gradeBox.getItems().add("3학년");
        gradeBox.getItems().add("4학년");
        gradeBox.setOnAction(this::setSearchGrade);
    }

    public void setSearchGrade(ActionEvent event) {

        String choice = gradeBox.getValue();

        if (choice.equals("1학년")) searchGrade = 1;
        else if (choice.equals("2학년")) searchGrade = 2;
        else if (choice.equals("3학년")) searchGrade = 3;
        else if (choice.equals("4학년")) searchGrade = 4;
        else searchGrade = 0;

        parentController.showMessage("검색 키워드 : " + choice);

    }



}
