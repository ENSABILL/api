package ma.ensa.ebanking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

    private String id;

    private String name;

    private String agencyName;

    private int qte;

    private double price;


}