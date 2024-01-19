package CollegeHelp.College_Mantra.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CreationRequestHolder extends Object implements Serializable {

    private Boolean isMailVerified;

    private Boolean isNumberVerified;

    private String tempId;

    private StudentCreateRequest studentCreateRequest;

    private RoomHonourCreateRequest roomHonourCreateRequest;

    private ServiceProviderCreateRequest serviceProviderCreateRequest;

    private MessCreateRequest messCreateRequest;

}
