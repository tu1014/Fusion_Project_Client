package persistence.Entity;

public class UserDTO {

    protected int userId;
    protected String name;
    protected String password;
    protected String phoneNumber;

    public UserDTO() {}

    public UserDTO(int userId, String name, String password, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public UserDTO(String name, String password, String phoneNumber) {
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
