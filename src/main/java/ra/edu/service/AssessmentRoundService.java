package ra.edu.service;

import ra.edu.dto.request.AssessmentRoundRequest;
import ra.edu.dto.response.AssessmentRoundResponse;

import java.util.List;

public interface AssessmentRoundService {
    List<AssessmentRoundResponse> getAllAssessmentRounds(Long phaseId);
    AssessmentRoundResponse getAssessmentRoundById(Long roundId);
    AssessmentRoundResponse createAssessmentRound(AssessmentRoundRequest request);
    AssessmentRoundResponse updateAssessmentRound(Long roundId, AssessmentRoundRequest request);
    void deleteAssessmentRound(Long roundId);
}
