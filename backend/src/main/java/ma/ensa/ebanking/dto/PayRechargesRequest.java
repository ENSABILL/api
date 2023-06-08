package ma.ensa.ebanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PayRechargesRequest {
    private List<Float> amounts = new ArrayList<>();
    private String serviceId;
    private String token;
}
