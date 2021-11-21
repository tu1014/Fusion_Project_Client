package persistence.DTO;

public class ProfessorDTO extends UserDTO {

    private int departmentId;
    private String professorId;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public ProfessorDTO() {}

    public ProfessorDTO(long userId, String name, String password, String phoneNumber, int departmentId, String professorId) {
        super(userId, name, password, phoneNumber);
        this.departmentId = departmentId;
        this.professorId = professorId;
    }

    // main test에서 사용
    public ProfessorDTO(String name, String password, String phoneNumber, int departmentId, String professorId) {
        super(name, password, phoneNumber);
        this.departmentId = departmentId;
        this.professorId = professorId;
    }

    @Override
    public String toString() {
        return "ProfessorDTO{" +
                "departmentId=" + departmentId +
                ", professorId='" + professorId + '\'' +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
