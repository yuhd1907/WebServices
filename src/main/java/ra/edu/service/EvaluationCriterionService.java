package ra.edu.service;

import ra.edu.dto.request.EvaluationCriterionRequest;
import ra.edu.dto.response.EvaluationCriterionResponse;

import java.util.List;

public interface EvaluationCriterionService {
    List<EvaluationCriterionResponse> getAllCriteria();
    EvaluationCriterionResponse getCriterionById(Long criterionId);
    EvaluationCriterionResponse createCriterion(EvaluationCriterionRequest request);
    EvaluationCriterionResponse updateCriterion(Long criterionId, EvaluationCriterionRequest request);
    void deleteCriterion(Long criterionId);
}
