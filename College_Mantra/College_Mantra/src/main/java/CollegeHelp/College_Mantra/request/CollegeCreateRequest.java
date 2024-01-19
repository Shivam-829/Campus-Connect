package CollegeHelp.College_Mantra.request;

import CollegeHelp.College_Mantra.enums.City;
import CollegeHelp.College_Mantra.model.College;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CollegeCreateRequest {

    @NotBlank(message = "College name must have some value")
    @NotNull(message = "College name must have some value")
    private String name;

    @NotBlank(message = "City must have some value")
    @NotNull(message = "City must have some value")
    private City city;

    public College to(){
        return College.builder()
                .name(this.name)
                .city(this.city)
                .build();
    }

}
