package CollegeHelp.College_Mantra.request;

import CollegeHelp.College_Mantra.model.MessProduct;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class MessProductCreateRequest {

    @NotBlank(message = "Product Name Should Not Be NULL")
    private String productName;

    @NotNull(message = "Product Price Should Not Be NULL")
    private int price;

    public MessProduct to(){
        return MessProduct.builder()
                .productName(this.productName)
                .price(this.price)
                .build();
    }

}
