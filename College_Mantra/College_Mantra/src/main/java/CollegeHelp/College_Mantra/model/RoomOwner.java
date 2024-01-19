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
public class RoomOwner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "gmail",unique = true,nullable = false)
    private String gmail;

    @Column(name = "phone_number",unique = true,nullable = false)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private College college;

    @OneToOne
    @JsonIgnore
    @JoinColumn
    private User user;

    @JoinColumn(name = "profile_photo")
    @OneToOne
    @JsonIgnore
    private ProfileImage profileImage;

    @OneToMany(mappedBy = "roomOwner")
    @JsonIgnore
    private List<Student> studentList;

    @Column(name = "reputations")
    private long numberOfReputation;

    @Column(name = "rating")
    private double rating;

    @ManyToMany(mappedBy = "requestGot")
    @JsonIgnore
    private List<Student> requestSent;

    @ManyToMany(mappedBy = "requestSent")
    @JsonIgnore
    private List<Student> requestGot;

    @OneToMany(mappedBy = "roomOwner")
    private List<Relation> relationList;

    @OneToMany(mappedBy = "roomOwner")
    @JsonIgnore
    private List<Room> roomList;

    @CreationTimestamp
    private Date createDate;

    @UpdateTimestamp
    private Date updateDate;

    public UserResponse to() throws IOException {
        UserResponse.UserResponseBuilder userResponseBuilder =  UserResponse.builder()
                .authority(this.user.getAuthority())
                .name(this.name)
                .gmail(this.gmail)
                .phoneNumber(this.phoneNumber)
                .rating(this.rating)
                .numberOfReputations(this.numberOfReputation)
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
