package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.enums.ServiceType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomServiceResponse {

    private long id;
    private ServiceType serviceType;
    private int price;
    private UserResponse serviceProviderResponse;

}
