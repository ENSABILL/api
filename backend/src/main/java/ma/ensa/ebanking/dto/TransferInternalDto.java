package ma.ensa.ebanking.dto;

import lombok.Data;

@Data
public class TransferInternalDto {
    private String username;

    private double amount;
}
