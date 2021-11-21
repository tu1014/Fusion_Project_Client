package persistence.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO {

    private int id;
    private String subjectCode;
    private String subjectName;
    private int targetGrade;
    private int semester;
    private int credit;

    public SubjectDTO(String subjectCode, String subjectName, int targetGrade, int semester, int credit){
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.targetGrade = targetGrade;
        this.semester = semester;
        this.credit = credit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTargetGrade() {
        return targetGrade;
    }

    public void setTargetGrade(int targetGrade) {
        this.targetGrade = targetGrade;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "SubjectDTO{" +
                "id=" + id +
                ", subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", targetGrade=" + targetGrade +
                ", semester=" + semester +
                ", credit=" + credit +
                '}';
    }
}