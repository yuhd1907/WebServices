package ra.edu.mapper;

import ra.edu.dto.response.AssessmentResultResponse;
import ra.edu.entity.AssessmentResult;

public class AssessmentResultMapper {

    private AssessmentResultMapper() {
        // Private constructor để ẩn constructor mặc định
    }

    public static AssessmentResultResponse toResponse(AssessmentResult result) {
        if (result == null) {
            return null;
        }
        
        return AssessmentResultResponse.builder()
                .resultId(result.getResultId())
                .assignmentId(result.getAssignment().getAssignmentId())
                .studentId(result.getAssignment().getStudent().getStudentId())
                .studentName(result.getAssignment().getStudent().getUser().getFullName())
                .roundId(result.getRound().getRoundId())
                .roundName(result.getRound().getRoundName())
                .criterionId(result.getCriterion().getCriterionId())
                .criterionName(result.getCriterion().getCriterionName())
                .score(result.getScore())
                .comments(result.getComments())
                .evaluatedById(result.getEvaluatedBy().getUserId())
                .evaluatedByName(result.getEvaluatedBy().getFullName())
                .evaluationDate(result.getEvaluationDate())
                .createdAt(result.getCreatedAt())
                .updatedAt(result.getUpdatedAt())
                .build();
    }
}
