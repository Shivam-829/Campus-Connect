package CollegeHelp.College_Mantra.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServiceTransactionResponse {

    private String transactionId;
    private UserResponse studentResponse;
    private UserResponse serviceProviderResponse;
    private RoomServiceResponse roomServiceResponse;

}
