package ma.ensa.ebanking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {

    private String orderId;

    private String productId;

    private int orderQte;

    private String status;

    private ClientDto client;

}