package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.edu.dto.request.EvaluationCriterionRequest;
import ra.edu.dto.response.EvaluationCriterionResponse;
import ra.edu.entity.EvaluationCriterion;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ResourceNotFoundException;
import ra.edu.repository.EvaluationCriterionRepository;
import ra.edu.service.EvaluationCriterionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationCriterionServiceImpl implements EvaluationCriterionService {

    private final EvaluationCriterionRepository criterionRepository;

    @Override
    public List<EvaluationCriterionResponse> getAllCriteria() {
        return criterionRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationCriterionResponse getCriterionById(Long criterionId) {
        EvaluationCriterion criterion = criterionRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá với id: " + criterionId));
        return toResponse(criterion);
    }

    @Override
    public EvaluationCriterionResponse createCriterion(EvaluationCriterionRequest request) {
        if (criterionRepository.existsByCriterionName(request.getCriterionName())) {
            throw new ConflictException("Tên tiêu chí đã tồn tại: " + request.getCriterionName());
        }
        EvaluationCriterion criterion = EvaluationCriterion.builder()
                .criterionName(request.getCriterionName())
                .description(request.getDescription())
                .maxScore(request.getMaxScore())
                .build();
        return toResponse(criterionRepository.save(criterion));
    }

    @Override
    public EvaluationCriterionResponse updateCriterion(Long criterionId, EvaluationCriterionRequest request) {
        EvaluationCriterion criterion = criterionRepository.findById(criterionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá với id: " + criterionId));

        if (!criterion.getCriterionName().equals(request.getCriterionName())
                && criterionRepository.existsByCriterionName(request.getCriterionName())) {
            throw new ConflictException("Tên tiêu chí đã tồn tại: " + request.getCriterionName());
        }

        criterion.setCriterionName(request.getCriterionName());
        criterion.setDescription(request.getDescription());
        criterion.setMaxScore(request.getMaxScore());

        return toResponse(criterionRepository.save(criterion));
    }

    @Override
    public void deleteCriterion(Long criterionId) {
        if (!criterionRepository.existsById(criterionId)) {
            throw new ResourceNotFoundException("Không tìm thấy tiêu chí đánh giá với id: " + criterionId);
        }
        criterionRepository.deleteById(criterionId);
    }

    private EvaluationCriterionResponse toResponse(EvaluationCriterion criterion) {
        return EvaluationCriterionResponse.builder()
                .criterionId(criterion.getCriterionId())
                .criterionName(criterion.getCriterionName())
                .description(criterion.getDescription())
                .maxScore(criterion.getMaxScore())
                .createdAt(criterion.getCreatedAt())
                .updatedAt(criterion.getUpdatedAt())
                .build();
    }
}
