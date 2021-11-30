package persistence.Entity;

import lombok.*;
import persistence.Enum.Day;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LectureTimeTable {

    private int lectureTimeTableId;
    private long openingSubjectId;
    private int lectureRoomId;
    private String lectureRoomNumber;
    private Day day;
    private int startPeriod;
    private int closePeriod;

    @Override
    public String toString() {
        String result = "";
        result += day.label();
        result += startPeriod;
        if(startPeriod != closePeriod) result += closePeriod;
        result += " ";
        result += lectureRoomNumber;

        return result;
    }
}
