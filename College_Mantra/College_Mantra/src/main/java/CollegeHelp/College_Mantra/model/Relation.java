package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.enums.RelationStatus;
import CollegeHelp.College_Mantra.response.RelationResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Relation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int rent;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Student student;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private RoomOwner roomOwner;

    @Enumerated(EnumType.STRING)
    private RelationStatus relationStatus;

    @ManyToOne
    @JoinColumn
    private Room room;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;

    public RelationResponse to() throws IOException {
        return RelationResponse.builder()
                .id(this.id)
                .rent(this.rent)
                .status(this.relationStatus)
                .roomHonour(this.roomOwner.to())
                .student(this.student.to())
                .createTime(this.createTime)
                .roomResponse(this.room.to())
                .build();
    }

}
