package persistence.Entity;

public class Professor extends UserDTO {

    private String departmentName;
    private String professorId;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public Professor() {}

    public Professor(int userId, String name, String password, String phoneNumber, String departmentName, String professorId) {
        super(userId, name, password, phoneNumber);
        this.departmentName = departmentName;
        this.professorId = professorId;
    }

    // main test에서 사용
    public Professor(String name, String password, String phoneNumber, String departmentName, String professorId) {
        super(name, password, phoneNumber);
        this.departmentName = departmentName;
        this.professorId = professorId;
    }

    @Override
    public String toString() {
        return "ProfessorDTO{" +
                "departmentId=" + departmentName +
                ", professorId='" + professorId + '\'' +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
