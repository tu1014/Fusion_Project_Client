package studentController;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import persistence.Entity.Subject;


public class SubjectListItemController {

    @FXML Label subjectCode;
    @FXML Label subjectName;
    @FXML Label grade;
    @FXML Label semester;
    @FXML Label credit;

    private Subject subject;
    private studentController.SubjectListController parent;

    public void setSubjectListController(SubjectListController con) {
        this.parent = con;
    }

    public void setSubject(Subject subject) { this.subject = subject; }
    public void setText() {
        subjectCode.setText(subject.getSubjectCode());
        subjectName.setText(subject.getSubjectName());
        semester.setText(Integer.toString(subject.getSemester()) + "학기");
        credit.setText(Integer.toString(subject.getCredit()) + "학점");
        grade.setText(Integer.toString(subject.getTargetGrade()) + "학년");
    }

    @FXML
    private void delete(ActionEvent event) {


    }



}
