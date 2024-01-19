package CollegeHelp.College_Mantra.request;

import CollegeHelp.College_Mantra.enums.ServiceType;
import CollegeHelp.College_Mantra.model.RoomService;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class RoomServiceRequest {

    @NotBlank(message = "Service Type Should Not Be Blank")
    private ServiceType serviceType;

    @NotNull(message = "Price Should Not Be NULL")
    private int price;

    public RoomService to(){
        return RoomService.builder()
                .serviceType(this.serviceType)
                .price(this.price)
                .build();
    }

}
