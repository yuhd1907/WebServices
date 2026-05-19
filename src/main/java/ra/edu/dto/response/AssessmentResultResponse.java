package ra.edu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AssessmentResultResponse {
    private Long resultId;
    private Long assignmentId;
    private Long studentId;
    private String studentName;
    private Long roundId;
    private String roundName;
    private Long criterionId;
    private String criterionName;
    private Double score;
    private String comments;
    private Long evaluatedById;
    private String evaluatedByName;
    private LocalDateTime evaluationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
