package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.enums.AvailabilityType;
import CollegeHelp.College_Mantra.enums.RoomType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public class RoomResponse {

    private long id;
    private RoomType roomType;
    private int cost;
    private UserResponse roomHonourResponse;
    private String address;
    private List<String> images;
    private AvailabilityType availabilityType;
    private Date createTime;

}
