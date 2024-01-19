package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.response.MessProductCartResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.cache.annotation.EnableCaching;

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
public class MessProductCart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn
    @ManyToMany
    @JsonIgnore
    private List<MessProduct> messProductList;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Mess mess;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Student student;

    public MessProductCartResponse to() throws IOException {
        return MessProductCartResponse.builder()
                .id(this.id)
                .messProductList(MessProduct.toProductResponseList(this.messProductList))
                .messResponse(this.mess.to())
                .studentResponse(this.student.to())
                .build();
    }

}
