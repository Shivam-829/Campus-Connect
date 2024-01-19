package CollegeHelp.College_Mantra.request;

import CollegeHelp.College_Mantra.model.MessProductCart;
import CollegeHelp.College_Mantra.model.Transaction;
import CollegeHelp.College_Mantra.response.UserResponse;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.json.JSONObject;

import java.io.Serializable;

@Builder
@Data
public class TransactionCreateRequest implements Serializable {

    private String order_id;

    private int amount;

    private Long messId;

    private Long studentId;

    private String description;

    private MessProductCart messProductCart;

    public Transaction to(){
        return Transaction.builder()
                .order_id(this.order_id)
                .amount(this.amount)
                .description(this.description)
                .build();
    }

}
