package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.enums.RequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceRequestResponse {

    private long id;

    private UserResponse studentResponse;

    private UserResponse serviceProviderResponse;

    private RoomServiceResponse roomServiceResponse;

    private RequestStatus requestStatus;

}
