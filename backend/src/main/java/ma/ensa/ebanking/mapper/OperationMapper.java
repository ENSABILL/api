package ma.ensa.ebanking.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.models.Operation;
import org.springframework.beans.BeanUtils;

//@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperationMapper {

    public static OperationDto mapOperation(Operation operation){
        OperationDto operationDto = new OperationDto();
        try {
            BeanUtils.copyProperties(operationDto, operation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return operationDto;
    }
}
