package persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegistrationDTO {

    private int registrationId;
    private int openingSubjectId;
    private String studentId;

}
