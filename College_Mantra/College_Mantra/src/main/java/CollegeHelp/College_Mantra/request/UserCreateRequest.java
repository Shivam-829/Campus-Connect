package CollegeHelp.College_Mantra.request;

import CollegeHelp.College_Mantra.enums.Authority;
import CollegeHelp.College_Mantra.model.Mess;
import CollegeHelp.College_Mantra.model.RoomOwner;
import CollegeHelp.College_Mantra.model.ServiceProvider;
import CollegeHelp.College_Mantra.model.Student;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest implements Serializable {

    private String username;
    private String password;
    private Authority authority;
    private Student student;
    private RoomOwner roomOwner;
    private ServiceProvider serviceProvider;
    private Mess mess;

}
