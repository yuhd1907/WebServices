package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.EvaluationCriterionRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.EvaluationCriterionResponse;
import ra.edu.service.EvaluationCriterionService;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation_criteria")
@RequiredArgsConstructor
public class EvaluationCriterionController {

    private final EvaluationCriterionService criterionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EvaluationCriterionResponse>>> getAllCriteria() {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách tiêu chí đánh giá thành công",
                criterionService.getAllCriteria()
        ));
    }

    @GetMapping("/{criterion_id}")
    public ResponseEntity<ApiResponse<EvaluationCriterionResponse>> getCriterionById(
            @PathVariable("criterion_id") Long criterionId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy thông tin tiêu chí đánh giá thành công",
                criterionService.getCriterionById(criterionId)
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EvaluationCriterionResponse>> createCriterion(
            @Valid @RequestBody EvaluationCriterionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Tạo tiêu chí đánh giá thành công",
                criterionService.createCriterion(request)
        ));
    }

    @PutMapping("/{criterion_id}")
    public ResponseEntity<ApiResponse<EvaluationCriterionResponse>> updateCriterion(
            @PathVariable("criterion_id") Long criterionId,
            @Valid @RequestBody EvaluationCriterionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Cập nhật tiêu chí đánh giá thành công",
                criterionService.updateCriterion(criterionId, request)
        ));
    }

    @DeleteMapping("/{criterion_id}")
    public ResponseEntity<ApiResponse<Void>> deleteCriterion(
            @PathVariable("criterion_id") Long criterionId) {
        criterionService.deleteCriterion(criterionId);
        return ResponseEntity.ok(ApiResponse.success("Xóa tiêu chí đánh giá thành công"));
    }
}
