package ma.ensa.ebanking.dto;

import lombok.Data;

@Data
public class PaymentServiceDto {
    private String serviceId;
    private double amount;
    private String data;
}
