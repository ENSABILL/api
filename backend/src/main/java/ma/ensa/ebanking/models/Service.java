package ma.ensa.ebanking.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.ServiceType;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "service",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<ServiceProduct> products = new ArrayList<>();

    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER)
    private List<Operation> operations = new ArrayList<>();

    @PrePersist
    public void init(){
        this.active = true;
    }

}
