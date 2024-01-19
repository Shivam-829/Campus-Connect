package CollegeHelp.College_Mantra.request;

import CollegeHelp.College_Mantra.model.Mess;
import CollegeHelp.College_Mantra.model.Student;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MessCreateRequest implements Serializable {

    @NotBlank(message = "Student name should not be BLANK")
    private String name;

    @NotBlank(message = "gmail should not be BLANK")
    private String gmail;

    @NotBlank(message = "Phone Number should not be BLANK")
    private String phoneNumber;

    @NotNull(message = "College Id should not be null")
    private Long collegeId;

    @NotBlank(message = "username should not be null")
    private String username;

    @NotBlank(message = "password should not be blank")
    private String password;

    public Mess to(){
        return Mess.builder()
                .name(this.name)
                .gmail(this.gmail)
                .phoneNumber(this.phoneNumber)
                .numberOfReputations(0)
                .rating(0.0)
                .build();
    }

    public UserCreateRequest toUser(){
        return UserCreateRequest.builder()
                .username(this.username)
                .password(this.password)
                .mess(this.to()).build();
    }

}
