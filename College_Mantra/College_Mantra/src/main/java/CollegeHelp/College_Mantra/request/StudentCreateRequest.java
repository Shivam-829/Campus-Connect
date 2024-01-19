package CollegeHelp.College_Mantra.request;

import CollegeHelp.College_Mantra.files.ProfileImage;
import CollegeHelp.College_Mantra.model.Student;
import CollegeHelp.College_Mantra.model.User;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class StudentCreateRequest implements Serializable {

    private static Logger LOGGER = LoggerFactory.getLogger(StudentCreateRequest.class);

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

    public Student to(){
        return Student.builder()
                .name(this.name)
                .gmail(this.gmail)
                .phoneNumber(this.phoneNumber)
                .numberOfReputations(0)
                .rating(0.0)
                .build();
    }

    public UserCreateRequest toUser(){
        LOGGER.info("Username : {}",this.username);
        LOGGER.info("Username : {}",this.getUsername());
        return UserCreateRequest.builder()
                .username(this.username)
                .password(this.password)
                .student(this.to()).build();
    }

}
