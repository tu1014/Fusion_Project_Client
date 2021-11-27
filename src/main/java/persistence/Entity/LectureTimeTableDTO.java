package persistence.Entity;

import lombok.*;
import persistence.Enum.Day;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LectureTimeTableDTO {

    private int lectureTimeTableId;
    private long openingSubjectId;
    private int lectureRoomId;
    private Day day;
    private int startPeriod;
    private int closePeriod;

}
