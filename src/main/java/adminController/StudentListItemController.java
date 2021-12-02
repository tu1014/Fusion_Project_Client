package adminController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import persistence.Entity.Student;


public class StudentListItemController {

    @FXML Label studentId;
    @FXML Label name;
    @FXML Label department;
    @FXML Label grade;

    private Student student;
    private StudentListController parent;

    public void setStudentListController(StudentListController studentListController) {
        this.parent = studentListController;
    }

    public void setStudent(Student std) { this.student = std; }
    public void setText() {
        studentId.setText(student.getStudentId());
        name.setText(student.getName());
        department.setText(student.getDepartmentName());
        grade.setText(student.getGrade() + "학년");
    }

    @FXML
    private void delete(ActionEvent event) {


    }



}
