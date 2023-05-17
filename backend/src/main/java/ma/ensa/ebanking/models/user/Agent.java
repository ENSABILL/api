package ma.ensa.ebanking.models.user;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.ensa.ebanking.models.user.User;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@SuperBuilder

@Entity
public class Agent extends User {}