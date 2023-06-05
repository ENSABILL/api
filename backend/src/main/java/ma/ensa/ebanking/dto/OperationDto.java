package ma.ensa.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.OperationStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationDto {
    private Long id;
    private Float amount;
    private OperationStatus operationStatus;
    private LocalDateTime operationTime;
    private String clientId;
    private String clientUsername;
    private String clientEmail;
    private ServiceDto service;
}
