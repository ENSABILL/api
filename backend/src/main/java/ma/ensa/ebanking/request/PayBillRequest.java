package ma.ensa.ebanking.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.models.Operation;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PayBillRequest {
    Long operationId;
}
