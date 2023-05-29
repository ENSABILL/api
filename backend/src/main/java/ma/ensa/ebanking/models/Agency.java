package ma.ensa.ebanking.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Agency {

    @Id
    private String imm;

    private String name;

    private String patentId;

    private String creditCardNumber;

    @OneToMany
    private List<Service> services;

    public void showActiveServicesOnly(){
        services = services
                .stream()
                .filter(Service::isActive)
                .toList();
    }

}
