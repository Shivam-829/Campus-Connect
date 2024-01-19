package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.files.ProfileImage;
import CollegeHelp.College_Mantra.response.UserResponse;
import CollegeHelp.College_Mantra.service.DownloadImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Mess implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true,nullable = false)
    private String name;

    @Column(unique = true,nullable = false)
    private String gmail;

    @Column(unique = true,nullable = false)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private College college;

    @Column
    private String address;

    @Column
    private String keyId;

    @Column
    private String keySecret;

    @OneToOne
    @JsonIgnore
    @JoinColumn
    private User user;

    @OneToOne
    @JoinColumn
    @JsonIgnore
    private ProfileImage profileImage;

    @Column
    private long numberOfReputations;

    private double rating;

    @OneToMany(mappedBy = "mess")
    @JsonIgnore
    private List<Transaction> transactionList;

    @OneToMany(mappedBy = "mess")
    @JsonIgnore
    private List<MessProduct> messProducts;

    @OneToMany(mappedBy = "mess")
    @JsonIgnore
    private List<MessProductCart> messProductCarts;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;

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
