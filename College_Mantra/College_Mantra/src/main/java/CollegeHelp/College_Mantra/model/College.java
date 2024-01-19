package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.enums.City;
import CollegeHelp.College_Mantra.response.CollegeResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class College implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name",unique = true,nullable = false)
    private String name;

    @OneToMany(mappedBy = "college")
    @JsonIgnore
    private List<Student> studentList;

    @OneToMany(mappedBy = "college")
    @JsonIgnore
    private List<RoomOwner> roomOwnerList;

    @OneToMany(mappedBy = "college")
    @JsonIgnore
    private List<Mess> messList;

    @OneToMany(mappedBy = "college")
    @JsonIgnore
    private List<ServiceProvider> serviceProviderList;

    @Column(name = "city")
    @Enumerated(EnumType.STRING)
    private City city;

    @OneToMany(mappedBy = "college")
    @JsonIgnore
    private List<Room> roomList;

    @OneToMany(mappedBy = "college")
    @JsonIgnore
    private List<RoomService> roomServiceList;

    @CreationTimestamp
    private Date createDate;

    @UpdateTimestamp
    private Date updateDate;

    public CollegeResponse to(){
        return CollegeResponse.builder()
                .name(this.name)
                .city(this.city)
                .build();
    }

}
