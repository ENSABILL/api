package ma.ensa.ebanking.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.OperationStatus;
import ma.ensa.ebanking.models.user.Client;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Operation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float amount;

    @ManyToOne(fetch = FetchType.EAGER)
    private ServiceProduct serviceProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    private OperationStatus operationStatus;

    private LocalDateTime operationTime;

}
