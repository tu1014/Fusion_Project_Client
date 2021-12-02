package adminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import persistence.Entity.Professor;
import persistence.Entity.Student;

public class ProfessorListItemController {

    @FXML Label professorId;
    @FXML Label name;
    @FXML Label department;

    private Professor professor;
    private ProfessorListController parent;

    public void setProfessorListController(ProfessorListController con) {
        this.parent = con;
    }

    public void setProfessor(Professor professor) { this.professor = professor; }
    public void setText() {
        professorId.setText(professor.getProfessorId());
        name.setText(professor.getName());
        department.setText(professor.getDepartmentName());
    }

    @FXML
    private void delete(ActionEvent event) {


    }



}
