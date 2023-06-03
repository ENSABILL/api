package ma.ensa.ebanking.controllers;

import lombok.Data;

@Data
public class TransferInternalDto {
    private String username;
    private double amount;
}
