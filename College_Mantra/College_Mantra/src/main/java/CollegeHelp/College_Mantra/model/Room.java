package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.enums.AvailabilityType;
import CollegeHelp.College_Mantra.enums.RoomType;
import CollegeHelp.College_Mantra.files.ProfileImage;
import CollegeHelp.College_Mantra.response.RoomResponse;
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
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private int cost;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private RoomOwner roomOwner;

    @Column(nullable = false)
    private String address;

    @Column(name = "photo_list",nullable = false)
    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<ProfileImage> images;

    @Enumerated(EnumType.STRING)
    private AvailabilityType availabilityType;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Relation> relationList;

    @ManyToOne
    @JsonIgnore
    @JoinColumn
    private College college;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;

    public RoomResponse to() throws IOException {
        return RoomResponse.builder()
                .address(this.address)
                .roomHonourResponse(this.roomOwner.to())
                .roomType(this.roomType)
                .availabilityType(this.availabilityType)
                .cost(this.cost)
                .images(
                        this.images.stream().map(x -> DownloadImage.generateImgUri(x.getName())).collect(Collectors.toList())
                )
                .createTime(this.createTime)
                .id(this.id)
                .build();
    }

}
