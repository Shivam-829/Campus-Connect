package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.enums.RelationStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class RelationResponse {

    private Long id;
    private int rent;
    private RoomResponse roomResponse;
    private UserResponse student;
    private UserResponse roomHonour;
    private RelationStatus status;
    private Date createTime;

}
