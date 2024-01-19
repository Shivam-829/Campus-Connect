package CollegeHelp.College_Mantra.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class MessProductCartCreateRequest {

    @NotNull(message = "Add Some Products")
    private List<Long> messProductId;

    @NotNull(message = "Mess Id Should Not Be NULL")
    private Long messId;

}
