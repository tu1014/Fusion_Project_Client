package persistence.Entity;

public class AdminDTO extends UserDTO {

    private String adminId;


    public String getAdminId() {
        return adminId;
    }
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public AdminDTO(long userId, String name, String password, String phoneNumber, String adminId) {
        super(userId, name, password, phoneNumber);
        this.adminId = adminId;
    }

    // main test에서 사용
    public AdminDTO(String name, String password, String phoneNumber, String adminId) {
        super(name, password, phoneNumber);
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "AdminDTO{" +
                "userId=" + userId +
                ", adminId=" + adminId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
