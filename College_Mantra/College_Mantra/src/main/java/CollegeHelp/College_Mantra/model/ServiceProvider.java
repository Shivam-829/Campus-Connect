package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.files.ProfileImage;
import CollegeHelp.College_Mantra.response.UserResponse;
import CollegeHelp.College_Mantra.service.DownloadImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProvider implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String gmail;

    @Column(nullable = false,unique = true)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private College college;

    @OneToOne
    @JoinColumn
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn
    @JsonIgnore
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "serviceProvider",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ServiceRequest> serviceRequests;

    @OneToMany(mappedBy = "serviceProvider")
    @JsonIgnore
    private List<ServiceTransaction> serviceTransactions;

    private long numberOfReputations;

    private double rating;

    @OneToMany(mappedBy = "serviceProvider")
    @JsonIgnore
    private List<RoomService> roomServices;

//    @OneToMany
//    @JsonIgnore
//    private List<Transaction> transactionList;

    public UserResponse to() throws IOException {
        UserResponse.UserResponseBuilder userResponseBuilder =  UserResponse.builder()
                .authority(this.user.getAuthority())
                .name(this.name)
                .gmail(this.gmail)
                .phoneNumber(this.phoneNumber)
                .rating(this.rating)
                .numberOfReputations(this.numberOfReputations)
                .reputationResponseList(Reputation.toReputationResponseList(this.user.getReputationsGot()))
                .college(this.college)
                .id(this.user.getId());

        if(this.profileImage != null){
            String fileDownloadUri = DownloadImage.generateImgUri(this.profileImage.getName());

            userResponseBuilder.profileImg(fileDownloadUri);
        }

        return userResponseBuilder.build();
    }

}
