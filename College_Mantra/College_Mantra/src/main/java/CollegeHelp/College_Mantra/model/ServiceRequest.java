package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.enums.RequestStatus;
import CollegeHelp.College_Mantra.response.ServiceRequestResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ServiceRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Student student;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private ServiceProvider serviceProvider;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private RoomService roomService;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    public ServiceRequestResponse to() throws IOException {
        return ServiceRequestResponse.builder()
                .id(this.id)
                .studentResponse(this.student.to())
                .serviceProviderResponse(this.serviceProvider.to())
                .roomServiceResponse(roomService.to())
                .requestStatus(this.requestStatus)
                .build();
    }

}
