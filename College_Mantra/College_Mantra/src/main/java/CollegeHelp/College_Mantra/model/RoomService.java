package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.enums.City;
import CollegeHelp.College_Mantra.enums.ServiceType;
import CollegeHelp.College_Mantra.response.RoomServiceResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private ServiceProvider serviceProvider;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private College college;

    @Enumerated(EnumType.STRING)
    private City city;

    @OneToMany(mappedBy = "roomService")
    @JsonIgnore
    private List<ServiceRequest> serviceRequestList;

    @OneToMany(mappedBy = "roomService")
    @JsonIgnore
    private List<ServiceTransaction> serviceTransactions;

    public RoomServiceResponse to() throws IOException {
        return RoomServiceResponse.builder()
                .id(this.id)
                .serviceType(this.serviceType)
                .price(this.price)
                .serviceProviderResponse(this.serviceProvider.to()).build();
    }

}
