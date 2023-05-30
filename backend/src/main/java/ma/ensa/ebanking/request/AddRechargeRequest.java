package ma.ensa.ebanking.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.RechargeAmount;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddRechargeRequest {
    private String clientId;
    private String serviceId;
    private RechargeAmount amount;
}
