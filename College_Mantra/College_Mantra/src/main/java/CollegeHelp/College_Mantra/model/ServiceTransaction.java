package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.response.ServiceTransactionResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String transactionId;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private ServiceProvider serviceProvider;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Student student;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private RoomService roomService;

    public ServiceTransactionResponse to() throws IOException {
        return ServiceTransactionResponse.builder()
                .roomServiceResponse(this.roomService.to())
                .serviceProviderResponse(this.serviceProvider.to())
                .studentResponse(this.student.to())
                .transactionId(this.transactionId)
                .build();
    }

}
