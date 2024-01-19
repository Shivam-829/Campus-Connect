package CollegeHelp.College_Mantra.response;

import CollegeHelp.College_Mantra.enums.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollegeResponse {

    private String name;

    private City city;

}
