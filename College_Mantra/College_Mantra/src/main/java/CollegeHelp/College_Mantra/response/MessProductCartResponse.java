package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.model.MessProduct;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MessProductCartResponse {

    private Long id;
    private List<MessProductResponse> messProductList;
    private UserResponse messResponse;
    private UserResponse studentResponse;

}
