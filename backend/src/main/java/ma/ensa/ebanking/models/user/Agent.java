package ma.ensa.ebanking.models.user;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ma.ensa.ebanking.models.Agency;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@SuperBuilder

@Entity
public class Agent extends User {

    @ManyToOne
    private Agency agency;
}