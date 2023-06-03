package ma.ensa.ebanking.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.ebanking.enums.ServiceType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindServiceRequest {
    String keyword;
    ServiceType type;
}
