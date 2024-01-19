package CollegeHelp.College_Mantra.request;

import CollegeHelp.College_Mantra.enums.AvailabilityType;
import CollegeHelp.College_Mantra.enums.RoomType;
import CollegeHelp.College_Mantra.model.Room;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class RoomCreateRequest {

    @NotBlank(message = "Room_Type should not be null")
    private RoomType roomType;

    @NotNull(message = "Room_Cost should not be null")
    private int roomCost;

    @NotBlank(message = "Address should not be null")
    private String address;

    public Room to(){
        return Room.builder()
                .address(this.address)
                .cost(this.roomCost)
                .availabilityType(AvailabilityType.TOO_LATE)
                .roomType(this.roomType)
                .build();
    }

}
