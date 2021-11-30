package persistence.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.Enum.Day;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
public class OpeningSubject {

    private long openingSubjectId;
    private int subjectId;
    private String subjectCode;
    private String dividedClass;
    private String subjectName;

    private int grade;
    private int semester;
    private int credit;

    private String professorId;
    private String professorName;

    private LectureTimeTable time;

    private int registered;
    private int capacity;

    private LocalDateTime syllabusWriteStart;
    private LocalDateTime syllabusWriteClose;
    private LocalDateTime registerStart;
    private LocalDateTime registerClose;

}
