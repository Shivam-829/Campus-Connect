package CollegeHelp.College_Mantra.files;

import CollegeHelp.College_Mantra.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileImage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "profileImage")
    @JsonIgnore
    private Student student;

    @OneToOne(mappedBy = "profileImage")
    @JsonIgnore
    private ServiceProvider serviceProvider;

    @OneToOne(mappedBy = "profileImage")
    @JsonIgnore
    private RoomOwner roomOwner;

    @OneToOne(mappedBy = "profileImage")
    @JsonIgnore
    private Mess mess;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Room room;

    private String type;

    @Column(unique = true,nullable = false)
    private String name;

    @Column(nullable = false)
    private String filePath;

}
