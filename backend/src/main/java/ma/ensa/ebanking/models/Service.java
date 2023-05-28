package ma.ensa.ebanking.models;

import jakarta.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private ServiceType type;

    private boolean active;

    @ManyToOne
    private Agency agency;

    @PrePersist
    public void init(){
        this.active = true;
    }

}
