package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.enums.Authority;
import CollegeHelp.College_Mantra.model.College;
import CollegeHelp.College_Mantra.model.Reputation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse {

    private Long id;

    private Authority authority;

    private String name;

    private String gmail;

    private String phoneNumber;

    private double rating;

    private long numberOfReputations;

    private List<ReputationResponse> reputationResponseList;

    private College college;

    private String profileImg;

}
