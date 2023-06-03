package ma.ensa.ebanking.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.models.user.Agent;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "agency")
    private List<Service> services;

    @OneToMany(mappedBy = "agency")
    private List<Agent> agents = new ArrayList<>();

    public void showActiveServicesOnly(){
        services = services
                .stream()
                .filter(Service::isActive)
                .toList();
    }

}
