package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.RoundCriterionRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.RoundCriterionResponse;
import ra.edu.service.RoundCriterionService;

import java.util.List;

@RestController
@RequestMapping("/api/round_criteria")
@RequiredArgsConstructor
public class RoundCriterionController {

    private final RoundCriterionService roundCriterionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoundCriterionResponse>>> getCriteriaByRoundId(
            @RequestParam("round_id") Long roundId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy danh sách tiêu chí của đợt đánh giá thành công",
                roundCriterionService.getCriteriaByRoundId(roundId)
        ));
    }

    @GetMapping("/{round_criterion_id}")
    public ResponseEntity<ApiResponse<RoundCriterionResponse>> getRoundCriterionById(
            @PathVariable("round_criterion_id") Long roundCriterionId) {
        return ResponseEntity.ok(ApiResponse.success(
                "Lấy chi tiết tiêu chí trong đợt đánh giá thành công",
                roundCriterionService.getRoundCriterionById(roundCriterionId)
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoundCriterionResponse>> addCriterionToRound(
            @Valid @RequestBody RoundCriterionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Thêm tiêu chí vào đợt đánh giá thành công",
                roundCriterionService.addCriterionToRound(request)
        ));
    }

    @PutMapping("/{round_criterion_id}")
    public ResponseEntity<ApiResponse<RoundCriterionResponse>> updateCriterionWeight(
            @PathVariable("round_criterion_id") Long roundCriterionId,
            @Valid @RequestBody RoundCriterionRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                "Cập nhật trọng số của tiêu chí thành công",
                roundCriterionService.updateCriterionWeight(roundCriterionId, request)
        ));
    }

    @DeleteMapping("/{round_criterion_id}")
    public ResponseEntity<ApiResponse<Void>> removeCriterionFromRound(
            @PathVariable("round_criterion_id") Long roundCriterionId) {
        roundCriterionService.removeCriterionFromRound(roundCriterionId);
        return ResponseEntity.ok(ApiResponse.success("Xóa tiêu chí khỏi đợt đánh giá thành công"));
    }
}
