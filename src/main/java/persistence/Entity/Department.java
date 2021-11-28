package persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Department {

    private int departmentId;
    private String departmentName;

    public Department() {}

    public Department(int departmentId, String departmentName) {

        this.departmentId = departmentId;
        this.departmentName = departmentName;

    }

}
