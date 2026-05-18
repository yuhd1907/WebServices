package ra.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EvaluationCriterionResponse {
    private Long criterionId;
    private String criterionName;
    private String description;
    private BigDecimal maxScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
