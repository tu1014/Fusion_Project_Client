package persistence.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StudentTimeTableDTO {

    private int studentTimeTableId;
    private String studentId;
    private int lectureTimeTableId;

}
