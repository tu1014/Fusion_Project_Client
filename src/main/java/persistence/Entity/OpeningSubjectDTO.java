package persistence.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class OpeningSubjectDTO {

    private long openingSubjectId;
    private int subjectId;
    private int registered;
    private String professorId;
    private String dividedClass;
    private int capacity;
    private LocalDateTime syllabusWriteStart;
    private LocalDateTime syllabusWriteClose;
    private LocalDateTime registerStart;
    private LocalDateTime registerClose;

}
