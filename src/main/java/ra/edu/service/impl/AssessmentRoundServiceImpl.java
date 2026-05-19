package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.dto.request.AssessmentRoundRequest;
import ra.edu.dto.request.RoundCriterionRequestItem;
import ra.edu.dto.response.AssessmentRoundResponse;
import ra.edu.dto.response.RoundCriterionItemResponse;
import ra.edu.entity.AssessmentRound;
import ra.edu.entity.EvaluationCriterion;
import ra.edu.entity.InternshipPhase;
import ra.edu.entity.RoundCriterion;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.repository.AssessmentRoundRepository;
import ra.edu.repository.EvaluationCriterionRepository;
import ra.edu.repository.InternshipPhaseRepository;
import ra.edu.repository.RoundCriterionRepository;
import ra.edu.service.AssessmentRoundService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentRoundServiceImpl implements AssessmentRoundService {

    private final AssessmentRoundRepository assessmentRoundRepository;
    private final InternshipPhaseRepository internshipPhaseRepository;
    private final EvaluationCriterionRepository evaluationCriterionRepository;
    private final RoundCriterionRepository roundCriterionRepository;

    @Override
    public List<AssessmentRoundResponse> getAllAssessmentRounds(Long phaseId) {
        List<AssessmentRound> rounds = assessmentRoundRepository.findAllWithFilter(phaseId);
        return rounds.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AssessmentRoundResponse getAssessmentRoundById(Long roundId) {
        AssessmentRound round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt đánh giá với ID: " + roundId));
        return toResponse(round);
    }

    @Override
    @Transactional
    public AssessmentRoundResponse createAssessmentRound(AssessmentRoundRequest request) {
        InternshipPhase phase = internshipPhaseRepository.findById(request.getPhaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt thực tập với ID: " + request.getPhaseId()));

        AssessmentRound round = AssessmentRound.builder()
                .phase(phase)
                .roundName(request.getRoundName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        AssessmentRound savedRound = assessmentRoundRepository.save(round);

        if (request.getCriteria() != null && !request.getCriteria().isEmpty()) {
            for (RoundCriterionRequestItem item : request.getCriteria()) {
                EvaluationCriterion criterion = evaluationCriterionRepository.findById(item.getCriterionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá với ID: " + item.getCriterionId()));

                RoundCriterion roundCriterion = RoundCriterion.builder()
                        .assessmentRound(savedRound)
                        .evaluationCriterion(criterion)
                        .weight(item.getWeight())
                        .build();

                roundCriterionRepository.save(roundCriterion);
                savedRound.getRoundCriteria().add(roundCriterion);
            }
        }

        return toResponse(savedRound);
    }

    @Override
    public AssessmentRoundResponse updateAssessmentRound(Long roundId, AssessmentRoundRequest request) {
        AssessmentRound round = assessmentRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt đánh giá với ID: " + roundId));

        if (!round.getPhase().getPhaseId().equals(request.getPhaseId())) {
            InternshipPhase phase = internshipPhaseRepository.findById(request.getPhaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đợt thực tập với ID: " + request.getPhaseId()));
            round.setPhase(phase);
        }

        round.setRoundName(request.getRoundName());
        round.setStartDate(request.getStartDate());
        round.setEndDate(request.getEndDate());
        round.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            round.setIsActive(request.getIsActive());
        }

        return toResponse(assessmentRoundRepository.save(round));
    }

    @Override
    public void deleteAssessmentRound(Long roundId) {
        if (!assessmentRoundRepository.existsById(roundId)) {
            throw new ResourceNotFoundException("Không tìm thấy đợt đánh giá với ID: " + roundId);
        }
        assessmentRoundRepository.deleteById(roundId);
    }

    private AssessmentRoundResponse toResponse(AssessmentRound round) {
        List<RoundCriterionItemResponse> criteriaResponses = round.getRoundCriteria().stream()
                .map(rc -> RoundCriterionItemResponse.builder()
                        .roundCriterionId(rc.getRoundCriterionId())
                        .criterionId(rc.getEvaluationCriterion().getCriterionId())
                        .criterionName(rc.getEvaluationCriterion().getCriterionName())
                        .weight(rc.getWeight())
                        .build())
                .collect(Collectors.toList());

        return AssessmentRoundResponse.builder()
                .roundId(round.getRoundId())
                .phaseId(round.getPhase().getPhaseId())
                .roundName(round.getRoundName())
                .startDate(round.getStartDate())
                .endDate(round.getEndDate())
                .description(round.getDescription())
                .isActive(round.getIsActive())
                .createdAt(round.getCreatedAt())
                .updatedAt(round.getUpdatedAt())
                .criteria(criteriaResponses)
                .build();
    }
}
