package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.response.ReputationResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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
public class Reputation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private User user1; // User Which Is Giving The Reputation

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private User user2; // User Which is Getting The Reputation

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private double rating;

    @CreationTimestamp
    private Date creationDate;

    public static List<ReputationResponse> toReputationResponseList(List<Reputation> reputationList) {
        return reputationList
                .stream()
                .map(Reputation::to)
                .collect(Collectors.toList());
    }

    public ReputationResponse to(){

        return ReputationResponse.builder()
                .userIdOfUserWhoGave(this.user1.getId())
                .getUserIdOfUserWhoGot(this.user2.getId())
                .comment(this.comment)
                .rating(this.rating)
                .build();

    }

}
