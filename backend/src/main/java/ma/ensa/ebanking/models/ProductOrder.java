package ma.ensa.ebanking.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.OrderStatus;
import ma.ensa.ebanking.models.user.Client;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity

public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Product product;

    private int qte;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @PrePersist
    public void init(){
        orderStatus = OrderStatus.PENDING;
    }
}
