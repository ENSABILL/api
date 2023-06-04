package ma.ensa.ebanking.mapper;

import lombok.Data;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.models.Operation;


@Data
public class OperationMapper {

    public static OperationDto toDto(Operation operation) {

        return OperationDto.builder()
                .id(operation.getId())
                .operationStatus(operation.getOperationStatus())
                .operationTime(operation.getOperationTime())
                .clientEmail(operation.getClient().getEmail())
                .clientId(operation.getClient().getId())
                .service(ServiceMapper.toDto(operation.getService()))
                .clientUsername(operation.getClient().getUsername())
                .amount(operation.getAmount())
                .build();
    }
}
