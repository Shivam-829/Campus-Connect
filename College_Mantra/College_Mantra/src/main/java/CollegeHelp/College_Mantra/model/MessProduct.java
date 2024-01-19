package CollegeHelp.College_Mantra.model;

import CollegeHelp.College_Mantra.response.MessProductResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessProduct implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Mess mess;

    @ManyToMany(mappedBy = "messProductList")
    @JsonIgnore
    private List<MessProductCart> messProductCarts;

    public static List<MessProductResponse> toProductResponseList(List<MessProduct> messProductList) {
        return messProductList.stream()
                .map(x -> {
                    try {
                        return x.to();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    public MessProductResponse to() throws IOException {
        return MessProductResponse.builder()
                .id(this.id)
                .productName(this.productName)
                .price(this.price)
                .build();
    }

}
