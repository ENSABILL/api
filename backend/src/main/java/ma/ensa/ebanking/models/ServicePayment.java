package ma.ensa.ebanking.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.models.user.Client;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class ServicePayment {

    @Id
    @GeneratedValue(strategy = UUID)
    private String id;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Service service;

    private double amount;

    /*
    @Column(length = 1023)
    private String data;
    */

    @CreationTimestamp
    private LocalDateTime at;
}
