package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.edu.dto.request.RoundCriterionRequest;
import ra.edu.dto.response.RoundCriterionResponse;
import ra.edu.entity.AssessmentRound;
import ra.edu.entity.EvaluationCriterion;
import ra.edu.entity.RoundCriterion;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.repository.AssessmentRoundRepository;
import ra.edu.repository.EvaluationCriterionRepository;
import ra.edu.repository.RoundCriterionRepository;
import ra.edu.service.RoundCriterionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoundCriterionServiceImpl implements RoundCriterionService {

    private final RoundCriterionRepository roundCriterionRepository;
    private final AssessmentRoundRepository assessmentRoundRepository;
    private final EvaluationCriterionRepository evaluationCriterionRepository;

    @Override
    public List<RoundCriterionResponse> getCriteriaByRoundId(Long roundId) {
        return roundCriterionRepository.findByAssessmentRound_RoundId(roundId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoundCriterionResponse getRoundCriterionById(Long roundCriterionId) {
        RoundCriterion rc = roundCriterionRepository.findById(roundCriterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy RoundCriterion với ID: " + roundCriterionId));
        return toResponse(rc);
    }

    @Override
    public RoundCriterionResponse addCriterionToRound(RoundCriterionRequest request) {
        AssessmentRound round = assessmentRoundRepository.findById(request.getRoundId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt đánh giá với ID: " + request.getRoundId()));

        EvaluationCriterion criterion = evaluationCriterionRepository.findById(request.getCriterionId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá với ID: " + request.getCriterionId()));

        // Check if already exists to avoid unique constraint violation
        boolean exists = roundCriterionRepository.findByAssessmentRound_RoundId(request.getRoundId())
                .stream()
                .anyMatch(rc -> rc.getEvaluationCriterion().getCriterionId().equals(request.getCriterionId()));

        if (exists) {
            throw new ConflictException("Tiêu chí này đã tồn tại trong đợt đánh giá.");
        }

        RoundCriterion roundCriterion = RoundCriterion.builder()
                .assessmentRound(round)
                .evaluationCriterion(criterion)
                .weight(request.getWeight())
                .build();

        return toResponse(roundCriterionRepository.save(roundCriterion));
    }

    @Override
    public RoundCriterionResponse updateCriterionWeight(Long roundCriterionId, RoundCriterionRequest request) {
        RoundCriterion rc = roundCriterionRepository.findById(roundCriterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy RoundCriterion với ID: " + roundCriterionId));

        rc.setWeight(request.getWeight());
        return toResponse(roundCriterionRepository.save(rc));
    }

    @Override
    public void removeCriterionFromRound(Long roundCriterionId) {
        if (!roundCriterionRepository.existsById(roundCriterionId)) {
            throw new ResourceNotFoundException("Không tìm thấy RoundCriterion với ID: " + roundCriterionId);
        }
        roundCriterionRepository.deleteById(roundCriterionId);
    }

    private RoundCriterionResponse toResponse(RoundCriterion rc) {
        return RoundCriterionResponse.builder()
                .roundCriterionId(rc.getRoundCriterionId())
                .roundId(rc.getAssessmentRound().getRoundId())
                .roundName(rc.getAssessmentRound().getRoundName())
                .criterionId(rc.getEvaluationCriterion().getCriterionId())
                .criterionName(rc.getEvaluationCriterion().getCriterionName())
                .weight(rc.getWeight())
                .createdAt(rc.getCreatedAt())
                .updatedAt(rc.getUpdatedAt())
                .build();
    }
}
