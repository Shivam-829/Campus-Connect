package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.enums.RequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReputationResponse {

    private long userIdOfUserWhoGave;
    private long getUserIdOfUserWhoGot;
    private String comment;
    private double rating;

}
