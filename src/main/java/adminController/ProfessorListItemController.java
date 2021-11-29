package adminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import persistence.Entity.Professor;
import persistence.Entity.Student;

public class ProfessorListItemController {

    @FXML Label studentId;
    @FXML Label name;
    @FXML Label department;

    private Professor professor;
    private ProfessorListController parent;

    public void setProfessorListController(ProfessorListController con) {
        this.parent = con;
    }

    public void setStudent(Professor professor) { this.professor = professor; }
    public void setText() {
        studentId.setText(professor.getProfessorId());
        name.setText(professor.getName());
        department.setText(professor.getDepartmentName());
    }

    @FXML
    private void delete(ActionEvent event) {

        // studentListController.vBox.getChildren().remove(index);
        // System.out.println(event.getSource().);


    }



}
