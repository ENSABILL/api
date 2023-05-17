package ma.ensa.ebanking.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.ServiceType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Service {

    @Id @GeneratedValue
    private long id;

    private String name;

    private ServiceType type;

    @ManyToOne
    private Agency agency;


}
