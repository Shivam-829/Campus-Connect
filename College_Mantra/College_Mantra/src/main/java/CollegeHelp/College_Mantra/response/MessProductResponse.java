package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.model.Mess;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessProductResponse {

    private long id;

    private String productName;

    private int price;

}
