package ra.edu.service;

import ra.edu.dto.request.RoundCriterionRequest;
import ra.edu.dto.response.RoundCriterionResponse;

import java.util.List;

public interface RoundCriterionService {
    List<RoundCriterionResponse> getCriteriaByRoundId(Long roundId);
    RoundCriterionResponse getRoundCriterionById(Long roundCriterionId);
    RoundCriterionResponse addCriterionToRound(RoundCriterionRequest request);
    RoundCriterionResponse updateCriterionWeight(Long roundCriterionId, RoundCriterionRequest request);
    void removeCriterionFromRound(Long roundCriterionId);
}
