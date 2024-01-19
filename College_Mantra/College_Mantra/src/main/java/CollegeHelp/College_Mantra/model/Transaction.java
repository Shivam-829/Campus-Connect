package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.response.TransactionResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String order_id;

    @Column
    private int amount;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Mess mess;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Student student;

    @Column
    private String description;

    @CreationTimestamp
    private Date createDate;

    public TransactionResponse to() throws IOException {
        return TransactionResponse.builder()
                .order_id(this.order_id)
                .amount(this.amount)
                .messResponse(this.mess.to())
                .studentResponse(this.student.to())
                .description(this.description)
                .creationDate(this.createDate).build();
    }

}
