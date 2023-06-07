package ma.ensa.ebanking.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private double price;

    private int qte;

    private String imageUrl;

    @ManyToOne
    private Agency agency;

    @CreationTimestamp
    private LocalDateTime addedAt;

    @OneToMany(mappedBy = "product")
    private List<ProductOrder> orderList;


}
