package ra.edu.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoundCriterionRequest {

    private Long roundId; // Used for POST

    private Long criterionId; // Used for POST

    @NotNull(message = "Trọng số (weight) không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Trọng số phải lớn hơn 0")
    private BigDecimal weight;
}
