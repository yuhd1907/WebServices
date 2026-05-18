package ra.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class RoundCriterionItemResponse {
    private Long roundCriterionId;
    private Long criterionId;
    private String criterionName;
    private BigDecimal weight;
}
