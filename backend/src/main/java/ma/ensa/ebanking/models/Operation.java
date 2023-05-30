package ma.ensa.ebanking.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.FactureStatus;
import ma.ensa.ebanking.models.user.Client;

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
    private Service service;

    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    private FactureStatus factureStatus;

}
