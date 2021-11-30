package persistence.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.Enum.Day;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class OpeningSubject {

    private int openingSubjectId;
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
    private List<LectureTimeTable> lttList = new ArrayList<>();

    private int registered;
    private int capacity;

    private LocalDateTime syllabusWriteStart;
    private LocalDateTime syllabusWriteClose;
    private LocalDateTime registerStart;
    private LocalDateTime registerClose;

    public String getAllTime() {

        String result = "";

        for(int i=0; i<lttList.size(); i++) {

            if(i != 0) result += "/";
            result += lttList.get(i).toString();
        }

        return result;
    }

}
