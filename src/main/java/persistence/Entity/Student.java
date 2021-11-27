package persistence.Entity;

public class Student extends UserDTO {

    public Student() {}

    public Student(long userId, String name, String password, String phoneNumber, String studentId, String departmentName, int grade) {
        super(userId, name, password, phoneNumber);
        this.studentId = studentId;
        this.departmentName = departmentName;
        this.grade = grade;
    }

    // main test에서 사용
    public Student(String name, String password, String phoneNumber, String studentId, String departmentName, int grade) {
        super(name, password, phoneNumber);
        this.studentId = studentId;
        this.departmentName = departmentName;
        this.grade = grade;
    }

    private String studentId;
    private String departmentName;
    private int grade;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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
                ", departmentId=" + departmentName +
                ", grade=" + grade +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
