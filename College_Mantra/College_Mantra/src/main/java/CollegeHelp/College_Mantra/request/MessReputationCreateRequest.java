package CollegeHelp.College_Mantra.request;

import CollegeHelp.College_Mantra.model.Reputation;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class MessReputationCreateRequest {

    @NotNull(message = "Mess UserId Shouldn't Be Null")
    private Long messUserId;

    @NotBlank(message = "Comment Should not be BLANK")
    private String comment;

    @NotNull(message = "Rating should not be NULL")
    @Min(value = 1,message = "Rating Should Be Greater Than Equal To 1")
    @Max(value = 5,message = "Rating Should Be Less Than Equal To 5")
    private double rating;

    public Reputation to(){
        return Reputation.builder()
                .comment(this.comment)
                .rating(this.rating)
                .build();
    }

}
