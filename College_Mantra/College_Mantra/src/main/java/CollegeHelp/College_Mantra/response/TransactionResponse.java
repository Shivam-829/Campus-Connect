package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.model.MessProductCart;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TransactionResponse {

    private String order_id;
    private int amount;
    private UserResponse messResponse;
    private UserResponse studentResponse;
    private String description;
    private Date creationDate;

}
