package persistence.DTO;

public class StudentDTO extends UserDTO {

    public StudentDTO() {}

    public StudentDTO(long userId, String name, String password, String phoneNumber, String studentId, int departmentId, int grade) {
        super(userId, name, password, phoneNumber);
        this.studentId = studentId;
        this.departmentId = departmentId;
        this.grade = grade;
    }

    // main test에서 사용
    public StudentDTO(String name, String password, String phoneNumber, String studentId, int departmentId, int grade) {
        super(name, password, phoneNumber);
        this.studentId = studentId;
        this.departmentId = departmentId;
        this.grade = grade;
    }

    private String studentId;
    private int departmentId;
    private int grade;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "studentId='" + studentId + '\'' +
                ", departmentId=" + departmentId +
                ", grade=" + grade +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
