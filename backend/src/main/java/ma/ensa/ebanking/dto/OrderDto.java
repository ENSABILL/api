package ma.ensa.ebanking.dto;

import lombok.Data;
import ma.ensa.ebanking.enums.OrderStatus;

@Data
public class OrderDto {

    private String orderId;

    private String productId;

    private int orderQte;

    private String status;

}